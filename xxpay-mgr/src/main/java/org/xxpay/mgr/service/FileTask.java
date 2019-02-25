package org.xxpay.mgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.common.util.MyLog;
import org.xxpay.common.util.excel.FTPUtil;
import org.xxpay.common.util.excel.write.WriteExcel;
import org.xxpay.common.util.excel.write.WriteListExcel;
import org.xxpay.dal.dao.model.MchInfo;
import org.xxpay.dal.dao.model.PayOrder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FileTask {

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private MchInfoService mchInfoService;
    @Value("${ftp.server.ip}")
    private String ip;
    @Value("${ftp.server.pass}")
    private String pass;
    @Value("${ftp.server.user}")
    private String user;


    private final static MyLog _log = MyLog.getLog(FileTask.class);
    //文件分隔符
    private static final String separator=File.separator;

    /**
     * 定时任务：
     * 每天三点生成各商户前一日的交易账单并上传ftp
     */
//    @Scheduled(cron="0/10 * *  * * ? ")
    @Scheduled(cron="0 0 3 * * ?")
    public void exportTask(){
        List<MchInfo> mchInfos = mchInfoService.getMchInfoList(0,50,null);

        for(MchInfo mchInfo:mchInfos){

            String mchId=mchInfo.getMchId();
            System.out.println(mchId);
            List<PayOrder> orders = payOrderService.getYestardayPayOrderList(mchId);
            if(orders.size()>0){
               String excelPath= this.doExcelExport(orders,mchId);
                String txtPath=  this.doTxtExport(orders,mchId);
                doUpload(excelPath,txtPath);
            }else {
                _log.info(mchId+"订单数据为空");
            }
        }
        }
    /**
     * 生成excel，返回文件路径
     * @param list
     * @param mchId
     * @return
     */
    public  String doExcelExport(List list,String mchId){
        WriteExcel we = new WriteListExcel();
        Map<String,Object> param = new HashMap<String,Object>();
        String currentDate=this.currentDate();
//        String excelPath = "D:"+separator+currentDate+"-"+mchId+".xls";
        String excelPath="/home/app/project/file"+separator+currentDate+"-"+mchId+".xls";
        param.put("outFilePath", excelPath);
        if(!list.isEmpty()){
            try {
                we.write(param, list);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            _log.debug("ftp上传数据为空");
        }
        return excelPath;
    }

    /**
     * 生成txt，返回文件路径
     * @param mchId
     * @param orders
     */
    private String doTxtExport(List<PayOrder> orders,String mchId){
        List<String> list =new ArrayList<String>();
        list.add("支付订单号");
        list.add("商户ID");
        list.add("商户订单号");
        list.add("渠道ID");
        list.add("支付金额");
        list.add("三位货币代码");
        list.add("支付状态");
        list.add("客户端IP");
        list.add("设备");
        list.add("商品标题");
        list.add("商品描述信息");
        list.add("特定渠道发起时额外参数");
        list.add("渠道商户ID");
        list.add("渠道订单号");
        list.add("渠道支付错误码");
        list.add("渠道支付错误描述");
        list.add("扩展参数1");
        list.add("扩展参数2");
        list.add("通知地址");
        list.add("通知次数");
        list.add("最后一次通知时间");
        list.add("订单失效时间");
        list.add("订单支付成功时间");
        list.add("创建时间");
        list.add("更新时间");

        String currentDate=this.currentDate();
        String txtPath = separator+currentDate+"-"+mchId+".txt";
        FileWriter writer=null;
        String sb="";
        try {
            File file = new File(txtPath);
            if(!file.exists()){
                file.createNewFile();
            }
            writer = new FileWriter(file,false);//覆盖写入

            for(int i=0;i<list.size();i++){

                if(i==list.size()){
                    sb+=list.get(list.size());
                }else{
                    sb+=list.get(i)+",";
                }
            }
            writer.write(sb+"\r\n");

            for(int i=0;i<orders.size();i++){
                writer.write(orders.get(i).getPayOrderId()+","+orders.get(i).getMchId()+","+
                        orders.get(i).getMchOrderNo()+","+orders.get(i).getChannelId()+","+orders.get(i).getAmount()+","+
                        orders.get(i).getCurrency()+","+orders.get(i).getStatus()+","+orders.get(i).getClientIp()+","+
                        orders.get(i).getDevice()+","+orders.get(i).getSubject()+","+orders.get(i).getBody()+","+
                        orders.get(i).getExtra()+","+orders.get(i).getChannelMchId()+","+orders.get(i).getChannelOrderNo()+","+
                        orders.get(i).getMchId()+","+orders.get(i).getErrCode()+","+orders.get(i).getErrMsg()+","+
                        orders.get(i).getMchId()+","+orders.get(i).getParam1()+","+orders.get(i).getParam2()+","+
                        orders.get(i).getMchId()+","+orders.get(i).getNotifyUrl()+","+orders.get(i).getNotifyCount()+","+
                        orders.get(i).getMchId()+","+orders.get(i).getLastNotifyTime()+","+orders.get(i).getExpireTime()+","+
                        orders.get(i).getMchId()+","+orders.get(i).getPaySuccTime()+","+orders.get(i).getCreateTime()+","+
                        orders.get(i).getUpdateTime()+"\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return txtPath;
    }

    /**
     * 生成文件名yyyyMMdd
     * @return
     */
    public  String currentDate(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 上传ftp
     * @param excelPath
     * @param txtPath
     */
    private void doUpload(String excelPath,String txtPath){

        FTPUtil ftpUtil=new FTPUtil(ip,21,user,pass);
        File excelFile = new File(excelPath);
        File txtFile = new File(txtPath);

        try {
            List fileLists=new ArrayList<>();
            fileLists.add(excelFile);
            fileLists.add(txtFile);
            FTPUtil.uploadFile(fileLists,ftpUtil);
            //已经上传到ftp服务器上

            excelFile.delete();
            txtFile.delete();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
