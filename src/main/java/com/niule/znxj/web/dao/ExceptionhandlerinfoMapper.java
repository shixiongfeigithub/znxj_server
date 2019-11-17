package com.niule.znxj.web.dao;

import com.niule.znxj.web.model.Exceptionhandlerinfo;
import com.niule.znxj.web.model.ExceptionhandlerinfoExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExceptionhandlerinfoMapper {
    int countByExample(ExceptionhandlerinfoExample example);

    int deleteByExample(ExceptionhandlerinfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Exceptionhandlerinfo record);

    int insertSelective(Exceptionhandlerinfo record);

    List<Exceptionhandlerinfo> selectByExample(ExceptionhandlerinfoExample example);

    List<Exceptionhandlerinfo> selectByReportId(Long id);

    Exceptionhandlerinfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Exceptionhandlerinfo record, @Param("example") ExceptionhandlerinfoExample example);

    int updateByExample(@Param("record") Exceptionhandlerinfo record, @Param("example") ExceptionhandlerinfoExample example);

    int updateByPrimaryKeySelective(Exceptionhandlerinfo record);

    int updateByPrimaryKey(Exceptionhandlerinfo record);

    int updateByReportId(HashMap<String,Object> map);
    Exceptionhandlerinfo selectInfoById(Long id);
}