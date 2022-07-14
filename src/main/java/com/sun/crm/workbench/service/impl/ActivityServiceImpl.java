package com.sun.crm.workbench.service.impl;

import com.sun.crm.settings.dao.UserDao;
import com.sun.crm.settings.domain.User;
import com.sun.crm.utils.SqlSessionUtil;
import com.sun.crm.vo.PaginationVO;
import com.sun.crm.workbench.dao.ActivityDao;
import com.sun.crm.workbench.dao.ActivityRemarkDao;
import com.sun.crm.workbench.domain.Activity;
import com.sun.crm.workbench.domain.ActivityRemark;
import com.sun.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public boolean save(Activity a) {
        boolean flag = true;
        int count = activityDao.save(a);

        if(count!=1){
            //添加失败
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //查询要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);
        //删除备注,返回受影响的条数
        int count2 = activityRemarkDao.deleteByAids(ids);
        if(count1!=count2){
            flag = false;
        }
        //删除市场活动记录
        int count3 = activityDao.delete(ids);
        if(count3!= ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //去uList
        List<User> uList = userDao.getUserList();
        //取activity对象
        Activity a = activityDao.getById(id);
        //将uList和activity放入map集合中
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("a",a);

        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flag = true;
        int count = activityDao.update(a);

        if(count!=1){
            //添加失败
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity a = activityDao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteById(id);
        if (count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.saveRemark(ar);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemark(ar);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList = activityDao.getActivityListByClueId(clueId);

        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> aList =  activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> aList =  activityDao.getActivityListByName(aname);
        return aList;
    }
}
