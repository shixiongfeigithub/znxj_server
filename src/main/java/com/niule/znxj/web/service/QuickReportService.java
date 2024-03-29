package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Quickreport;
import com.niule.znxj.web.model.QuickreportExample;

import java.util.HashMap;
import java.util.List;

/**
 * Created by administor on 2017/4/18.
 */
public interface QuickReportService {
    List<Quickreport> selectByExample(int page, int pagesize, int type);
    int countByExample(int type);
    Quickreport selectByPrimaryKey(Long id);
    int deleteByPrimaryKey(Long id);
    List<Quickreport> showQuickreport(HashMap<String, Object> map);

    List<Quickreport> showQuickreport2(HashMap<String, Object> map);
    int countByExample2(HashMap<String, Object> map);

    int updateByPrimaryKeySelective(Quickreport record);

    int countByCloseState(HashMap<String, Object> map);
}
