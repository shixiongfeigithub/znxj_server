package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Siteareainfo;
import com.niule.znxj.web.model.SiteareainfoExample;

import java.util.HashMap;
import java.util.List;

/**
 * Created by administor on 2017/3/21.
 */
public interface SiteService {
    List<Siteareainfo> queryAllSite();
    public List<Siteareainfo> findByPageSite(HashMap<String,Object> map);
//    public int countSite();
    int deleteByPrimaryKey(Long id);

    int insert(Siteareainfo record);
    Siteareainfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Siteareainfo record);
    List<Siteareainfo> selectByExample();
    List<Siteareainfo> selectByExample2(Integer siteid);
    List<Siteareainfo> queryAllSite2(int page,int size);
}
