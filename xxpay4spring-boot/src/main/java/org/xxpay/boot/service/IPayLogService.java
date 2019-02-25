package org.xxpay.boot.service;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: lizhaozhong
 * @date: 17/9/8
 * @description:
 */
public interface IPayLogService {

    //创建日志，插入数据
    public int createPayLog(JSONObject jsonParam);
    //更新创建订单时间
    public int updatePayLog(Map param);
    //更新统一下单返回结果
    public int updatePayLog(String Result,Map param,Integer channelId);
    //异步通知结果
    public int updatePayLog(String payOrderId,Byte status);



}
