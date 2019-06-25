package com.niule.znxj.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.niule.znxj.web.dao.SiteareainfoMapper;
import com.niule.znxj.web.model.Siteareainfo;
import com.niule.znxj.web.model.SiteareainfoExample;
import com.niule.znxj.web.service.SiteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by administor on 2017/3/21.
 */
@Service
public class SiteServiceImpl implements SiteService{
    @Resource
    private SiteareainfoMapper siteareainfoMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return siteareainfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Siteareainfo record) {
        return siteareainfoMapper.insert(record);
    }

    @Override
    public Siteareainfo selectByPrimaryKey(Long id) {
        return siteareainfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Siteareainfo record) {
        return siteareainfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Siteareainfo> selectByExample() {
        SiteareainfoExample siteareainfoExample=new SiteareainfoExample();
        return siteareainfoMapper.selectByExample(siteareainfoExample);
    }

    @Override
    public List<Siteareainfo> selectByExample2(Integer siteid) {
        SiteareainfoExample example=new SiteareainfoExample();
        if(siteid!=null){
            example.createCriteria().andIdEqualTo(Long.valueOf(siteid));
        }
        return siteareainfoMapper.selectByExample(example);
    }

    @Override
    public List<Siteareainfo> findByPageSite(HashMap<String,Object> map) {
        int page=Integer.parseInt(map.get("page").toString());
        int size=Integer.parseInt(map.get("size").toString());
        PageHelper.startPage(page,size);
        return siteareainfoMapper.findByPageSite(map);
    }

    @Override
    public List<Siteareainfo> queryAllSite() {
        SiteareainfoExample example = new SiteareainfoExample();
        return siteareainfoMapper.selectByExample(example);
    }
    @Override
    public List<Siteareainfo> queryAllSite2(int page,int size) {
        PageHelper.startPage(page,size);
        SiteareainfoExample example = new SiteareainfoExample();
        return siteareainfoMapper.selectByExample(example);
    }
}
