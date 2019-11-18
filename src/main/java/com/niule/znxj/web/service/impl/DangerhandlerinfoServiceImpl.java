package com.niule.znxj.web.service.impl;

import com.niule.znxj.web.dao.DangerhandlerinfoMapper;
import com.niule.znxj.web.model.Dangerhandlerinfo;
import com.niule.znxj.web.model.DangerhandlerinfoExample;
import com.niule.znxj.web.service.DangerhandlerinfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuqb
 * Date: 2019-11-04 15:40
 */
@Service
public class DangerhandlerinfoServiceImpl implements DangerhandlerinfoService {

    @Resource
    private DangerhandlerinfoMapper dangerhandlerinfoMapper;

    @Override
    public int countByExample(DangerhandlerinfoExample example) {
        return dangerhandlerinfoMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(DangerhandlerinfoExample example) {
        return dangerhandlerinfoMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return dangerhandlerinfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Dangerhandlerinfo record) {
        return dangerhandlerinfoMapper.insert(record);
    }

    @Override
    public int insertSelective(Dangerhandlerinfo record) {
        return dangerhandlerinfoMapper.insertSelective(record);
    }

    @Override
    public List<Dangerhandlerinfo> selectByExample(DangerhandlerinfoExample example) {
        return dangerhandlerinfoMapper.selectByExample(example);
    }

    @Override
    public Dangerhandlerinfo selectByPrimaryKey(Long id) {
        return dangerhandlerinfoMapper.selectByPrimaryKey(id);
    }
    @Override
    public Dangerhandlerinfo selectInfoByReportId(Long id){
        return dangerhandlerinfoMapper.selectInfoByReportId(id);
    }
    @Override
    public int updateByExampleSelective(Dangerhandlerinfo record, DangerhandlerinfoExample example) {
        return dangerhandlerinfoMapper.updateByExampleSelective(record,example);
    }

    @Override
    public int updateByExample(Dangerhandlerinfo record, DangerhandlerinfoExample example) {
        return dangerhandlerinfoMapper.updateByExample(record,example);
    }

    @Override
    public int updateByPrimaryKeySelective(Dangerhandlerinfo record) {
        return dangerhandlerinfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Dangerhandlerinfo record) {
        return dangerhandlerinfoMapper.updateByPrimaryKey(record);
    }
}
