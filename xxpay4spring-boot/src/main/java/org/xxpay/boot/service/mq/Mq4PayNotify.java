package org.xxpay.boot.service.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.xxpay.boot.service.BaseService;
import org.xxpay.boot.service.IPayLogService;
import org.xxpay.common.util.MyLog;
import org.xxpay.dal.dao.model.PayLog;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dingzhiwei jmdhappy@126.com
 * @version V1.0
 * @Description: 业务通知MQ实现
 * @date 2017-07-05
 * @Copyright: www.xxpay.org
 */
@SuppressWarnings("Duplicates")
public abstract class Mq4PayNotify extends BaseService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired

    protected static final MyLog _log = MyLog.getLog(Mq4PayNotify.class);

    public abstract void send(String msg);

    /**
     * 发送延迟消息
     *
     * @param msg
     * @param delay
     */
    public abstract void send(String msg, long delay);

    public void receive(String msg) {
        _log.info("do notify task, msg={}", msg);
        JSONObject msgObj = JSON.parseObject(msg);
        String respUrl = msgObj.getString("url");
        String orderId = msgObj.getString("orderId");
        int count = msgObj.getInteger("count");
        _log.info("初始通知次数={}",count);
        int cnt=count+1;
        if (StringUtils.isEmpty(respUrl)) {

            _log.warn("notify url is empty. respUrl={}", respUrl);
            return;
        }
        try {
            String notifyResult = "";
            _log.info("==>MQ通知业务系统开始[orderId：{}][count：{}][time：{}]", orderId, cnt
                    , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            try {
                URI uri = new URI(respUrl);
                notifyResult = restTemplate.postForObject(uri, null, String.class);
            } catch (Exception e) {
                _log.error(e, "通知商户系统异常");
            }
            _log.info("<==MQ通知业务系统结束[orderId：{}][count：{}][time：{}]", orderId, cnt
                    , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            // 验证结果
            _log.info("notify response , OrderID={}", orderId);
            if (notifyResult.trim().equalsIgnoreCase("success")) {
                //_log.info("{} notify success, url:{}", _notifyInfo.getBusiId(), respUrl);
                //修改订单表
                try {
                    int result = super.baseUpdateStatus4Complete(orderId);
                    _log.info("修改payOrderId={},订单状态为处理完成->{}", orderId, result == 1 ? "成功" : "失败");
                } catch (Exception e) {
                    _log.error(e, "修改订单状态为处理完成异常");
                }
                // 修改通知次数
                try {
                    int result = super.baseUpdateMchNotifySuccess(orderId,"success", (byte) cnt);
                    _log.info("修改payOrderId={},通知业务系统次数->{}", orderId, result == 1 ? "成功" : "失败");
                } catch (Exception e) {
                    _log.error(e, "修改通知次数异常");
                }
                return; // 通知成功结束
            } else {
                // 通知失败，延时再通知
                count = cnt + 1;
                _log.info("notify count={}", count);
                // 修改通知次数
                try {
                    int result = super.baseUpdateMchNotifyFail(orderId,"file",(byte) count);
                    _log.info("修改payOrderId={},通知业务系统次数->{}", orderId, result == 1 ? "成功" : "失败");
                } catch (Exception e) {
                    _log.error(e, "修改通知次数异常");
                }

                if (count > 5) {
                    _log.info("notify count>5 stop. url={}", respUrl);
                    return;
                }
                msgObj.put("count", count);
                //修改通知业务系统的时间间隔
                //支付平台在收到第三方支付的支付成功通知后如向商户发起支付结果通知时，商户未返回确认消息，
                //则支付平台在第30秒、60秒、180秒、600秒、1800秒、3600秒发起通知
                // TODO 如第六次通知后商户后仍未返回确认消息则记录此消息并在后台进行提醒
                if(count < 3){
                    _log.info("notify count running. cnt={}", count);
                    this.send(msgObj.toJSONString(), count * 30 * 1000);
                }else if(count == 3){
                    _log.info("notify count running. cnt={}", count);
                    this.send(msgObj.toJSONString(), count * 60 * 1000);
                }else if(count == 4){
                    _log.info("notify count running. cnt={}", count);
                    this.send(msgObj.toJSONString(), count * 150 * 1000);
                }else if(count == 5){
                    _log.info("notify count running. cnt={}", count);
                    this.send(msgObj.toJSONString(), count * 360 * 1000);
                }else if (count ==6){
                    _log.info("notify count running. cnt={}", count);
                    this.send(msgObj.toJSONString(), count * 600 * 1000);
                }
            }
            _log.warn("notify failed. url:{}, response body:{}", respUrl, notifyResult.toString());
        } catch (Exception e) {
            _log.info("<==MQ通知业务系统结束[orderId：{}][count：{}][time：{}]", orderId, count
                    , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            _log.error(e, "notify exception. url:%s", respUrl);
            int result = super.baseUpdateMchNotifyFail(orderId,"file",(byte) count);
            _log.info("修改通知记录->{}",result);
            _log.info("修改payOrderId={},通知业务系统次数->{}", orderId, result == 1 ? "成功" : "失败");

        }

    }
}
