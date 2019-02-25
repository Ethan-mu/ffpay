package org.xxpay.mgr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xxpay.common.util.MD5Util;
import org.xxpay.common.util.ServerResponse;
import org.xxpay.dal.dao.mapper.UserMapper;
import org.xxpay.dal.dao.model.User;


import java.util.Date;
import java.util.UUID;

/**
 * Created by geely
 */
@Component
public class UserService  {

    @Autowired
    private UserMapper userMapper;


    public ServerResponse<User> login(String name, String password) {
        int resultCount = userMapper.checkUsername(name);
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(name,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }



    public ServerResponse<String> register(User user){

        String userId= System.currentTimeMillis()+"";
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        user.setStatus(user.getStatus());
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        user.setUserid(userId);
        user.setPhonenumber(user.getPhonenumber());
        user.setCreatetime(new Date());
        user.setLastlogintime(new Date());
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createBySuccessMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }



}
