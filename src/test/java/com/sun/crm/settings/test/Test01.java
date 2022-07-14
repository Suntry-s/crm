package com.sun.crm.settings.test;

import com.sun.crm.utils.DateTimeUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test01 {
    @Test //验证失效时间
    public void test01(){
        String expireTime = "2022-07-08 9:28:10";
        //当前系统时间
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);
    }
}
