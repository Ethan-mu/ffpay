package org.xxpay.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.boot.service.BaseService;
import org.xxpay.boot.service.IPayLogService;
import org.xxpay.common.domain.BaseParam;
import org.xxpay.common.enumm.RetEnum;
import org.xxpay.common.util.*;
import org.xxpay.dal.dao.mapper.PayLogMapper;
import org.xxpay.dal.dao.mapper.PayOrderMapper;
import org.xxpay.dal.dao.model.PayLog;
import org.xxpay.dal.dao.model.PayLogExample;
import org.xxpay.dal.dao.model.PayOrder;
import org.xxpay.dal.dao.model.PayOrderExample;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayLogServiceImpl  extends BaseService implements IPayLogService{
    private static final MyLog _log = MyLog.getLog(PayLogServiceImpl.class);


    @Override
    public int createPayLog(JSONObject params) {
        String mchId = params.getString("mchId"); 			    // 商户ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户订单号
        String channelId = params.getString("channelId"); 	    // 渠道ID
        Long amount = Long.valueOf(params.getString("amount")); 		    // 支付金额（单位分）
        String currency = params.getString("currency");         // 币种
        String clientIp = params.getString("clientIp");	        // 客户端IP
        String device = params.getString("device"); 	        // 设备
        String extra = params.getString("extra");		        // 特定渠道发起时额外参数
        String param1 = params.getString("param1"); 		    // 扩展参数1
        String param2 = params.getString("param2"); 		    // 扩展参数2
        String notifyUrl = params.getString("notifyUrl"); 		// 支付结果回调URL
        String sign = params.getString("sign"); 				// 签名
        String subject = params.getString("subject");	        // 商品主题
        String body = params.getString("body");

        PayLog payLog=new PayLog();
        payLog.setOutTradeNo(mchOrderNo);
        payLog.setRequestTime(new Date());
        payLog.setRequestUrl(notifyUrl);
        payLog.setTotalFee(amount);
        payLog.setUserId(mchId);
        payLog.setTradeState("0");
        payLog.setPayType(channelId);
        payLog.setOrderList(mchOrderNo);
        payLog.setRequestParam(params.toString());
        _log.info("创建日志记录--统一下单");
       return super.insert(payLog);
    }


    //更新创建时间
    @Override
    public int updatePayLog(Map param) {

        String mchOrderNo= (String) param.get("mchOrderNo");


        PayLog payLog = new PayLog();
        payLog.setOutTradeNo(mchOrderNo);
        payLog.setCreateTime(new Date());
        _log.info("更新日志--创建订单時間");
        return super.updateLog(payLog);


    }

    @Override
    public int updatePayLog(String Result,Map param,Integer channelId) {
        String ResponseParam=null;
        if(channelId==1){
            Map retMap = JSON.parseObject(Result);
            String aliResult= (String) retMap.get("payUrl");
            ResponseParam=aliResult;
        }else {
            ResponseParam=Result;
        }

        String mchOrderNo= (String) param.get("mchOrderNo");
        JSONObject jsonWxResult = JSONObject.parseObject(Result);
        String payOrderId= (String) jsonWxResult.get("payOrderId");
        PayLog payLog = new PayLog();
        payLog.setOutTradeNo(mchOrderNo);
        payLog.setReponseTime(new Date());
        payLog.setResponseParam(ResponseParam);
        payLog.setTransactionId(payOrderId);
        _log.info("更新日志----统一下单返回数据");
        return super.updateLog(payLog);

    }

    @Override
    public int updatePayLog(String payOrderId,Byte status) {
        PayLog payLog = new PayLog();
        payLog.setTransactionId(payOrderId);
        payLog.setPayTime(new Date());
        payLog.setTradeState(status+"");
        System.out.println(payLog.toString());
        _log.info("更新日志记录--异步通知返回结果");
        return super.updateLogByTransactionId(payLog);
    }


}
