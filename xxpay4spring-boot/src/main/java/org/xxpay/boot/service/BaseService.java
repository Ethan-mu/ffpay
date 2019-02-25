package org.xxpay.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xxpay.common.constant.PayConstant;
import org.xxpay.dal.dao.mapper.*;
import org.xxpay.dal.dao.model.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.xxpay.common.constant.PayConstant.*;

/**
 * @author: dingzhiwei
 * @date: 17/9/9
 * @description:
 */
@Service
public class BaseService {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private MchInfoMapper mchInfoMapper;

    @Autowired
    private PayChannelMapper payChannelMapper;

    @Autowired
    private PayLogMapper payLogMapper;
    @Autowired
    private MchNotifyMapper mchNotifyMapper;

    public MchInfo baseSelectMchInfo(String mchId) {
        return mchInfoMapper.selectByPrimaryKey(mchId);
    }

    public PayChannel baseSelectPayChannel(String mchId, String channelId) {
        PayChannelExample example = new PayChannelExample();
        PayChannelExample.Criteria criteria = example.createCriteria();
        criteria.andChannelIdEqualTo(channelId);
        criteria.andMchIdEqualTo(mchId);
        List<PayChannel> payChannelList = payChannelMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(payChannelList)) return null;
        return payChannelList.get(0);
    }

    public int baseCreatePayOrder(PayOrder payOrder) {
        return payOrderMapper.insertSelective(payOrder);
    }

    public PayOrder baseSelectPayOrder(String payOrderId) {
        return payOrderMapper.selectByPrimaryKey(payOrderId);
    }

    public PayOrder baseSelectPayOrderByMchIdAndPayOrderId(String mchId, String payOrderId) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andPayOrderIdEqualTo(payOrderId);
        List<PayOrder> payOrderList = payOrderMapper.selectByExample(example);
        return CollectionUtils.isEmpty(payOrderList) ? null : payOrderList.get(0);
    }

    public PayOrder baseSelectPayOrderByMchIdAndMchOrderNo(String mchId, String mchOrderNo) {
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andMchIdEqualTo(mchId);
        criteria.andMchOrderNoEqualTo(mchOrderNo);
        List<PayOrder> payOrderList = payOrderMapper.selectByExample(example);
        return CollectionUtils.isEmpty(payOrderList) ? null : payOrderList.get(0);
    }

    public int baseUpdateStatus4Ing(String payOrderId, String channelOrderNo) {
        PayOrder payOrder = new PayOrder();
        payOrder.setStatus(PayConstant.PAY_STATUS_PAYING);
        if(channelOrderNo != null) payOrder.setChannelOrderNo(channelOrderNo);
        payOrder.setPaySuccTime(System.currentTimeMillis());
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_INIT);
        return payOrderMapper.updateByExampleSelective(payOrder, example);
    }

    public int baseUpdateStatus4Success(String payOrderId, String channelOrderNo) {
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(payOrderId);
        payOrder.setStatus(PAY_STATUS_SUCCESS);
        if(channelOrderNo != null) payOrder.setChannelOrderNo(channelOrderNo);
        payOrder.setPaySuccTime(System.currentTimeMillis());
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PayConstant.PAY_STATUS_PAYING);
        return payOrderMapper.updateByExampleSelective(payOrder, example);
    }

    public int baseUpdateStatus4Complete(String payOrderId) {
        PayOrder payOrder = new PayOrder();
        payOrder.setPayOrderId(payOrderId);
        payOrder.setStatus(PAY_STATUS_COMPLETE);
        PayOrderExample example = new PayOrderExample();
        PayOrderExample.Criteria criteria = example.createCriteria();
        criteria.andPayOrderIdEqualTo(payOrderId);
        criteria.andStatusEqualTo(PAY_STATUS_SUCCESS);
        return payOrderMapper.updateByExampleSelective(payOrder, example);
    }

    public int baseUpdateNotify(String payOrderId, byte count) {
        MchNotify mchNotify=new MchNotify();
        mchNotify.setNotifyCount(count);
        mchNotify.setLastNotifyTime(new Date());
        mchNotify.setOrderId(payOrderId);
        mchNotify.setStatus(MCH_NOTIFY_STATUS_SUCCESS);
        mchNotify.setUpdateTime(new Date());
        mchNotify.setResult("success");
        return  mchNotifyMapper.updateByPrimaryKeySelective(mchNotify);
    }
    public int baseUpdateNotify(String payOrderId, byte count,String status) {
        MchNotify mchNotify=new MchNotify();
        mchNotify.setNotifyCount(count);
        mchNotify.setLastNotifyTime(new Date());
        mchNotify.setOrderId(payOrderId);
        mchNotify.setStatus(MCH_NOTIFY_STATUS_FAIL);
        return  mchNotifyMapper.updateByPrimaryKeySelective(mchNotify);
}
    public int baseUpdateNotify(PayOrder payOrder) {
        return payOrderMapper.updateByPrimaryKeySelective(payOrder);
    }

    //校验业务系统的订单号是否重复
    public int baseValidateMchOrderNo(String mchOrderNo) {
        return payOrderMapper.validateMchOrderNo(mchOrderNo);
    }

    /**
     * 日志记录处理
     * @param payLog
     * @return
     */

    public int insert(PayLog payLog) {
       return  payLogMapper.insertSelective(payLog);
    }

    public int updateLog(PayLog payLog){

        return payLogMapper.updateByPrimaryKeySelective(payLog);
    }
    public int updateLogByTransactionId(PayLog payLog){

        return payLogMapper.updateLogByTransactionId(payLog);
    }
    /**商户通知
     *
     */
    public int baseInsertMchNotify(String orderId, String mchId, String mchOrderNo, String orderType, String notifyUrl,Byte status,Byte count) {
        MchNotify mchNotify = new MchNotify();
        mchNotify.setOrderId(orderId);
        mchNotify.setMchId(mchId);
        mchNotify.setMchOrderNo(mchOrderNo);
        mchNotify.setOrderType(orderType);
        mchNotify.setNotifyUrl(notifyUrl);
        mchNotify.setStatus(status);
        mchNotify.setNotifyCount(count);
        mchNotify.setCreateTime(new Date());
//        return mchNotifyMapper.insertSelectiveOnDuplicateKeyUpdate(mchNotify);
        return mchNotifyMapper.insertSelective(mchNotify);
    }
    public MchNotify baseSelectMchNotify(String orderId) {
        return mchNotifyMapper.selectByPrimaryKey(orderId);
    }
    public int baseUpdateMchNotifySuccess(String orderId, String result, byte notifyCount) {
        MchNotify mchNotify = new MchNotify();
        mchNotify.setStatus(PayConstant.MCH_NOTIFY_STATUS_SUCCESS);
        mchNotify.setResult(result);
        mchNotify.setNotifyCount(notifyCount);
        mchNotify.setLastNotifyTime(new Date());
        MchNotifyExample example = new MchNotifyExample();
        MchNotifyExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List values = new LinkedList<>();
        values.add(PayConstant.MCH_NOTIFY_STATUS_NOTIFYING);
        values.add(PayConstant.MCH_NOTIFY_STATUS_FAIL);
        criteria.andStatusIn(values);
        return mchNotifyMapper.updateByExampleSelective(mchNotify, example);
    }

    public int baseUpdateMchNotifyFail(String orderId, String result, byte notifyCount) {
        MchNotify mchNotify = new MchNotify();
        mchNotify.setStatus(PayConstant.MCH_NOTIFY_STATUS_FAIL);
        mchNotify.setResult(result);
        mchNotify.setNotifyCount(notifyCount);
        mchNotify.setLastNotifyTime(new Date());
        MchNotifyExample example = new MchNotifyExample();
        MchNotifyExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List values = new LinkedList<>();
        values.add(PayConstant.MCH_NOTIFY_STATUS_NOTIFYING);
        values.add(PayConstant.MCH_NOTIFY_STATUS_FAIL);
        return mchNotifyMapper.updateByExampleSelective(mchNotify, example);
    }
}
