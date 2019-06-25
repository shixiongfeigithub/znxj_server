package com.niule.znxj.web.service;

import com.niule.znxj.web.model.Systemsettinginfo;
import com.niule.znxj.web.model.SystemsettinginfoExample;

import java.util.List;

/**
 * Created by administor on 2017/5/16.
 */
public interface SystemService {
    int updSysByKey(String key,String value);
    List<Systemsettinginfo> selectByExample();
    Systemsettinginfo findByKey(String key);
}
