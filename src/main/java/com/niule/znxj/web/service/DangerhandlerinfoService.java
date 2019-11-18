package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Dangerhandlerinfo;
import com.niule.znxj.web.model.DangerhandlerinfoExample;
import com.niule.znxj.web.model.Exceptionhandlerinfo;
import com.niule.znxj.web.model.ExceptionhandlerinfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DangerhandlerinfoService {
    int countByExample(DangerhandlerinfoExample example);

    int deleteByExample(DangerhandlerinfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Dangerhandlerinfo record);

    int insertSelective(Dangerhandlerinfo record);

    List<Dangerhandlerinfo> selectByExample(DangerhandlerinfoExample example);

    Dangerhandlerinfo selectByPrimaryKey(Long id);

    Dangerhandlerinfo selectInfoByReportId(Long id);

    int updateByExampleSelective(@Param("record") Dangerhandlerinfo record, @Param("example") DangerhandlerinfoExample example);

    int updateByExample(@Param("record") Dangerhandlerinfo record, @Param("example") DangerhandlerinfoExample example);

    int updateByPrimaryKeySelective(Dangerhandlerinfo record);

    int updateByPrimaryKey(Dangerhandlerinfo record);

}
