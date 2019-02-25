package org.xxpay.mgr.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.common.util.AmountUtil;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.plugin.PageModel;
import org.xxpay.mgr.service.FileTask;
import org.xxpay.mgr.service.PayOrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/pay_order")
public class PayOrderController {

    private final static MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private FileTask fileTask;

    @RequestMapping("/list.html")
    public String listInput(ModelMap model) {
        return "pay_order/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public String list(@ModelAttribute PayOrder payOrder, Integer pageIndex, Integer pageSize) {
        PageModel pageModel = new PageModel();
        this.date2Str(payOrder);
        int count = payOrderService.count(payOrder);
        if(count <= 0) return JSON.toJSONString(pageModel);
        List<PayOrder> payOrderList = payOrderService.getPayOrderList((pageIndex-1)*pageSize, pageSize, payOrder);
        if(!CollectionUtils.isEmpty(payOrderList)) {
            JSONArray array = new JSONArray();
            for(PayOrder po : payOrderList) {
                JSONObject object = (JSONObject) JSONObject.toJSON(po);
                if(po.getCreateTime() != null) object.put("createTime", DateUtil.date2Str(po.getCreateTime()));
                if(po.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(po.getAmount()+""));
                array.add(object);
            }
            pageModel.setList(array);
        }
        pageModel.setCount(count);
        pageModel.setMsg("ok");
        pageModel.setRel(true);
        return JSON.toJSONString(pageModel);
    }

    @RequestMapping("/view.html")
    public String viewInput(String payOrderId, ModelMap model) {
        PayOrder item = null;
        if(StringUtils.isNotBlank(payOrderId)) {
            item = payOrderService.selectPayOrder(payOrderId);
        }
        if(item == null) {
            item = new PayOrder();
            model.put("item", item);
            return "pay_order/view";
        }
        JSONObject object = (JSONObject) JSON.toJSON(item);
        if(item.getPaySuccTime() != null) object.put("paySuccTime", DateUtil.date2Str(new Date(item.getPaySuccTime())));
        if(item.getLastNotifyTime() != null) object.put("lastNotifyTime", DateUtil.date2Str(new Date(item.getLastNotifyTime())));
        if(item.getExpireTime() != null) object.put("expireTime", DateUtil.date2Str(new Date(item.getExpireTime())));
        if(item.getAmount() != null) object.put("amount", AmountUtil.convertCent2Dollar(item.getAmount()+""));
        model.put("item", object);
        return "pay_order/view";
    }
    @RequestMapping("/export")
    @ResponseBody
    public String export(@ModelAttribute PayOrder payOrder, HttpServletRequest request, HttpServletResponse response) {
        this.date2Str(payOrder);
        List<PayOrder> payOrderList = payOrderService.getPayOrderList(payOrder);
        if(CollectionUtils.isEmpty(payOrderList)) {
            return "导出数据为空";
        }
        if(payOrderList.size()<10){
            return "数据太少，不支持导出";
        }
        String excelPath=fileTask.doExcelExport(payOrderList,System.currentTimeMillis()+"");
        File file = new File(excelPath);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("success");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return "下载失败";
}

    private PayOrder date2Str(PayOrder payOrder){
        String param1=payOrder.getParam1();
        if(param1!=null&&param1!=""){
            String[] aa=param1.split(" - ");
            Date creTime=DateUtil.str2Date(aa[0].trim());
            Date updTime=DateUtil.str2Date(aa[1].trim());
            payOrder.setCreateTime(creTime);
            payOrder.setUpdateTime(updTime);
        }

        return payOrder;
    }
}