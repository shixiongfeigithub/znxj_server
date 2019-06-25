package com.niule.znxj.web.dao;

import com.github.pagehelper.PageInfo;
import com.niule.znxj.core.entity.Result;
import com.niule.znxj.web.model.Taskreportinfo;
import com.niule.znxj.web.model.TaskreportinfoExample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TaskreportinfoMapper {
    int countByExample(TaskreportinfoExample example);

    int deleteByExample(TaskreportinfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Taskreportinfo record);

    int insertSelective(Taskreportinfo record);

    List<Taskreportinfo> selectByExampleWithBLOBs(TaskreportinfoExample example);

    List<Taskreportinfo> selectByExample(TaskreportinfoExample example);

    Taskreportinfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Taskreportinfo record, @Param("example") TaskreportinfoExample example);

    int updateByExampleWithBLOBs(@Param("record") Taskreportinfo record, @Param("example") TaskreportinfoExample example);

    int updateByExample(@Param("record") Taskreportinfo record, @Param("example") TaskreportinfoExample example);

    int updateByPrimaryKeySelective(Taskreportinfo record);

    int updateByPrimaryKeyWithBLOBs(Taskreportinfo record);

    int updateByPrimaryKey(Taskreportinfo record);

    public List<Taskreportinfo> findByPageReport(HashMap<String,Object> map);
    public int countReport(HashMap<String,Object> map);

    public List<Taskreportinfo> findByPageReport2(HashMap<String,Object> map);
    public int countReport2(HashMap<String,Object> map);

    List<Taskreportinfo> queryByTaskidAndDonetime(int taskid, String donetime);

    /*查询某一天某项任务操作状态是正常或者异常的任务报告的信息*/
    List<Taskreportinfo> queryByState(HashMap<String,Object> map);

//    List<Taskreportinfo>gettaskreportlog();
    List<Taskreportinfo>gettaskreportlog2(HashMap<String,Object> map);

    List<Taskreportinfo>getreportbytype(int type);
    //不带参数饼图设计
    public List<Map<String, Integer>> getStaticMachineRateByReport();
    //带参数饼图设计
    public List<Map<String, Integer>> getMachineRateByParam(HashMap<String,Object> map);
    //折线图不带参数
    public List<Map<String, Integer>> lineChart();
    //折线图带参数
    public List<Map<String, Integer>> lineChartByParam(HashMap<String,Object> map);
    public List<Map<String, Object>>AvgTimelineChart();
    List<Taskreportinfo> reportList2(HashMap<String,Object> map);
    List<Taskreportinfo> reportList3(HashMap<String,Object> map);
    List<Taskreportinfo> timeChart(Long taskid);

    List<Taskreportinfo> dadiyreportlist(TaskreportinfoExample example);
    List<Taskreportinfo> dadiyreportlist2(HashMap<String,Object> map);
}