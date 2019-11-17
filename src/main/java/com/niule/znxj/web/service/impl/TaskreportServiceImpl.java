package com.niule.znxj.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niule.znxj.core.entity.JSONResult;
import com.niule.znxj.core.entity.Result;
import com.niule.znxj.core.util.DateUtils;
import com.niule.znxj.core.util.json.JsonUtil;
import com.niule.znxj.web.dao.*;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.model.response.*;
import com.niule.znxj.web.service.TaskreportService;
import net.sf.json.JSONArray;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.niule.znxj.core.util.DateUtils.getDaysDate;

/**
 * Created by administor on 2017/4/6.
 */
@Service
public class TaskreportServiceImpl implements TaskreportService {

    @Resource
    private TaskreportinfoMapper taskreportinfoMapper;
    @Resource
    private ReportcontentMapper reportcontentMapper;
    @Resource
    private TasktempinfoMapper tasktempinfoMapper;
    @Resource
    private OperatelogMapper operatelogMapper;
    @Resource
    private ExceptionhandlerinfoMapper exceptionhandlerinfoMapper;

    @Override
    public List<Tasktempinfo> dadiyreport(String date, Long taskidstr) {
        TasktempinfoExample example = new TasktempinfoExample();
        TasktempinfoExample.Criteria criteria = example.createCriteria();
        if (date != null && date != "") {
            criteria.andExecutetimeEqualTo(date);
        }
        if (taskidstr != null) {
            criteria.andTaskidEqualTo(taskidstr);
        }
        criteria.andStateNotEqualTo(-1);
        example.setOrderByClause("executetime desc");
        return tasktempinfoMapper.dadiyreport(example);
    }

    @Override
    public List<Taskreportinfo> dadiyreportlist(Long taskidstr, String date1,String date2) throws ParseException {
        TaskreportinfoExample example = new TaskreportinfoExample();
        TaskreportinfoExample.Criteria criteria = example.createCriteria();
        if (date1 != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (date2 != null) {
                criteria.andUploadtimeBetween(sdf.parse(date1),sdf.parse(date2));
            }else {
                criteria.andUploadtimeBetween(sdf.parse(date1), DateUtils.getDaysDate(-1, sdf.parse(date1)));
            }
        }
        if (taskidstr != null) {
            criteria.andTaskidEqualTo(taskidstr);
        }
        return taskreportinfoMapper.dadiyreportlist(example);
    }

