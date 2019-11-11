package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Interfaceengine;
import com.niule.znxj.web.model.InterfaceengineExample;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * Created by administor on 2017/3/21.
 */
public interface InterfaceService {
    int countByExample(InterfaceengineExample example);

    int deleteByExample(InterfaceengineExample example);

    int deleteByPrimaryKey(Long id);

    int deleteByPrimaryKey1(Long id);

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
}
