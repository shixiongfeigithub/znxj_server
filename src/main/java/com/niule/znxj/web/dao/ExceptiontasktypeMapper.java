package com.niule.znxj.web.dao;

import com.niule.znxj.web.model.Exceptiontasktype;
import com.niule.znxj.web.model.ExceptiontasktypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ExceptiontasktypeMapper {
    int countByExample(ExceptiontasktypeExample example);

    int deleteByExample(ExceptiontasktypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Exceptiontasktype record);

    int insertSelective(Exceptiontasktype record);

    List<Exceptiontasktype> selectByExample(ExceptiontasktypeExample example);

    Exceptiontasktype selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Exceptiontasktype record, @Param("example") ExceptiontasktypeExample example);

    int updateByExample(@Param("record") Exceptiontasktype record, @Param("example") ExceptiontasktypeExample example);

    int updateByPrimaryKeySelective(Exceptiontasktype record);

    int updateByPrimaryKey(Exceptiontasktype record);
}