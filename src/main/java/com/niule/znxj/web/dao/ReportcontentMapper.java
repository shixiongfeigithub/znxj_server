package com.niule.znxj.web.dao;

import com.niule.znxj.web.model.Reportcontent;
import com.niule.znxj.web.model.ReportcontentExample;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ReportcontentMapper {
    int countByExample(ReportcontentExample example);

    int deleteByExample(ReportcontentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Reportcontent record);

    int insertSelective(Reportcontent record);

    List<Reportcontent> selectByExample(ReportcontentExample example);

    Reportcontent selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Reportcontent record, @Param("example") ReportcontentExample example);

    int updateByExample(@Param("record") Reportcontent record, @Param("example") ReportcontentExample example);

    int updateByPrimaryKeySelective(Reportcontent record);

    int updateByPrimaryKey(Reportcontent record);

//    List<Reportcontent> dadiyreportlist(String operationtime);
}