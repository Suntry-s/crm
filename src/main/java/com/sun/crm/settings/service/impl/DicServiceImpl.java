package com.sun.crm.settings.service.impl;

import com.sun.crm.settings.dao.DicTypeDao;
import com.sun.crm.settings.dao.DicValueDao;
import com.sun.crm.settings.domain.DicType;
import com.sun.crm.settings.domain.DicValue;
import com.sun.crm.settings.service.DicService;
import com.sun.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();
        //取字典类型列表
        List<DicType> dicTypeList = dicTypeDao.getTypeList();
        //将字典类型列表遍历
        for (DicType dt : dicTypeList){
            //取得每一种类型的字典类型编码
            String code = dt.getCode();
            //根据每一种字典类型取字典值列表
            List<DicValue> dicValueList =  dicValueDao.getListByCode(code);
            //将code和List<DicValue>放入map集合中
            map.put(code+"List",dicValueList);
        }

        return map;
    }
}
