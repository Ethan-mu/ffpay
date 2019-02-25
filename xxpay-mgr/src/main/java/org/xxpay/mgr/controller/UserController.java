package org.xxpay.mgr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.xxpay.common.util.ServerResponse;
import org.xxpay.dal.dao.model.User;
import org.xxpay.mgr.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login( String name,String password){
        ServerResponse<User> response = userService.login(name,password);

        return response;
    }

    @RequestMapping(value="register.do",method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> register(User user){

        return userService.register(user);
    }

}
