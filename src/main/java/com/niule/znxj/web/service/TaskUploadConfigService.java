package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Taskuploadconfig;
import com.niule.znxj.web.model.TaskuploadconfigExample;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xuqb
 * Date: 2019-11-12 12:31
 */
public interface TaskUploadConfigService {

    int deleteByExample(TaskuploadconfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Taskuploadconfig record);

    int insertSelective(Taskuploadconfig record);

    int updateByPrimaryKeySelective(Taskuploadconfig record);

    int updateByPrimaryKey(Taskuploadconfig record);

    List<Taskuploadconfig> selectByExample(TaskuploadconfigExample example);

    Taskuploadconfig selectByPrimaryKey(Long id);

    List<Taskuploadconfig> selectByPageParam(HashMap<String,Object> map);

    int countByPageParam(HashMap<String,Object> map);
}
