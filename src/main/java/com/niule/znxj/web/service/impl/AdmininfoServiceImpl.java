package com.niule.znxj.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niule.znxj.web.dao.AdmininfoMapper;
import com.niule.znxj.web.model.Admininfo;
import com.niule.znxj.web.model.AdmininfoExample;
import com.niule.znxj.web.model.PositioninfoExample;
import com.niule.znxj.web.service.AdmininfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by administor on 2017/3/16.
 */
@Service
public class AdmininfoServiceImpl implements AdmininfoService{
    @Resource
    private AdmininfoMapper admininfoMapper;

   /* @Override
    public List<Admininfo> findByPage(int page, int pagesize) {
        return admininfoMapper.findByPage((page-1)*pagesize,pagesize);
    }*/

    @Override
    public int admincount(int roleid) {
        return admininfoMapper.admincount(roleid);
    }

    @Override
    public PageInfo<Admininfo> selectByExample(int page, int size) {
        PageHelper.startPage(page,size);
        AdmininfoExample example=new AdmininfoExample();
        return new PageInfo<>(admininfoMapper.selectByExample(example));
    }

    @Override
    public Admininfo getexistuname(String username) {
        return admininfoMapper.getexistuname(username);
    }

    /* @Override
            public int count() {

                return admininfoMapper.count();
    }*/
    public int addAdmininfo(Admininfo admininfo){
        return admininfoMapper.insert(admininfo);
    }
    public  Admininfo login(Admininfo admininfo){
        Admininfo admininfo1 = admininfoMapper.login(admininfo);
        return admininfo1;
    }
    public  int insert(Admininfo record){
        return admininfoMapper.insert(record);
    }
    public int deleteByPrimaryKey(Long id){
        return admininfoMapper.deleteByPrimaryKey(id);
    }
    public Admininfo selectByPrimaryKey(Long id){
        return admininfoMapper.selectByPrimaryKey(id);
    }
    public int updateByPrimaryKeySelective(Admininfo record){
        return admininfoMapper.updateByPrimaryKeySelective(record);
    }
}
