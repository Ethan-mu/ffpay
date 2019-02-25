package org.xxpay.boot.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.boot.service.BaseService;
import org.xxpay.boot.service.Notify4BasePay;
import org.xxpay.dal.dao.model.PayOrder;


@RestController
public class MchNotifyController {
    @Autowired
    private Notify4BasePay notify4BasePay;

    @Autowired
    private BaseService baseService;


    @RequestMapping(value = "/api/mch/doMchNotify")
    public String doMchNotify(@RequestParam String params){
        JSONObject po = JSONObject.parseObject(params);
        return payOrder(po);

    }
    public String payOrder(@RequestBody JSONObject params) {
        String payOrderId = params.getString("payOrderId");
        PayOrder payOrder= baseService.baseSelectPayOrder(payOrderId);
        notify4BasePay.doNotify(payOrder,false);
        return "";
    }
}
