package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Knowledge;
import com.niule.znxj.web.model.Taskcloseinfo;
import com.niule.znxj.web.model.TaskcloseinfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface TaskCloseService {
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
