package com.niule.znxj.web.service.impl;

import com.niule.znxj.web.dao.TaskuploadconfigMapper;
import com.niule.znxj.web.model.Taskuploadconfig;
import com.niule.znxj.web.model.TaskuploadconfigExample;
import com.niule.znxj.web.service.TaskUploadConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuqb
 * Date: 2019-11-12 12:33
 */
@Service
public class TaskUploadConfigServiceImpl implements TaskUploadConfigService {

    @Resource
    private TaskuploadconfigMapper taskuploadconfigMapper;


    @Override
    public int deleteByExample(TaskuploadconfigExample example) {
        return taskuploadconfigMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return taskuploadconfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Taskuploadconfig record) {
        return taskuploadconfigMapper.insert(record);
    }

    @Override
    public int insertSelective(Taskuploadconfig record) {
        return taskuploadconfigMapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(Taskuploadconfig record) {
        return taskuploadconfigMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Taskuploadconfig record) {
        return taskuploadconfigMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Taskuploadconfig> selectByExample(TaskuploadconfigExample example) {
        return taskuploadconfigMapper.selectByExample(example);
    }

    @Override
    public Taskuploadconfig selectByPrimaryKey(Long id) {
        return taskuploadconfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Taskuploadconfig> selectByPageParam(HashMap<String, Object> map) {
        return taskuploadconfigMapper.selectByPageParam(map);
    }

    @Override
    public int countByPageParam(HashMap<String, Object> map) {
        return taskuploadconfigMapper.countByPageParam(map);
    }
}
