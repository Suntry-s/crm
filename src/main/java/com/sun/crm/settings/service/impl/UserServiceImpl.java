package com.sun.crm.settings.service.impl;

import com.sun.crm.exception.LoginException;
import com.sun.crm.settings.dao.UserDao;
import com.sun.crm.settings.domain.User;
import com.sun.crm.settings.service.UserService;
import com.sun.crm.utils.DateTimeUtil;
import com.sun.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);
        if (user==null) {
            throw new LoginException("账号密码错误");
        }
        //账号密码正确
        //验证失效时间
        String expireTime = user.getExpireTime();
        String sysTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(sysTime)<0) {
            //账号失效
            throw new LoginException("账号已失效");
        }
        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw new LoginException("账号已锁定");
        }
        //判断ip地址
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)) {
            throw new LoginException("ip受限");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {
         List<User> uList = userDao.getUserList();
        return uList;
    }
}
