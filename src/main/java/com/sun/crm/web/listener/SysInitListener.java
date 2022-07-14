package com.sun.crm.web.listener;

import com.sun.crm.settings.domain.DicValue;
import com.sun.crm.settings.service.DicService;
import com.sun.crm.settings.service.impl.DicServiceImpl;
import com.sun.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器缓存处理数据字典");
        ServletContext application = event.getServletContext();
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        //取数据字典
        //调用业务层方法，返回一个map
        Map<String, List<DicValue>> map = ds.getAll();
        //将map解析为上下文域对象中的键值对
        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束");

        //解析Stage2Possibility.properties文件
        Map<String,String> pMap = new HashMap<String, String>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = rb.getKeys();
        while (keys.hasMoreElements()){
            //阶段
            String key = keys.nextElement();
            //可能性
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        //将pMap存入服务器缓存中
        application.setAttribute("pMap",pMap);

    }
}
