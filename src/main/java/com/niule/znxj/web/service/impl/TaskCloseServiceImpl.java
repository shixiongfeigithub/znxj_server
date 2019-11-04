package com.niule.znxj.web.service.impl;

import com.niule.znxj.web.dao.TaskcloseinfoMapper;
import com.niule.znxj.web.model.Taskcloseinfo;
import com.niule.znxj.web.model.TaskcloseinfoExample;
import com.niule.znxj.web.service.TaskCloseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuqb
 * Date: 2019-11-04 15:40
 */
@Service
public class TaskCloseServiceImpl implements TaskCloseService {

    @Resource
    private TaskcloseinfoMapper taskcloseinfoMapper;

    @Override
    public int countByExample(TaskcloseinfoExample example) {
        return taskcloseinfoMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(TaskcloseinfoExample example) {
        return taskcloseinfoMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return taskcloseinfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Taskcloseinfo record) {
        return taskcloseinfoMapper.insert(record);
    }

    @Override
    public int insertSelective(Taskcloseinfo record) {
        return taskcloseinfoMapper.insertSelective(record);
    }

    @Override
    public List<Taskcloseinfo> selectByExample(TaskcloseinfoExample example) {
        return taskcloseinfoMapper.selectByExample(example);
    }

    @Override
    public Taskcloseinfo selectByPrimaryKey(Integer id) {
        return taskcloseinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Taskcloseinfo record, TaskcloseinfoExample example) {
        return taskcloseinfoMapper.updateByExampleSelective(record,example);
    }

    @Override
    public int updateByExample(Taskcloseinfo record, TaskcloseinfoExample example) {
        return taskcloseinfoMapper.updateByExample(record,example);
    }

    @Override
    public int updateByPrimaryKeySelective(Taskcloseinfo record) {
        return taskcloseinfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Taskcloseinfo record) {
        return taskcloseinfoMapper.updateByPrimaryKey(record);
    }
}
