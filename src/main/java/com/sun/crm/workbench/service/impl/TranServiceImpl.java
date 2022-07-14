package com.sun.crm.workbench.service.impl;

import com.sun.crm.utils.DateTimeUtil;
import com.sun.crm.utils.SqlSessionUtil;
import com.sun.crm.utils.UUIDUtil;
import com.sun.crm.workbench.dao.CustomerDao;
import com.sun.crm.workbench.dao.TranDao;
import com.sun.crm.workbench.dao.TranHistoryDao;
import com.sun.crm.workbench.domain.Customer;
import com.sun.crm.workbench.domain.Tran;
import com.sun.crm.workbench.domain.TranHistory;
import com.sun.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public boolean save(Tran t, String customerName) {
        boolean flag = true;
        Customer cus = customerDao.getCustomerByName(customerName);
        if (cus==null){
            //新建客户
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setOwner(t.getOwner());
            cus.setName(customerName);
            cus.setNextContactTime(t.getNextContactTime());
            cus.setCreateTime(t.getCreateTime());
            cus.setCreateBy(t.getCreateBy());
            cus.setContactSummary(t.getContactSummary());
            //添加客户
            int count1 = customerDao.save(cus);
            if (count1!=1){
                flag = false;
            }
        }
        //将客户id封装到t对象中
        t.setCustomerId(cus.getId());
        //添加交易
        int count2 = tranDao.save(t);
        if (count2!=1){
            flag = false;
        }
        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t = tranDao.detail(id);

        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList =  tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;
        int count =  tranDao.changeStage(t);
        if (count!=1){
            flag = false;
        }
        //改变交易成功后，增加一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(t.getEditTime());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());

        int count2 = tranHistoryDao.save(th);
        if (count2!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        //取得total
         int total = tranDao.getTotal();
        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();
        //将total和dataList封装到map集合中
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("dataList",dataList);


        return map;
    }
}
