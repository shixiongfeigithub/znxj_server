package com.niule.znxj.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.niule.znxj.web.dao.QuickreportMapper;
import com.niule.znxj.web.model.Quickreport;
import com.niule.znxj.web.model.QuickreportExample;
import com.niule.znxj.web.service.QuickReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by administor on 2017/4/18.
 */
@Service
public class QuickReportServiceImpl implements QuickReportService{
    @Resource
    private QuickreportMapper quickreportMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return quickreportMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int countByExample(int type) {
        return quickreportMapper.countByExample(type);
    }

    @Override
    public Quickreport selectByPrimaryKey(Long id) {
        return quickreportMapper.selectByPrimaryKey(id);
    }

   @Override
    public List<Quickreport> selectByExample(int page,int pagesize,int type) {
        return quickreportMapper.selectByExample(type,(page-1)*pagesize,pagesize);
    }

    @Override
    public List<Quickreport> showQuickreport(HashMap<String,Object> map) {
        int page=Integer.parseInt(map.get("page").toString());
        int size=Integer.parseInt(map.get("size").toString());
        PageHelper.startPage(page,size);
        return quickreportMapper.showQuickreport(map);
    }

    @Override
    public List<Quickreport> showQuickreport2(HashMap<String,Object> map) {
        return quickreportMapper.showQuickreport2(map);
    }

    @Override
    public int countByExample2(HashMap<String,Object> map) {
        return quickreportMapper.countByExample2(map);
    }

    @Override
    public int countByCloseState(HashMap<String,Object> map) {
        return quickreportMapper.countByCloseState(map);
    }

    @Override
    public int updateByPrimaryKeySelective(Quickreport record) {
        return quickreportMapper.updateByPrimaryKeySelective(record);
    }
}
