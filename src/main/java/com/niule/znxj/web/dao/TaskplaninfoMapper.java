package com.niule.znxj.web.dao;

import com.niule.znxj.web.model.Taskplaninfo;
import com.niule.znxj.web.model.TaskplaninfoExample;

import java.util.HashMap;
import java.util.List;

import com.niule.znxj.web.model.taskresponse.TaskTempRes;
import org.apache.ibatis.annotations.Param;

public interface TaskplaninfoMapper {
    int countByExample(TaskplaninfoExample example);

    int deleteByExample(TaskplaninfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Taskplaninfo record);

    int insertSelective(Taskplaninfo record);

    List<Taskplaninfo> selectByExampleWithBLOBs(TaskplaninfoExample example);

    List<Taskplaninfo> selectByExample(TaskplaninfoExample example);

    Taskplaninfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Taskplaninfo record, @Param("example") TaskplaninfoExample example);

    int updateByExampleWithBLOBs(@Param("record") Taskplaninfo record, @Param("example") TaskplaninfoExample example);

    int updateByExample(@Param("record") Taskplaninfo record, @Param("example") TaskplaninfoExample example);

    int updateByPrimaryKeySelective(Taskplaninfo record);

    int updateByPrimaryKeyWithBLOBs(Taskplaninfo record);

    int updateByPrimaryKey(Taskplaninfo record);



    public List<Taskplaninfo> findByPageTask(HashMap<String,Object> map);
    public int countTask(HashMap<String,Object> map);

    public List<Taskplaninfo> findByPageTask2(HashMap<String,Object> map);
    public int countTask2(HashMap<String,Object> map);

    List<TaskTempRes> getTakTemps(Long classId, Integer type , Integer state);

    List<TaskTempRes> getTakTempsExecuting(Long classId, Integer type , Integer state , Long userId);

    int updatestate(Long id);
}