package org.xxpay.mgr.service;

import java.util.Date;
import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.common.util.DateUtil;
import org.xxpay.common.util.HTTPUtil;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.PayOrder;

/**
 * Created by lzz
 * 检测订单，又异常信息发送模板消息报警.
 */
@Component
public class MonitorTask {

    private final static MyLog _log = MyLog.getLog(MonitorTask.class);

    @Value("${mail.from}")
    private String from;
    @Value("${mail.to}")
    private String to;
    @Value("${weixinToken}")
    private  String weixinToken;
    @Value("${touser}")
    private  String touser;
    @Value("${template_id}")
    private  String template_id;

    @Autowired
    private PayOrderService payOrderService;
    @Autowired
    private JavaMailSender mailSender;

//        @Scheduled(fixedDelay = 60 * 1000)
//    @Scheduled(cron = "0 0 * * *  ?")
    @Scheduled(cron="0/10 * *  * * ? ")
    public void healthCheck() {
        List<PayOrder> orders = payOrderService.selectOrderStatusCount(1);
        int s1 = 0;
        int s2 = 0;
        int s3 = 0;

        for (PayOrder payOrder : orders) {
            if (payOrder.getStatus() == 1) {
                s1++;
            } else if (payOrder.getStatus() == 2) {
                s2++;
            } else {
                s3++;
            }
        }
        if (s1 > 300) {
            String msg="前一小时-未支付订单超过指定数量——》"+s1;
            sendTemplateMessage(msg);
            _log.info("已发送未支付订单邮件->{}", s1);
        }
        if (s2 > 300) {
            String msg="前一小时-已支付订单超过指定数量——》"+s2;
            sendTemplateMessage(msg);
            _log.info("已发送支付订单邮件->{}", s2);
        }
        if (s3 > 300) {
            String msg="前一小时-通知成功订单超过指定数量——》"+s3;
            sendTemplateMessage(msg);
            _log.info("已发送通知成功邮件->{}", s3);
        }
    }

    /**
     * 发送邮件通知
     * @param message
     */
    private void sendAlertMessage(String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject("【订单状态】监控信息");
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    /**
     * 发送微信模板消息通知
     */

    public  void sendTemplateMessage(String keyword1) {
        // 获取token
        String jsonParam = HTTPUtil.sendGet(weixinToken);
        Map maps = (Map) JSON.parse(jsonParam);
        String token = (String) maps.get("token");

        String url="http://weixin.qq.com/download";
       String keyword2= DateUtil.date2Str(new Date());
        String requrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
        String reqdate= "{\"touser\":\""+touser+"\",\"template_id\":\""+template_id+"\",\""+
                url+"\":\""+url+"\",\"data\":{\"first\":{\"value\":\"服务器出现异常\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\""+
                keyword1+"\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\""+
                keyword2+"\",\"color\":\"#173177\"},\"remark\":{\"value\":\"请速度处理\",\"color\":\"#173177\"}}}";

        HTTPUtil.HttpPostWithJson(requrl,reqdate);
    }

}