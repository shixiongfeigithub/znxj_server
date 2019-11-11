package com.niule.znxj.web.dao;

import com.niule.znxj.web.model.Interfaceengine;
import com.niule.znxj.web.model.InterfaceengineExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface InterfaceengineMapper {
    int countByExample(InterfaceengineExample example);

    int deleteByExample(InterfaceengineExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Interfaceengine record);

    int insertSelective(Interfaceengine record);

    List<Interfaceengine> selectByExample(InterfaceengineExample example);

    List<Interfaceengine> selectAllListBySite(HashMap<String,Object> map);

    int countInterface(HashMap<String,Object> map);

    Interfaceengine selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Interfaceengine record, @Param("example") InterfaceengineExample example);

    int updateByExample(@Param("record") Interfaceengine record, @Param("example") InterfaceengineExample example);

    int updateByPrimaryKeySelective(Interfaceengine record);

    int updateByPrimaryKey(Interfaceengine record);

    int deleteByPrimaryKey1(Long id);
}