    @Override
    public List<Tasktempinfo> dadiyreport2(String date1, String date2, Long taskidstr) {
        TasktempinfoExample example = new TasktempinfoExample();
        TasktempinfoExample.Criteria criteria = example.createCriteria();
        if (date1 != null && date1 != "" && date2 != null && date2 != "") {
            criteria.andExecutetimeBetween(date1, date2);
        }
        if (taskidstr != null) {
            criteria.andTaskidEqualTo(taskidstr);
        }
        criteria.andStateNotEqualTo(-1);
        example.setOrderByClause("executetime desc");
        return tasktempinfoMapper.dadiyreport(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return taskreportinfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Taskreportinfo selectByPrimaryKey(Long id) {
        return taskreportinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<Reportcontent> queryByTaskidAndDonetime(int page, int size, int taskid, String donetime, String reportstate) {
        List<Taskreportinfo> reportcontents = taskreportinfoMapper.queryByTaskidAndDonetime(taskid, donetime);
        HashMap<String, Object> map = new HashMap<>();
        List<Long> reportid = new ArrayList<>();
        for (Taskreportinfo report : reportcontents) {
            reportid.add(report.getId());
        }
        PageHelper.startPage(page, size);
        ReportcontentExample example = new ReportcontentExample();
        ReportcontentExample.Criteria criteria = example.createCriteria();
        criteria.andReportidIn(reportid);
        List<Reportcontent> reportcontentList = null;

        if ((donetime != "" && donetime != null)) {
            criteria.andOperationtimeEqualTo(donetime);
        }
        if ((reportstate != "" && reportstate != null)) {
            criteria.andReportstateEqualTo(reportstate);
        }
        reportcontentList = reportcontentMapper.selectByExample(example);

        return new PageInfo<>(reportcontentList);
    }

    @Override
    public List<Taskreportinfo> queryByTaskidAndDonetime2(int type, int taskid, String donetime) {
        return taskreportinfoMapper.huiZong(type, taskid, donetime);
    }

    @Override
    public List<Taskreportinfo> queryByTaskidAndDonetime3(int type, int taskid, String time1, String time2) {
        TaskreportinfoExample example = new TaskreportinfoExample();
        TaskreportinfoExample.Criteria criteria = example.createCriteria();
        criteria.andTaskidEqualTo((long) taskid).andTasktypeEqualTo(type);
        if (time1 != null && !Objects.equals(time1, "") && time2 != null && !Objects.equals(time2, "")) {
            criteria.andDonetimeBetween2(time1, time2);
        }
        return taskreportinfoMapper.selectByExample(example);
    }

    @Override
    public List<Reportcontent> exportexcel(int taskid, String donetime) {
        List<Taskreportinfo> reportcontents = taskreportinfoMapper.queryByTaskidAndDonetime(taskid, donetime);
        HashMap<String, Object> map = new HashMap<>();
        List<Long> reportid = new ArrayList<>();
        for (Taskreportinfo report : reportcontents) {
            reportid.add(report.getId());
        }
        ReportcontentExample example = new ReportcontentExample();
        ReportcontentExample.Criteria criteria = example.createCriteria();
        criteria.andReportidIn(reportid);
        List<Reportcontent> reportcontentList = reportcontentMapper.selectByExample(example);
        return reportcontentList;
    }

    @Override
    public List<Taskreportinfo> queryByState(HashMap<String, Object> map) {
        return taskreportinfoMapper.queryByState(map);
    }

    @Override
    public List<Taskreportinfo> findByPageReport(HashMap<String, Object> map) {
        return taskreportinfoMapper.findByPageReport(map);
    }

    @Override
    public int countReport(HashMap<String, Object> map) {
        return taskreportinfoMapper.countReport(map);
    }

    @Override
    public PageInfo<Taskreportinfo> gettaskreportlog2(HashMap<String, Object> map) {
        int page = (int) map.get("page");
        int pagesize = (int) map.get("pagesize");
        PageHelper.startPage(page, pagesize);
        return new PageInfo<>(taskreportinfoMapper.gettaskreportlog2(map));
    }

    @Override
    public int delTaskLog(Long id) {
        TaskreportinfoWithBLOBs taskreportinfo = taskreportinfoMapper.selectByPrimaryKey(id);
        taskreportinfo.setLogcat(null);
        return taskreportinfoMapper.updateByPrimaryKeyWithBLOBs(taskreportinfo);
    }

    @Override
    public int deleteByExample(TaskreportinfoExample example) {
        return taskreportinfoMapper.deleteByExample(example);
    }

    @Override
    public List<Taskreportinfo> selectByExample(TaskreportinfoExample example) {
        return taskreportinfoMapper.selectByExample(example);
    }

    @Override
    public List<Taskreportinfo> getreportbytype(int type) {
        return taskreportinfoMapper.getreportbytype(type);
    }

    @Override
    public List<Taskreportinfo> findByPageReport2(HashMap<String, Object> map) {
        return taskreportinfoMapper.findByPageReport2(map);
    }

    @Override
    public int countReport2(HashMap<String, Object> map) {
        return taskreportinfoMapper.countReport2(map);
    }

    @Override
    public List<Taskreportinfo> findByPageReport3(HashMap<String, Object> map) {
        return taskreportinfoMapper.findByPageReport3(map);
    }

    @Override
    public int countReport3(HashMap<String, Object> map) {
        return taskreportinfoMapper.countReport3(map);
    }

    @Override
    public List<Taskreportinfo> findByPageReport4(HashMap<String, Object> map) {
        return taskreportinfoMapper.findByPageReport4(map);
    }

    @Override
    public int countReport4(HashMap<String, Object> map) {
        return taskreportinfoMapper.countReport4(map);
    }
    /**
     * 饼图设计
     *
     * @return
     */
    @Override
    public List<Map<String, Integer>> getStaticMachineRateByReport() {
        return taskreportinfoMapper.getStaticMachineRateByReport();
    }

    @Override
    public List<Map<String, Integer>> getMachineRateByParam(HashMap<String, Object> map) {
        return taskreportinfoMapper.getMachineRateByParam(map);
    }

    @Override
    public List<Map<String, Integer>> lineChart() {
        return taskreportinfoMapper.lineChart();
    }

    @Override
    public List<Map<String, Integer>> lineChartByParam(HashMap<String, Object> map) {
        return taskreportinfoMapper.lineChartByParam(map);
    }

    @Override
    public List<Map<String, Object>> AvgTimelineChart() {
        return taskreportinfoMapper.AvgTimelineChart();
    }

    @Override
    public PageInfo<Taskreportinfo> reportList(HashMap<String, Object> map) throws Exception {
        int page = (int) map.get("page");
        int size = (int) map.get("size");
        Long siteid = map.get("siteid") == null || "".equals(map.get("siteid")) ? null : Long.parseLong(map.get("siteid").toString());
        Long taskid = map.get("taskid") == null || "".equals(map.get("taskid")) ? null : Long.parseLong(map.get("taskid").toString());
        Integer tasktype = map.get("tasktype") == null || "".equals(map.get("tasktype")) ? null : Integer.parseInt(map.get("tasktype").toString());
        String worker = "".equals(map.get("worker")) ? null : map.get("worker").toString();
        String time1 = map.get("time1") == null || "".equals(map.get("time1")) ? null : map.get("time1").toString();
        String time2 = map.get("time2") == null || "".equals(map.get("time2")) ? null : map.get("time2").toString();
        if (taskid != null) {
            TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
            TaskreportinfoExample.Criteria criteria = taskreportinfoExample.createCriteria();
            if (worker != null)
                criteria.andWorkerLike(worker);
            if (time1 != null && time2 != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(time1);
                Date date2 = sdf.parse(time2);
                criteria.andexecutetimeBetween(getDaysDate(0, date1), getDaysDate(-1, date2));
            }
            criteria.andTempTaskidEqualTo(taskid);
            PageHelper.startPage(page, size);
            return new PageInfo<>(taskreportinfoMapper.selectByExample(taskreportinfoExample));
        }
        PageHelper.startPage(page, size);
        return new PageInfo<>(taskreportinfoMapper.reportList2(map));
    }

    @Override
    public Result avgTimeLine(HashMap<String, Object> map) {
        List<Taskreportinfo> originData = getAllReport(map);
        Map<String, Map<String, List<Taskreportinfo>>> origResMap = new HashMap<>();
        Map<String, Map<String, Integer>> resMap = new HashMap();

        Map<String, String> taskNameMap = new HashMap();
        Map<String, String> taskMonthMap = new HashMap();
        if (originData.size() == 0)
            return new JSONResult<>();
        for (Taskreportinfo info : originData) {
            if (!taskNameMap.containsKey(getTaskName(info.getTaskcode()))) {
                taskNameMap.put(getTaskName(info.getTaskcode()), getTaskName(info.getTaskcode()));
            }
            if (!taskMonthMap.containsKey(getTaskMonth(info.getDonetime()))) {
                taskMonthMap.put(getTaskMonth(info.getDonetime()), getTaskMonth(info.getDonetime()));
            }
        }

        WebChartRes res = new WebChartRes();
        List<String> monthList = new ArrayList<>();
        CollectionUtils.addAll(monthList, taskMonthMap.values().toArray());

        res.setMonthes(monthList);
        List<WebChartResBean> series = new ArrayList<>();
        res.setSeries(series);
        for (String name : taskNameMap.values()) {
            origResMap.put(name, new HashMap<String, List<Taskreportinfo>>());
            resMap.put(name, new HashMap<String, Integer>());
            for (String month : taskMonthMap.values()) {
                origResMap.get(name).put(month, new ArrayList<Taskreportinfo>());
            }
        }
        for (Taskreportinfo info : originData) {
            origResMap.get(getTaskName(info.getTaskcode())).get(getTaskMonth(info.getDonetime())).add(info);
        }
        WebChartResBean item;
        for (String name : taskNameMap.values()) {
            List<Integer> dataList = new ArrayList<>();
            for (String month : taskMonthMap.values()) {
                List<Taskreportinfo> reportlist = origResMap.get(name).get(month);
                if (reportlist.size() == 0)
                    continue;
                int timeCount = 0;
                for (Taskreportinfo info : reportlist) {
                    timeCount += getMinite(info.getStarttime(), info.getEndtime());
                }
                int avgTime = timeCount / reportlist.size();
                resMap.get(name).put(month, avgTime);
                dataList.add(avgTime);
            }
            item = new WebChartResBean();
            item.setName(name);
            item.setData(dataList);
            series.add(item);
        }
        return new JSONResult<>(res);
    }

    public String getTaskName(String var) {
        return var.split("-")[0];
    }

    public String getTaskMonth(Date var) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(var);
        return cal.get(Calendar.MONTH) + 1 + "";
    }

    public int getMinite(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / (1000 * 60));
    }

    public List<Taskreportinfo> getAllReport(HashMap<String, Object> map) {
        Long siteid = map.get("siteid") == null || "".equals(map.get("siteid")) ? null : Long.parseLong(map.get("siteid").toString());
        Long taskid = map.get("taskid") == null || "".equals(map.get("taskid")) ? null : Long.parseLong(map.get("taskid").toString());
        Integer tasktype = map.get("tasktype") == null || "".equals(map.get("tasktype")) ? null : Integer.parseInt(map.get("tasktype").toString());
        String worker = "".equals(map.get("worker")) ? null : map.get("worker").toString();
        String time1 = map.get("time1") == null || "".equals(map.get("time1")) ? null : map.get("time1").toString();
        String time2 = map.get("time2") == null || "".equals(map.get("time2")) ? null : map.get("time2").toString();
        if (taskid != null) {
            TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
            TaskreportinfoExample.Criteria criteria = taskreportinfoExample.createCriteria();
            if (worker != null)
                criteria.andWorkerLike(worker);
            if (time1 != null && time2 != null)
                criteria.andDonetimeBetween3(time1, time2);
            criteria.andTaskidEqualTo(taskid);
            return taskreportinfoMapper.selectByExample(taskreportinfoExample);
        }
        return taskreportinfoMapper.reportList2(map);
    }

    @Override
    public List<TaskReportContent> equipSateInfo(int page, int size) {
        TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
        List<Taskreportinfo> taskreportinfos = taskreportinfoMapper.selectByExample(taskreportinfoExample);

        List<TaskReportContent> reportContents = new ArrayList<TaskReportContent>();
        for (Taskreportinfo report : taskreportinfos) {
            String content = "{" + "\"res\":" + report.getContent() + "}";
            if (content != null) {
                TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
                if (res != null && res.getRes().size() > 0) {
                    for (TaskReportContent reportContent : res.getRes()) {
                        reportContents.add(reportContent);
                    }
                }
            }
        }
        return reportContents;
    }

    @Override
    /**
     * 设备状态情况信息统计列表
     */
    public PageInfo<Reportcontent> equipSateInfo2(int page, int size, String checkname, String startTime, String endTime, String sitename, String areaname, String equipname) throws ParseException {
        PageHelper.startPage(page, size);
        List<Reportcontent> pageInfo = null;
        ReportcontentExample reportcontentExample = new ReportcontentExample();
        ReportcontentExample.Criteria criteria = reportcontentExample.createCriteria();
        reportcontentExample.setOrderByClause("id desc");
        if ((checkname == "" && checkname == null) && (sitename == "" || sitename == null) && (areaname.equals("") || areaname == null) && (equipname.equals("") || equipname == null)) {
            pageInfo = reportcontentMapper.selectByExample(reportcontentExample);
        } else {
            if (areaname != "" && areaname != null) {
                criteria.andAreanameEqualTo(areaname);
            }
            if (equipname != "" && equipname != null) {
                criteria.andEquipnameEqualTo(equipname);
            }
            if (checkname != "" && checkname != null) {
                criteria.andChecknameEqualTo(checkname);
            }
            if ((startTime != "" && startTime != null) && (endTime != "" && endTime != null)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                criteria.andOperationtimeBetween(startTime, sdf.format(getDaysDate(-1, sdf.parse(endTime))));
            }
            pageInfo = reportcontentMapper.selectByExample(reportcontentExample);
        }
        return new PageInfo<>(pageInfo);
    }

    public List<TaskReportContent> selectByExampleEquipState(int page, int size, List<Long> taskid, Date time1, Date time2, String sitename, String areaname, String equipname, String checkname) {
        //获取满足条件的报告
        TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
        taskreportinfoExample.createCriteria().andTaskidIn(taskid).andDonetimeBetween(time2, time1);
        List<Taskreportinfo> taskreportinfos = taskreportinfoMapper.selectByExample(taskreportinfoExample);
        List<TaskReportContent> reportContents = new ArrayList<>();
        for (Taskreportinfo report : taskreportinfos) {
            String content = "{" + "\"res\":" + report.getContent() + "}";
            if (content != null) {
                TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
                for (TaskReportContent reportContent : res.getRes()) {
                    if (areaname.equals(reportContent.getAreaname()) && equipname.equals(reportContent.getEquipname()) && checkname.equals(reportContent.getCheckname())) {
                        reportContents.add(reportContent);
                        break;
                    }
                }
            }
        }
        System.out.print(reportContents.size());
        return reportContents;
    }

    /**
     * @param checkname
     * @param startTime
     * @param endTime
     * @param areaname
     * @param equipname
     * @return
     */
    public List<Reportcontent> equipSateInfo3(String checkname, String startTime, String endTime, String areaname, String equipname) throws ParseException {
        List<Reportcontent> pageInfo = null;
        ReportcontentExample reportcontentExample = new ReportcontentExample();
        reportcontentExample.setOrderByClause("id desc");

        if (checkname != "" && checkname != null && startTime != "" && startTime != null && endTime != "" && endTime != null && (areaname != null || areaname != "") && (equipname != null || equipname != "")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            reportcontentExample.createCriteria().andChecknameEqualTo(checkname).andOperationtimeBetween(startTime, sdf.format(getDaysDate(-1, sdf.parse(endTime)))).andAreanameEqualTo(areaname).andEquipnameEqualTo(equipname);
            pageInfo = reportcontentMapper.selectByExample(reportcontentExample);
        } else {
            pageInfo = reportcontentMapper.selectByExample(reportcontentExample);
        }
        return pageInfo;
    }

    /**
     * 设备状态情况折线图
     *
     * @param page
     * @param size
     * @param checkname
     * @param startTime
     * @return
     */
    public WebChartRes2 equipStateChart(int page, int size, String checkname, String startTime, String endTime, String areaname, String equipname) throws ParseException {
        List<Reportcontent> equipstates = equipSateInfo3(checkname, startTime, endTime, areaname, equipname);
        WebChartRes2 res = new WebChartRes2();
        List<String> monthList = new ArrayList<>();
        List<WebChartResBean2> series = new ArrayList<>();

        WebChartResBean2 webChartResBean = new WebChartResBean2();

        Map<Integer, Integer> timeMap = new HashMap<Integer, Integer>();
        List<Double> dataList = new ArrayList<Double>();
        for (Reportcontent item : equipstates) {
            Calendar time = Calendar.getInstance();
            if (item.getOperationtime() != null && !"".equals(item.getOperationtime())) {
                if (item.getCheckvalue() != null && !"".equals(item.getCheckvalue())) {
                    monthList.add(item.getOperationtime());
                    dataList.add(Double.parseDouble(item.getCheckvalue()));
                }
            }
        }
        if (equipstates.size() > 0 && !"".equals(equipstates.get(0).getCheckname()))
            webChartResBean.setName(equipstates.get(0).getCheckname());
        webChartResBean.setData(dataList);
        series.add(webChartResBean);
        res.setSeries(series);
        res.setMonthes(monthList);
        return res;
    }

    public Integer getTaskDay(Date var) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(var);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int updReportContent(String reportData, Long reportId, String examuser, String taskcode) {
        //json集合
        JSONArray jsonArray = JSONArray.fromObject(reportData);
        //Java集合
        List<CheckReport> list = (List<CheckReport>) jsonArray.toCollection(jsonArray, CheckReport.class);
        //更新报告审核时间以，审核状态，审核人
        TaskreportinfoWithBLOBs taskreportinfo = new TaskreportinfoWithBLOBs();
        taskreportinfo.setId(reportId);
        taskreportinfo.setExamstate(1);
        taskreportinfo.setExamtime(new Date());
        taskreportinfo.setExamuser(examuser);
        int updCheckTime = taskreportinfoMapper.updateByPrimaryKeySelective(taskreportinfo);
        int updReportContent = 0;
        //更新reportcontent的审核值
        for (CheckReport checkReport : list) {
            Reportcontent reportcontent = new Reportcontent();
            reportcontent.setId(checkReport.getId());
            reportcontent.setCheckvalue(checkReport.getCheckvalue());
            updReportContent = reportcontentMapper.updateCheckValue(reportcontent);
        }
        if (updCheckTime > 0 && updReportContent > 0) {
            String info = "用户" + examuser + "复核报告" + taskcode + "成功";
            return insertSelective(examuser, info);
        } else
            return 0;
    }

    @Override
    public Reportcontent selectReportContentByPrimaryKey(Long id) {
        return reportcontentMapper.selectByPrimaryKey(id.intValue());
    }

    @Override
    public List<Reportcontent> selectReportcontentByParam(HashMap<String, Object> map) {
        return reportcontentMapper.selectByExample2(map);
    }

    @Override
    public int countReportcontentByParam(HashMap<String, Object> map) {
        return reportcontentMapper.countByExample2(map);
    }
    @Override
   public int countReportcontentByExceptionState(HashMap<String, Object> map){
        return reportcontentMapper.countByExample3(map);
   }

    public int insertSelective(String username, String operate) {
        Operatelog operatelog = new Operatelog();
        operatelog.setUsername(username);
        operatelog.setTime(new Date());
        operatelog.setOperate(operate);
        return operatelogMapper.insertSelective(operatelog);
    }
}
