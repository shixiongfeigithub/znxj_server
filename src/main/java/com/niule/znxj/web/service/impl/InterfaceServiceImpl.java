package com.niule.znxj.web.service.impl;

import com.niule.znxj.web.dao.*;
import com.niule.znxj.web.model.Interfaceengine;
import com.niule.znxj.web.model.InterfaceengineExample;
import com.niule.znxj.web.service.InterfaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by administor on 2017/3/21.
 */
@Service
public class InterfaceServiceImpl implements InterfaceService {
    @Resource
    private InterfaceengineMapper interfaceengineMapper;


    @Override
    public int countByExample(InterfaceengineExample example) {
        return interfaceengineMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(InterfaceengineExample example) {
        return interfaceengineMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return interfaceengineMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByPrimaryKey1(Long id) {
        return interfaceengineMapper.deleteByPrimaryKey1(id);
    }

    @Override
    public int insert(Interfaceengine record) {
        return interfaceengineMapper.insert(record);
    }

    @Override
    public int insertSelective(Interfaceengine record) {
        return interfaceengineMapper.insertSelective(record);
    }

    @Override
    public List<Interfaceengine> selectByExample(InterfaceengineExample example) {
        return interfaceengineMapper.selectByExample(example);
    }
    @Override
    public int countInterface(HashMap<String,Object> map){
        return interfaceengineMapper.countInterface(map);
    }

    @Override
    public List<Interfaceengine> selectAllListBySite(HashMap<String,Object> map){
        return interfaceengineMapper.selectAllListBySite(map);
    }

    @Override
    public Interfaceengine selectByPrimaryKey(Long id) {
        return interfaceengineMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Interfaceengine record, InterfaceengineExample example) {
        return interfaceengineMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(Interfaceengine record, InterfaceengineExample example) {
        return interfaceengineMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(Interfaceengine record) {
        return interfaceengineMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Interfaceengine record) {
        return interfaceengineMapper.updateByPrimaryKey(record);
    }
}

