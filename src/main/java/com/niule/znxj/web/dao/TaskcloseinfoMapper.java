package com.niule.znxj.web.dao;

import com.niule.znxj.web.model.Taskcloseinfo;
import com.niule.znxj.web.model.TaskcloseinfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TaskcloseinfoMapper {
    int countByExample(TaskcloseinfoExample example);

    int deleteByExample(TaskcloseinfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Taskcloseinfo record);

    int insertSelective(Taskcloseinfo record);

    List<Taskcloseinfo> selectByExample(TaskcloseinfoExample example);

    Taskcloseinfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Taskcloseinfo record, @Param("example") TaskcloseinfoExample example);

    int updateByExample(@Param("record") Taskcloseinfo record, @Param("example") TaskcloseinfoExample example);

    int updateByPrimaryKeySelective(Taskcloseinfo record);

    int updateByPrimaryKey(Taskcloseinfo record);
}