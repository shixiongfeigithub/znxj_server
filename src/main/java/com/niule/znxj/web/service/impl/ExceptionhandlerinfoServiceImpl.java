package com.niule.znxj.web.service.impl;

import com.niule.znxj.web.dao.ExceptionhandlerinfoMapper;
import com.niule.znxj.web.model.Exceptionhandlerinfo;
import com.niule.znxj.web.model.ExceptionhandlerinfoExample;
import com.niule.znxj.web.service.ExceptionhandlerinfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuqb
 * Date: 2019-11-04 15:40
 */
@Service
public class ExceptionhandlerinfoServiceImpl implements ExceptionhandlerinfoService {

    @Resource
    private ExceptionhandlerinfoMapper exceptionhandlerinfoMapper;

    @Override
    public int countByExample(ExceptionhandlerinfoExample example) {
        return exceptionhandlerinfoMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(ExceptionhandlerinfoExample example) {
        return exceptionhandlerinfoMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return exceptionhandlerinfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Exceptionhandlerinfo record) {
        return exceptionhandlerinfoMapper.insert(record);
    }

    @Override
    public int insertSelective(Exceptionhandlerinfo record) {
        return exceptionhandlerinfoMapper.insertSelective(record);
    }

    @Override
    public List<Exceptionhandlerinfo> selectByExample(ExceptionhandlerinfoExample example) {
        return exceptionhandlerinfoMapper.selectByExample(example);
    }

    @Override
    public Exceptionhandlerinfo selectByPrimaryKey(Long id) {
        return exceptionhandlerinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(Exceptionhandlerinfo record, ExceptionhandlerinfoExample example) {
        return exceptionhandlerinfoMapper.updateByExampleSelective(record,example);
    }

    @Override
    public int updateByExample(Exceptionhandlerinfo record, ExceptionhandlerinfoExample example) {
        return exceptionhandlerinfoMapper.updateByExample(record,example);
    }

    @Override
    public int updateByPrimaryKeySelective(Exceptionhandlerinfo record) {
        return exceptionhandlerinfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Exceptionhandlerinfo record) {
        return exceptionhandlerinfoMapper.updateByPrimaryKey(record);
    }
}
