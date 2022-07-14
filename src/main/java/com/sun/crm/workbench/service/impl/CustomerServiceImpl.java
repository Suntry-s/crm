package com.sun.crm.workbench.service.impl;

import com.sun.crm.utils.SqlSessionUtil;
import com.sun.crm.workbench.dao.CustomerDao;
import com.sun.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public List<String> getCustomerName(String name) {
        List<String> sList =  customerDao.getCustomerName(name);

        return sList;
    }
}
