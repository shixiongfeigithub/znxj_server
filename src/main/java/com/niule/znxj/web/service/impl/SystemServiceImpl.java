package com.niule.znxj.web.service.impl;

import com.niule.znxj.web.dao.SystemsettinginfoMapper;
import com.niule.znxj.web.model.Systemsettinginfo;
import com.niule.znxj.web.model.SystemsettinginfoExample;
import com.niule.znxj.web.service.SystemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by administor on 2017/5/16.
 */
@Service
public class SystemServiceImpl implements SystemService{
    @Resource
    private SystemsettinginfoMapper systemsettinginfoMapper;

    @Override
    public int updSysByKey(String key, String value) {
        return systemsettinginfoMapper.updSysByKey(key,value);
    }

    @Override
    public List<Systemsettinginfo> selectByExample() {
        SystemsettinginfoExample example=new SystemsettinginfoExample();
        return systemsettinginfoMapper.selectByExample(example);
    }

    @Override
    public Systemsettinginfo findByKey(String key) {
        SystemsettinginfoExample example=new SystemsettinginfoExample();
        example.createCriteria().andKeynameEqualTo(key);
        return systemsettinginfoMapper.selectByExample(example).get(0);
    }
}
