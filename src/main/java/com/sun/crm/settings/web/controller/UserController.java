package com.sun.crm.settings.web.controller;

import com.sun.crm.settings.domain.User;
import com.sun.crm.settings.service.UserService;
import com.sun.crm.settings.service.impl.UserServiceImpl;
import com.sun.crm.utils.MD5Util;
import com.sun.crm.utils.PrintJson;
import com.sun.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入用户视图器");
        String path = request.getServletPath();

        //模板方法
        if ("/settings/user/login.do".equals(path)) {
            Login(request,response);
        }else if ("/settings/user/xxx.do".equals(path)){

        }

    }

    //登录方法
    private void Login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        System.out.println("进入到验证登录操作");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        //将密码的明文形式转为md5密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        //接收浏览器的IP地址
        String ip = request.getRemoteAddr();
        System.out.println("-------ip"+ip);
        //业务接口指向代理对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            User user = us.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);
            //登录成功
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            //登录失败,抛出异常，打印错误消息
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);


        }

    }
}
