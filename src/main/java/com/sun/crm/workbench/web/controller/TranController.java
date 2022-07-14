package com.sun.crm.workbench.web.controller;


import com.sun.crm.settings.domain.User;
import com.sun.crm.settings.service.UserService;
import com.sun.crm.settings.service.impl.UserServiceImpl;
import com.sun.crm.utils.DateTimeUtil;
import com.sun.crm.utils.PrintJson;
import com.sun.crm.utils.ServiceFactory;
import com.sun.crm.utils.UUIDUtil;
import com.sun.crm.workbench.domain.Customer;
import com.sun.crm.workbench.domain.Tran;
import com.sun.crm.workbench.domain.TranHistory;
import com.sun.crm.workbench.service.CustomerService;
import com.sun.crm.workbench.service.TranService;
import com.sun.crm.workbench.service.impl.CustomerServiceImpl;
import com.sun.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("进入线索控制器");
        String path = request.getServletPath();

        //模板方法
        if ("/workbench/transaction/add.do".equals(path)) {
            add(request, response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        }else if ("/workbench/transaction/save.do".equals(path)) {
            save(request, response);
        }else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request, response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        }else if ("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request, response);
        }

    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得交易阶段数量统计表的数据");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        // 业务层反 total 和 dataList
        Map<String,Object> map =  ts.getCharts();
        PrintJson.printJsonObj(response,map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("改变交易阶段");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran t = new Tran();
        t.setId(id);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setStage(stage);
        t.setEditTime(editTime);
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.changeStage(t);
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("t",t);
        PrintJson.printJsonObj(response,map);



    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易id取得交易历史列表");
        String tranId = request.getParameter("tranId");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList =  ts.getHistoryListByTranId(tranId);
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        //遍历thList
        for(TranHistory th : thList){
            //取得阶段
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response,thList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页");
        String id = request.getParameter("id");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t =  ts.detail(id);

        String stage = t.getStage();
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);

        request.setAttribute("t",t);
        t.setPossibility(possibility);
        request.setAttribute("t",t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("添加交易的操作");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName"); //此处我们暂时只有客户名称，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(t,customerName);
        if (flag){
            //跳转到页表页
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得客户名称列表进行模糊查询");
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList =  cs.getCustomerName(name);
        PrintJson.printJsonObj(response,sList);


    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到添加交易信息页");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);


    }
}
