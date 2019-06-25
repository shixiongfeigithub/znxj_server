package com.niule.znxj.web.controller;

import com.github.pagehelper.PageInfo;
import com.niule.znxj.core.entity.JSONResult;
import com.niule.znxj.core.entity.Result;
import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.core.util.PageUtil;
import com.niule.znxj.core.util.json.JsonUtil;
import com.niule.znxj.web.dao.*;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.model.response.WebChartRes2;
import com.niule.znxj.web.service.*;

import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by administor on 2017/4/6.
 */
@Controller
public class WebTaskreportController {
    @Resource
    private TaskreportService taskreportService;
    @Resource
    private TaskstopinfoMapper taskstopinfoMapper;
    @Resource
    private OperateLogService operateLogService;
    @Resource
    private SiteService siteService;
    @Resource
    private TaskPlanService taskPlanService;
    @Resource
    private AreaService areaService;
    @Resource
    private EquipmentService equipmentService;
    @Resource
    private CheckInfoService checkInfoService;
    @Resource
    private EquipmentTaskMapper equipmentTaskMapper;
    @Resource
    private CheckitemTaskMapper checkitemTaskMapper;
    @Resource
    private ReportcontentMapper reportcontentMapper;
    @Resource
    private TasktempinfoMapper tasktempinfoMapper;

    protected PageBean pageBean = new PageBean();


    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    /**
     * 分页显示所有任务的报告 - 列表
     *
     * @param m
     * @param page         当前页
     * @param tasktype     任务状态 0日常巡检 1计划巡检 2隐患排查 3视频巡检 4临时任务
     * @param taskCcode    任务编号
     * @param reportSstate 报告状态 0 正常 1 异常
     * @param time1        开始时间
     * @param time2        结束时间
     * @return
     */
    @RequestMapping("/showallreport1")
    public String showallreport1(Model m, int page, int tasktype, String taskCcode,
                                 String reportSstate, String time1, String time2,
                                 HttpServletRequest request, String operationstate,Long siteid,Integer searchtype) {
        if (page <= 0) {
            page = 1;
        }
        page = PageBean.countCurrentPage(page);
        List<Taskreportinfo> taskreportinfos = null;
        long totalpage = 0;
        int pagesize = 15;
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");

        List ids = new ArrayList();
        if (admininfo.getSiteid() == null) {
            List<Siteareainfo> sites = siteService.queryAllSite();
            for (Siteareainfo siteareainfo : sites) {
                ids.add(siteareainfo.getId());
            }
        } else {
            ids.add(admininfo.getSiteid());
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("page", (page - 1) * pagesize);
        map.put("pagesize", pagesize);
        if (taskCcode == null) {
            taskCcode = "";
        }
        map.put("taskcode", taskCcode == "" ? null : "%" + taskCcode + "%");
        map.put("reportstate", reportSstate);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("tasktype", tasktype);
        map.put("ids", ids);
        if ((taskCcode == null || "".equals(taskCcode)) &&
                (reportSstate == null || "".equals(reportSstate)) &&
                (time1 == null || "".equals(time1)) &&
                (time2 == null || "".equals(time2)) && (operationstate == null || "".equals(operationstate))) {
            taskreportinfos = taskreportService.findByPageReport(map);
            int rows1 = taskreportService.countReport(map);
            totalpage = PageBean.counTotalPage(pagesize, rows1);
            reportSstate = "";
        } else {
            int state = 2;
            if (operationstate.equals("5")) {
                state = 3;
                map.put("stopstate",1);
                map.put("operationstate", null);
            }else{
                map.put("stopstate",null);
                map.put("operationstate", operationstate);
            }

            map.put("state", state);
            taskreportinfos = taskreportService.findByPageReport2(map);
            int rows2 = taskreportService.countReport2(map);
            totalpage = PageBean.counTotalPage(pagesize, rows2);
        }
        List<Siteareainfo> siteareainfos = siteService.selectByExample2(admininfo.getSiteid());

        pageBean.setList(taskreportinfos);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean", pageBean);
        m.addAttribute("type", tasktype);
        m.addAttribute("taskCcode", taskCcode);
        m.addAttribute("siteid", siteid);
        m.addAttribute("searchtype", searchtype);
        m.addAttribute("reportSstate", reportSstate);
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        m.addAttribute("operationstate", operationstate);
        m.addAttribute("sites", siteareainfos);
        m.addAttribute("roleid", admininfo.getRoleid());

        return "showreport1";
    }

    /**
     * 删除指定的任务报告
     *
     * @param id
     * @return
     */
    @RequestMapping("/deltaskreport")
    @RequiresPermissions("del:taskreport")
    @ResponseBody
    public int deltaskreport(Integer id, Long tempid, HttpServletRequest request) {
        int delreoprt = 0;
        int deltemp = 0;
        if (id != 0) {
            delreoprt = taskreportService.deleteByPrimaryKey(Long.valueOf(id));
            deltemp = tasktempinfoMapper.updateTempState2(tempid);
        } else {
            deltemp = tasktempinfoMapper.updateTempState2(tempid);
            delreoprt = 1;
        }
//        HttpSession session=request.getSession();
//        String report=request.getParameter("report");
//        String name="删除报告"+report+"的信息";
//        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
//        String username=logadmininfo.getUsername();
//        int addlogres=operateLogService.insertSelective(username,name);
        if (deltemp > 0 && delreoprt > 0) {
            return 1;
        } else
            return 0;
    }

    /**
     * 任务报告的详细信息
     *
     * @param id   任务报告编号
     * @param m
     * @param type 任务类型
     * @return
     */
    @RequestMapping("querytaskreportdetail")
   /*@RequiresPermissions("item:reportdetail")*/
    public String querytaskreportdetail(Long id, Model m, int type) {
        Taskreportinfo taskreportinfo = taskreportService.selectByPrimaryKey(id);
//        m.addAttribute("type", type);
//        if (taskreportinfo != null) {
//            m.addAttribute("taskreportinfo", taskreportinfo);
//            if (type2 == 0) {
//                return "reportdetail";
//            } else {
//                return "reportdetail2";
//            }
//
//        } else
//            return "showallreport1?page=1&tasktype=" + type;

        List<List<String>> resList = new ArrayList<>();
        List<String> childList = new ArrayList<>();
        List<String> taskcode = new ArrayList<>();
        childList.add("区域");
        childList.add("设备");
        childList.add("巡检项");
        childList.add("巡检项类型");
        childList.add("执行时间");
        childList.add("低值");
        childList.add("高值");
        childList.add("告警下限");
        childList.add("告警上限");
        childList.add("数值");
        childList.add("异常描述");
        childList.add("数据名称");
        childList.add("单位");
        resList.add(childList);
        String content = "{" + "\"res\":" + taskreportinfo.getContent() + "}";
        TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
        for (TaskReportContent item : res.getRes()) {
            childList = new ArrayList<>();
            String area = item.getAreaname();
            String equipment = item.getEquipname();
            String checkname = item.getCheckname();
            String checkitype = item.getChecktype();
//            String executetime=item.getOperationtime();

            String normalmin= "";
            String normalmax =  "";
            String lowerwarning = "";
            String upperwarning ="";

            childList.add(area);
            childList.add(equipment);
            childList.add(checkname);
            childList.add(checkitype);
            childList.add(checkitype);
            if (item.getChecktype().equals("状态项")) {
                normalmin = "-";
                normalmax = "-";
                lowerwarning = "-";
                upperwarning = "-";
                childList.add(normalmin);
                childList.add(normalmax);
                childList.add(lowerwarning);
                childList.add(upperwarning);
                if("0".equals(item.getReportstate())){
                    childList.add("正常");
                    childList.add("-");
                }else {
                    childList.add("<span style='background:red;color:white;display:inline-block;padding:5 10;'>"+"异常"+"</span>");
                    childList.add("<a href='javascript:void(0);' onclick=showimg('" + JsonUtil.toJSON(item.getImg()) + "','" + JsonUtil.toJSON(item.getAudio()) + "','" + JsonUtil.toJSON(item.getVideo()) + "')>" + item.getErrcontent() + "</a>");
                }
            } else {
                normalmin = item.getNormalmin()+"";
                normalmax = item.getNormalmax()+"";
                lowerwarning = item.getLowerwarning()+"";
                upperwarning = item.getUpperwarning()+"";
                childList.add(normalmin);
                childList.add(normalmax);
                childList.add(lowerwarning);
                childList.add(upperwarning);
                if (item.getNumvalue() != "") {
                    if(item.getLowerwarning()!=0&&item.getUpperwarning()!=0){
                        if (Double.parseDouble(item.getNumvalue()) > item.getUpperwarning()) {
                            childList.add("<span style='color:red;'>" + item.getNumvalue() +  "</span>");
                            childList.add("<span style='color:red;'>" + "↑↑" + "</span>");
                        }
                        if (Double.parseDouble(item.getNumvalue()) < item.getLowerwarning()) {
                            childList.add("<span style='color:red;'>" + item.getNumvalue() + "</span>");
                            childList.add("<span style='color:red;'>" +  "↓↓" + "</span>");
                        }
                        if (item.getLowerwarning() <= Double.parseDouble(item.getNumvalue()) &&
                                Double.parseDouble(item.getNumvalue()) <= item.getUpperwarning()) {
                            childList.add(item.getNumvalue());
                            childList.add("-");
                        }
                    }else{
                        if (Double.parseDouble(item.getNumvalue()) > item.getNormalmax()) {
                            childList.add("<span style='color:red;'>" + item.getNumvalue()  + "</span>");
                            childList.add("<span style='color:red;'>" +  "↑↑" + "</span>");
                        }
                        if (Double.parseDouble(item.getNumvalue()) < item.getNormalmin()) {
                            childList.add("<span style='color:red;'>" + item.getNumvalue()  + "</span>");
                            childList.add("<span style='color:red;'>" +"↓↓" + "</span>");
                        }
                        if (item.getNormalmin() <= Double.parseDouble(item.getNumvalue()) &&
                                Double.parseDouble(item.getNumvalue()) <= item.getNormalmax()) {
                            childList.add(item.getNumvalue());
                            childList.add("-");
                        }
                    }
                } else {
                    childList.add("-");
                    childList.add("-");
                }
            }
            String recordname=item.getRecordname();
            String unitname=item.getUnitname();
            childList.add(recordname);
            childList.add(unitname);
            resList.add(childList);
        }
        m.addAttribute("reportinfos", resList);
        m.addAttribute("type",type);
        m.addAttribute("taskreportid",id);
        m.addAttribute("taskcode",taskreportinfo.getTaskcode());
        return "report3";
    }

    /**
     * 任务报告汇总
     *
     * @param taskid 任务编号
     * @param m
     * @param type   任务类型
     * @return
     */
    @RequestMapping("reportcount2")
    public String reportcount2(int taskid, Model m, int type, String donetime, String taskname) throws Exception {
//       Date date=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK).parse(donetime);
//       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//       String donetime2=sdf.format(date);
        List<Taskreportinfo> reportinfos = taskreportService.queryByTaskidAndDonetime2(type, taskid, donetime);

        List<List<String>> resList = new ArrayList<>();
        List<String> childList = new ArrayList<>();
        List<String> taskcode = new ArrayList<>();
        childList.add("区域");
        childList.add("设备");
        childList.add("巡检项");
        childList.add("低值");
        childList.add("高值");
        childList.add("告警下限");
        childList.add("告警上限");
//       for( Taskreportinfo info : reportinfos){
//           childList.add(info.getTaskcode());
//       }
        //aaaaaa
        taskcode.add(taskname);
        if(reportinfos.size()>0){
            for (Taskreportinfo info : reportinfos) {
                childList.add("报告值");
                childList.add("异常描述");
                taskcode.add(info.getTaskcode());
            }
            resList.add(taskcode);
            resList.add(childList);

            String content = "{" + "\"res\":" + reportinfos.get(0).getContent() + "}";
            TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
            for (TaskReportContent item : res.getRes()) {
                childList = new ArrayList<>();
                String area = item.getAreaname();
                String equipment = item.getEquipname();
                String checkname = item.getCheckname();
                String checkitype = item.getChecktype();
                String normalmin = item.getNormalmin() + "";
                String normalmax = item.getNormalmax() + "";
                String lowerwarning = item.getLowerwarning() + "";
                String upperwarning = item.getUpperwarning() + "";
                if (checkitype.equals("状态项")) {
                    normalmin = "-";
                    normalmax = "-";
                    lowerwarning = "-";
                    upperwarning = "-";
                }

                childList.add(area);
                childList.add(equipment);
                childList.add(checkname);
                childList.add(normalmin);
                childList.add(normalmax);
                childList.add(lowerwarning);
                childList.add(upperwarning);
                for (int i=0;i< reportinfos.size();i++) {
                    boolean hasValue = false;
                    String childcontent = "{" + "\"res\":" + reportinfos.get(i).getContent() + "}";
                    TaskReportRes childres = JsonUtil.toObject(childcontent, TaskReportRes.class);
                    for (TaskReportContent childitem : childres.getRes()) {
                        if (childitem.getAreaname().equals(area) && childitem.getEquipname().equals(equipment) && childitem.getCheckname().equals(checkname)) {
                            hasValue = true;
                            if (childitem.getChecktype().equals("状态项")) {
                                if("0".equals(childitem.getReportstate())){
                                    childList.add("正常");
                                    childList.add("-");
                                }else {
                                    childList.add("<span style='background:red;color:white;display:inline-block;padding:5 10;'>"+"异常"+"</span>");
                                    childList.add("<a href='javascript:void(0);' onclick=showimg('" + JsonUtil.toJSON(childitem.getImg()) + "','" + JsonUtil.toJSON(childitem.getAudio()) + "','" + JsonUtil.toJSON(childitem.getVideo()) + "')>" + childitem.getErrcontent() + "</a>");
                                }
                            } else {
                                if (childitem.getNumvalue() != "") {
                                    if(childitem.getLowerwarning()!=0&&childitem.getUpperwarning()!=0){
                                        if (Double.parseDouble(childitem.getNumvalue()) > childitem.getUpperwarning()) {
                                            childList.add("<span style='color:red;'>" + childitem.getNumvalue() +  "</span>");
                                            childList.add("<span style='color:red;'>" + "↑↑" + "</span>");
                                        }
                                        if (Double.parseDouble(childitem.getNumvalue()) < childitem.getLowerwarning()) {
                                            childList.add("<span style='color:red;'>" + childitem.getNumvalue() + "</span>");
                                            childList.add("<span style='color:red;'>" +  "↓↓" + "</span>");
                                        }
                                        if (childitem.getLowerwarning() <= Double.parseDouble(childitem.getNumvalue()) &&
                                                Double.parseDouble(childitem.getNumvalue()) <= childitem.getUpperwarning()) {
                                            childList.add(childitem.getNumvalue());
                                            childList.add("-");
                                        }
                                    }else{
                                        if (Double.parseDouble(childitem.getNumvalue()) > childitem.getNormalmax()) {
                                            childList.add("<span style='color:red;'>" + childitem.getNumvalue()  + "</span>");
                                            childList.add("<span style='color:red;'>" +  "↑↑" + "</span>");
                                        }
                                        if (Double.parseDouble(childitem.getNumvalue()) < childitem.getNormalmin()) {
                                            childList.add("<span style='color:red;'>" + childitem.getNumvalue()  + "</span>");
                                            childList.add("<span style='color:red;'>" +"↓↓" + "</span>");
                                        }
                                        if (childitem.getNormalmin() <= Double.parseDouble(childitem.getNumvalue()) &&
                                                Double.parseDouble(childitem.getNumvalue()) <= childitem.getNormalmax()) {
                                            childList.add(childitem.getNumvalue());
                                            childList.add("-");
                                        }
                                    }
                                } else {
                                    childList.add("-");
                                    childList.add("-");
                                }
                            }
                            break;
                        }
                    }
                    if (!hasValue) {
                        childList.add("-");
                        childList.add("-");
                    }
                }
                resList.add(childList);
            }
        }

        m.addAttribute("reportinfos", resList);
        m.addAttribute("donetime", donetime);
        m.addAttribute("type", type);
        m.addAttribute("taskid", taskid);
        m.addAttribute("taskname", taskname);
        m.addAttribute("taskcode", taskcode);
        return "bb";
    }

    @RequestMapping("reportcount")
    public String reportcount(int taskid, Model m, int page, int type, String operationtime, String reportstate, String taskname) throws Exception {
//       Date date=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK).parse(donetime);
//       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//       String donetime2=sdf.format(date);
        if (page <= 0) {
            page = 1;
        }
        int size = 15;
        PageInfo<Reportcontent> reportinfos = taskreportService.queryByTaskidAndDonetime(page, size, taskid, operationtime, reportstate);

        m.addAttribute("reportinfos", reportinfos);
        m.addAttribute("donetime", operationtime);
//       m.addAttribute("donetime2",donetime2);
        m.addAttribute("type", type);
        m.addAttribute("taskid", taskid);
        m.addAttribute("taskname", taskname);
//        if(type2==0){
//            return "reportcount";
//        }else
        return "reportcount2";

    }

    /* @RequestMapping("findbyreportstate")
      public String findbyreportstate(int taskid,String reportstate,String donetime,Model m){
         HashMap<String,Object> map=new HashMap<String,Object>();
         map.put("taskid",taskid);
         map.put("reportstate",reportstate);
         map.put("donetime",donetime);
         List<Taskreportinfo> reportinfos=taskreportService.queryByState(map);
         m.addAttribute("reportinfos",reportinfos);
         m.addAttribute("donetime",donetime);
         m.addAttribute("taskid",taskid);
         return "taskcount";
     }*/
    @RequestMapping("gettaskreportlog")
    @RequiresPermissions("item:taskreportlog")
    public String gettaskreportlog(int page, Model m, String taskname, String time, HttpServletRequest request) {
        if (page <= 0) {
            page = 1;
        }
        int size = 15;
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("pagesize", size);
        map.put("taskname", taskname == null ? "" : "%" + taskname + "%");
        map.put("time", time);
        map.put("siteid", admininfo.getSiteid());
        PageInfo<Taskreportinfo> taskreportinfoPageInfo = taskreportService.gettaskreportlog2(map);
        m.addAttribute("allreport", taskreportinfoPageInfo);
        return "showallreportlog";
    }

    /**
     * 删除操作日志
     *
     * @param id
     * @return
     */
    @RequestMapping("deltasklog")
    @ResponseBody
    public int deltasklog(Long id) {
        return taskreportService.delTaskLog(id);
    }

    @RequestMapping("getreportlog")
    @ResponseBody
    public Taskreportinfo getreportlog(Long id) {
        return taskreportService.selectByPrimaryKey(id);
    }

    @RequestMapping("queryByTempid")
    @ResponseBody
    public Taskstopinfo queryByTempid(int tempid) {
        return taskstopinfoMapper.queryStopByTempid(tempid);
    }

    /**
     * 任务完成情况饼图
     *
     * @param m
     * @param worker
     * @param tasktype
     * @param siteid
     * @param time1
     * @param time2
     * @param taskid
     * @return
     */
    @RequestMapping("/getStaticMachineRateByReport")
    @ResponseBody
    public Map<String, Object> getStaticMachineRateByReport(HttpServletRequest request, Model m, String worker, String tasktype, String siteid, String time1, String time2, String taskid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("worker", "%" + worker + "%");
        map.put("tasktype", tasktype);
        map.put("siteid", siteid);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("taskid", taskid);
        List<Map<String, Integer>> machines;
        if ((worker == null || "".equals(worker)) &&
                (tasktype == null || "".equals(tasktype)) &&
                (siteid == null || "".equals(siteid)) &&
                (time1 == null || "".equals(time1)) &&
                (time2 == null || "".equals(time2))) {

            machines = taskreportService.getStaticMachineRateByReport();
        } else {
            machines = taskreportService.getMachineRateByParam(map);
        }
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> siteareainfos = siteService.selectByExample2(admininfo.getSiteid());
        List<Siteareainfo> sites = siteService.selectByExample2(admininfo.getSiteid());
        map.put("machines", machines);
        map.put("sites", sites);
        map.put("tasktype", tasktype);
        map.put("worker", worker);
        map.put("siteid", siteid);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("taskid", taskid);
        return map;
    }

    /**
     * 任务完成情况折线统计
     *
     * @param worker   执行人
     * @param tasktype 任务类型
     * @param siteid   厂区编号
     * @param time1
     * @param time2
     * @param taskid   任务编号
     * @return
     */
    @RequestMapping("/linechart")
    @ResponseBody
    public List<Map<String, Integer>> linechart(String worker, String tasktype, String siteid, String time1, String time2, String taskid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("worker", "%" + worker + "%");
        map.put("tasktype", tasktype);
        map.put("siteid", siteid);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("taskid", taskid);
        List<Map<String, Integer>> machines;
        if ((worker == null || "".equals(worker)) &&
                (tasktype == null || "".equals(tasktype)) &&
                (siteid == null || "".equals(siteid)) &&
                (time1 == null || "".equals(time1)) &&
                (time2 == null || "".equals(time2))) {

            machines = taskreportService.lineChart();
        } else {
            machines = taskreportService.lineChartByParam(map);
        }
        return machines;
    }

    /**
     * 任务完成时间折线统计
     *
     * @return
     */
    @RequestMapping("/avgtimelinechart")
    @ResponseBody
    public Result avgtimelinechart(Model m, String worker, String tasktype, String siteid, String time1, String time2, String taskid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("worker", worker == null || "".equals(worker) ? "" : "%" + worker + "%");
        map.put("tasktype", tasktype);
        map.put("siteid", siteid);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("taskid", taskid);

        m.addAttribute("tasktype", tasktype);
        m.addAttribute("worker", worker);
        m.addAttribute("siteid", siteid);
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        m.addAttribute("taskid", taskid);
        return taskreportService.avgTimeLine(map);
    }

    /**
     * 任务完成情况和任务完成时间的数据列表
     *
     * @param page
     * @param m
     * @param worker   执行人
     * @param tasktype 任务类型
     * @param siteid   厂区编号
     * @param time1
     * @param time2
     * @param taskid   任务编号
     * @param type     0任务完成情况数据列表 1 任务完成时间数据列表
     * @return
     */
    @RequestMapping("reportlist")
    public String reportlist(int page, Model m, String worker, String tasktype, String siteid, String time1, String time2, Long taskid, int type, HttpServletRequest request) {
        if (page <= 0) {
            page = 1;
        }
        int size;
        if (type == 0) {
            size = 7;
        } else {
            size = 7;
        }
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        if (admininfo.getSiteid() != null) {
            siteid = admininfo.getSiteid() + "";
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("size", size);
        map.put("worker", worker == null || "".equals(worker) ? "" : "%" + worker + "%");
        map.put("tasktype", tasktype);
        map.put("siteid", siteid);
        map.put("time1", time1);
        map.put("time2", time2);
        map.put("taskid", taskid);
        PageInfo<Taskreportinfo> reports = null;
        reports = taskreportService.reportList(map);
        List<Siteareainfo> siteareainfos = siteService.selectByExample2(admininfo.getSiteid());
        m.addAttribute("reports", reports);
        m.addAttribute("sites", siteareainfos);
        m.addAttribute("tasktype", tasktype);
        m.addAttribute("worker", worker);
        m.addAttribute("siteid", siteid);
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        m.addAttribute("taskid", taskid);
        m.addAttribute("type", type);
        if (type == 0) {
            return "showtaskplan2";
        } else
            return "showtaskplan3";
    }

    /**
     * 根据厂区编号和任务类型查找任务
     *
     * @param siteid
     * @param type
     * @return
     */
    @RequestMapping("taskbysiteandtype")
    @ResponseBody
    public List<Taskplaninfo> taskbysiteandtype(Long siteid, Integer type) {
        return taskPlanService.selectByType(siteid, type);
       /* return taskreportService.getreportbytype(type);*/
    }

    /**
     * 将不同类型的任务报告导出到Excel
     *
     * @param type
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/exportExcel")
    public ModelAndView exportExcel(int taskid, Model m, int type, HttpServletResponse response, String time1,String time2, String taskname) throws Exception {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        // 传递中文参数编码
        String name = "任务" + taskname + "汇总" + ".xls";
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("GBK"), "ISO8859_1"));

        List<Taskreportinfo> reportinfos = taskreportService.queryByTaskidAndDonetime3(type, taskid, time1,time2);

        List<List<String>> resList = new ArrayList<>();
        List<String> childList = new ArrayList<>();
        List<String> taskcode = new ArrayList<>();
        childList.add("区域");
        childList.add("设备");
        childList.add("巡检项");
        childList.add("低值");
        childList.add("高值");
        childList.add("告警下限");
        childList.add("告警上限");
//       for( Taskreportinfo info : reportinfos){
//           childList.add(info.getTaskcode());
//       }
        taskcode.add(taskname);
        if(reportinfos.size()>0){

        }
        for (Taskreportinfo info : reportinfos) {
            childList.add("报告值");
            childList.add("异常描述");
            taskcode.add(info.getTaskcode());
        }
        resList.add(taskcode);
        resList.add(childList);

        String content = "{" + "\"res\":" + reportinfos.get(0).getContent() + "}";
        TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
        for (TaskReportContent item : res.getRes()) {
            childList = new ArrayList<>();
            String area = item.getAreaname();
            String equipment = item.getEquipname();
            String checkname = item.getCheckname();
            String checkitype = item.getChecktype();
            String normalmin = item.getNormalmin() + "";
            String normalmax = item.getNormalmax() + "";
            String lowerwarning = item.getLowerwarning() + "";
            String upperwarning = item.getUpperwarning() + "";
            if (checkitype.equals("状态项")) {
                normalmin = "-";
                normalmax = "-";
                lowerwarning = "-";
                upperwarning = "-";
            }
            childList.add(area);
            childList.add(equipment);
            childList.add(checkname);
            childList.add(normalmin);
            childList.add(normalmax);
            childList.add(lowerwarning);
            childList.add(upperwarning);

            for (Taskreportinfo info : reportinfos) {
                boolean hasValue = false;
                String childcontent = "{" + "\"res\":" + reportinfos.get(0).getContent() + "}";
                TaskReportRes childres = JsonUtil.toObject(childcontent, TaskReportRes.class);
                for (TaskReportContent childitem : childres.getRes()) {
                    if (childitem.getAreaname().equals(area) && childitem.getEquipname().equals(equipment) && childitem.getCheckname().equals(checkname)) {
                        hasValue = true;
                        if (childitem.getChecktype().equals("状态项")) {
                            if("0".equals(childitem.getReportstate())){
                                childList.add("正常");
                                childList.add("-");
                            }else {
                                childList.add("异常");
                                childList.add(childitem.getErrcontent() + "," + JsonUtil.toJSON(childitem.getImg()) + "," + JsonUtil.toJSON(childitem.getAudio()) + "," + JsonUtil.toJSON(childitem.getVideo()));

                                //childList.add("<a href='javascript:void(0);' onclick=showimg('" + JsonUtil.toJSON(childitem.getImg()) + "','" + JsonUtil.toJSON(childitem.getAudio()) + "','" + JsonUtil.toJSON(childitem.getVideo()) + "')>" + childitem.getErrcontent() + "</a>");
                            }
//                            if (!childitem.getErrcontent().equals("")) {
                               // childList.add(childitem.getErrcontent() + "," + JsonUtil.toJSON(childitem.getImg()) + "," + JsonUtil.toJSON(childitem.getAudio()) + "," + JsonUtil.toJSON(childitem.getVideo()));
//                            } else {
//                                childList.add("-" + "," + JsonUtil.toJSON(childitem.getImg()) + "," + JsonUtil.toJSON(childitem.getAudio()) + "," + JsonUtil.toJSON(childitem.getVideo()));
//                            }
                        } else {
                            if (childitem.getNumvalue() != "") {
                                if(childitem.getLowerwarning()!=0&&childitem.getUpperwarning()!=0){
                                    if (Double.parseDouble(childitem.getNumvalue()) > childitem.getUpperwarning()) {
                                        childList.add( childitem.getNumvalue() + "↑↑" );
                                        childList.add( "↑↑" );
                                    }
                                    if (Double.parseDouble(childitem.getNumvalue()) < childitem.getLowerwarning()) {
                                        childList.add( childitem.getNumvalue() + "↓↓" );
                                        childList.add( "↓↓" );
                                    }
                                    if (childitem.getLowerwarning() <= Double.parseDouble(childitem.getNumvalue()) &&
                                            Double.parseDouble(childitem.getNumvalue()) <= childitem.getUpperwarning()) {
                                        childList.add(childitem.getNumvalue());
                                        childList.add("-");
                                    }
                                }else{
                                    if (Double.parseDouble(childitem.getNumvalue()) > childitem.getNormalmax()) {
                                        childList.add(childitem.getNumvalue() + "↑↑");
                                        childList.add("↑↑");
                                    }
                                    if (Double.parseDouble(childitem.getNumvalue()) < childitem.getNormalmin()) {
                                        childList.add(childitem.getNumvalue() + "↓↓");
                                        childList.add( "↓↓");
                                    }
                                    if (childitem.getNormalmin() <= Double.parseDouble(childitem.getNumvalue()) &&
                                            Double.parseDouble(childitem.getNumvalue()) <= childitem.getNormalmax()) {
                                        childList.add(childitem.getNumvalue());
                                        childList.add("-");
                                    }
                                }
                            } else {
                                childList.add("-");
                                childList.add("-");
                            }
                        }
                        break;
                    }
                }
                if (!hasValue) {
                    childList.add("-");
                }
            }
            resList.add(childList);
        }

        // 定义一个工作薄
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(name);
        // 创建一行
        Row row = sheet.createRow(0);
        CellRangeAddress cra2 = new CellRangeAddress(0, 0, 0, 0);
        for (int i = 0; i < resList.size(); i++) {
            int colsindex = 0;
            row = sheet.createRow(i);
            int a = 0;
            int b1 = 0;
            int b2 = 0;
            List<String> cols = resList.get(i);
            for (int j = 0; j < cols.size(); j++) {
                if (i == 0) {
                    if (j == 0) {
                        a = colsindex + 6;
                        b1 = a;
//                        CellRangeAddress cra=new CellRangeAddress(0, 0, 0,a);
                        row.createCell(0).setCellValue(cols.get(0));
//                        sheet.addMergedRegion(cra);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, a));
                    } else {
                        if (b1 < 7) {
                            b1 = b1 + 1;
                            b2 = b1 + 1;
                        } else {
                            b1 = b1 + 2;
                            b2 = b1 + 1;
                        }
//                        cra2=new CellRangeAddress(0, 0, b1,b2);
                        row.createCell(b1).setCellValue(resList.get(i).get(j));
//                        System.out.print(resList.get(i).get(j));
//                        sheet.addMergedRegion(cra2);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, b1, b2));
                    }

                } else
                    row.createCell(j).setCellValue(resList.get(i).get(j));
            }

        }
//        for(int n=0;n<taskcode.size();n++){
//            if(n==0){
//                sheet.addMergedRegion(new CellRangeAddress(0,0, 0, 6));
//            }else
//            sheet.addMergedRegion(new CellRangeAddress(0,0, n, 2));
//        }
        OutputStream fOut = response.getOutputStream();
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        return null;
    }

    /**
     * 任务报告明细导出excel
     *
     * @param id
     * @param response1
     * @return
     * @throws Exception
     */
    @RequestMapping("aa")
    public ModelAndView aa(Long id, HttpServletResponse response1) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        response1.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response1.setCharacterEncoding("utf-8");
        Taskreportinfo taskreportinfo = taskreportService.selectByPrimaryKey(id);

        String name = "";
        if (taskreportinfo.getTasktype() == 0) {
            name = "日常巡检报告_" + taskreportinfo.getTaskcode() + ".xls";
        } else if (taskreportinfo.getTasktype() == 1) {
            name = "计划巡检报告_" + taskreportinfo.getTaskcode() + ".xls";
        } else if (taskreportinfo.getTasktype() == 2) {
            name = "hse隐患排查报告_" + taskreportinfo.getTaskcode() + ".xls";
        } else if (taskreportinfo.getTasktype() == 3) {
            name = "视频巡检报告_" + taskreportinfo.getTaskcode() + ".xls";
        } else {
            name = "即拍即传报告_" + taskreportinfo.getTaskcode() + ".xls";
        }
//        String name="子任务"+taskreportinfo.getTaskcode()+".xls";
        // 传递中文参数编码
//        response1.setHeader("Content-Disposition","attachment;filename="+new String(name.getBytes("GBK"),"ISO8859_1"));


        // 定义一个工作薄
        Workbook workbook = new HSSFWorkbook();
        /*workbook.setSheetName(0,(short)0); *///这里(short)1是解决中文乱码的关键；而第一个参数是工作表的索引号。
        // 创建一个sheet页
        Sheet sheet = workbook.createSheet(name);
        // 创建一行
        Row row = sheet.createRow(0);
        /*workbook.setSheetName(1, "中文", HSSFWorkbook.ENCODING_UTF_16);*/
        // 在本行赋值 以0开始
        row.createCell(0).setCellValue("任务名称");
        row.createCell(1).setCellValue("任务编号");
        row.createCell(2).setCellValue("工人");
        row.createCell(3).setCellValue("任务类型");
        row.createCell(4).setCellValue("区域");
        row.createCell(5).setCellValue("设备");
        row.createCell(6).setCellValue("巡检项");
        row.createCell(7).setCellValue("巡检项类型");
        row.createCell(8).setCellValue("巡检项状态");

        row.createCell(9).setCellValue("正常最低值");
        row.createCell(10).setCellValue("正常最高值");
        row.createCell(11).setCellValue("下限警告值");
        row.createCell(12).setCellValue("上限警告值");
        row.createCell(13).setCellValue("数值");
        row.createCell(14).setCellValue("异常描述");
        row.createCell(15).setCellValue("数据名称");
        row.createCell(16).setCellValue("单位");

//        row.createCell(17).setCellValue("开始时间");
//        row.createCell(18).setCellValue("结束时间");
        String areaname = "";
        String equipname = "";
        String checkname = "";
        String checktype = "";
        String reportstate = "";
        String errcontent = "";
        List<String> video;
        List<String> audio;

        row = sheet.createRow(1);
        String content = "{" + "\"res\":" + taskreportinfo.getContent() + "}";
        TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
        int j = 1;
        for (TaskReportContent reportContent : res.getRes()) {
            row = sheet.createRow(j);
            areaname = reportContent.getAreaname();
            equipname = reportContent.getEquipname();
            checkname = reportContent.getCheckname();
            checktype = reportContent.getChecktype();
            String checkt = "";
            if (reportContent.getReportstate().equals("0")) {
                checkt = "正常";
            } else {
                checkt = "异常";
            }
            reportstate = checkt;
            Integer tasktype = taskreportinfo.getTasktype();
            String typename;
            if (tasktype == 0) {
                typename = "日常巡检";
            } else if (tasktype == 1) {
                typename = "计划巡检";
            } else if (tasktype == 2) {
                typename = "hse隐患排查";
            } else if (tasktype == 3) {
                typename = "视频巡检";
            } else {
                typename = "即拍即传";
            }
            row.createCell(0).setCellValue(taskreportinfo.getTaskcode().substring(0, taskreportinfo.getTaskcode().indexOf("-")));
            row.createCell(1).setCellValue(taskreportinfo.getTaskcode());
            row.createCell(2).setCellValue(taskreportinfo.getWorker());
            row.createCell(3).setCellValue(typename);
            row.createCell(4).setCellValue(areaname);
            row.createCell(5).setCellValue(equipname);
            row.createCell(6).setCellValue(checkname);
            row.createCell(7).setCellValue(checktype);
               /* row.createCell(9).setCellValue(reportContent.getAudio().size());
               row.createCell(10).setCellValue(reportContent.getVideo().size());*/
            if (checktype.equals("记录项")) {
                row.createCell(8).setCellValue("-");
                row.createCell(9).setCellValue(reportContent.getNormalmin());
                row.createCell(10).setCellValue(reportContent.getNormalmax());
                row.createCell(11).setCellValue(reportContent.getLowerwarning());
                row.createCell(12).setCellValue(reportContent.getUpperwarning());

                if (reportContent.getNumvalue() != "") {
                    if(reportContent.getLowerwarning()!=0&&reportContent.getUpperwarning()!=0){
                        if (Double.parseDouble(reportContent.getNumvalue()) > reportContent.getUpperwarning()) {
                            row.createCell(13).setCellValue( reportContent.getNumvalue() + "↑↑");
                            row.createCell(14).setCellValue("↑↑");
                        }
                        if (Double.parseDouble(reportContent.getNumvalue()) < reportContent.getLowerwarning()) {
                            row.createCell(13).setCellValue( reportContent.getNumvalue() + "↓↓");
                            row.createCell(14).setCellValue("↓↓");
                        }
                        if (reportContent.getLowerwarning() <= Double.parseDouble(reportContent.getNumvalue()) &&
                                Double.parseDouble(reportContent.getNumvalue()) <= reportContent.getUpperwarning()) {
                            row.createCell(13).setCellValue( reportContent.getNumvalue());
                            row.createCell(14).setCellValue("-");
                        }
                    }else{
                        if (Double.parseDouble(reportContent.getNumvalue()) > reportContent.getNormalmax()) {
                            row.createCell(13).setCellValue( reportContent.getNumvalue() + "↑↑");
                            row.createCell(14).setCellValue("↑↑");
                        }
                        if (Double.parseDouble(reportContent.getNumvalue()) < reportContent.getNormalmin()) {
                            row.createCell(13).setCellValue( reportContent.getNumvalue() + "↓↓");
                            row.createCell(14).setCellValue("↓↓");
                        }
                        if (reportContent.getNormalmin() <= Double.parseDouble(reportContent.getNumvalue()) &&
                                Double.parseDouble(reportContent.getNumvalue()) <= reportContent.getNormalmax()) {
                            row.createCell(13).setCellValue( reportContent.getNumvalue());
                            row.createCell(14).setCellValue("-");
                        }
                    }
                }

                row.createCell(15).setCellValue(reportContent.getRecordname());
                row.createCell(16).setCellValue(reportContent.getUnitname());

//                if (taskreportinfo.getStarttime() != null) {
//                    row.createCell(17).setCellValue(sdf.format(taskreportinfo.getStarttime()));
//                }
//                if (taskreportinfo.getEndtime() != null) {
//                    row.createCell(18).setCellValue(sdf.format(taskreportinfo.getEndtime()));
//                }
            } else {
                row.createCell(8).setCellValue(reportstate);

                row.createCell(9).setCellValue("-");
                row.createCell(10).setCellValue("-");
                row.createCell(11).setCellValue("-");
                row.createCell(12).setCellValue("-");

                row.createCell(13).setCellValue(reportstate);
                if(reportContent.getReportstate().equals("1")){
                    row.createCell(14).setCellValue(reportContent.getErrcontent() + "," + JsonUtil.toJSON(reportContent.getImg()) + "," + JsonUtil.toJSON(reportContent.getAudio()) + "," + JsonUtil.toJSON(reportContent.getVideo()));
                }else{
                    row.createCell(14).setCellValue(reportstate);
                }
                row.createCell(15).setCellValue("-");
                row.createCell(16).setCellValue("-");
//                if (taskreportinfo.getStarttime() != null) {
//                    row.createCell(17).setCellValue(sdf.format(taskreportinfo.getStarttime()));
//                }
//                if (taskreportinfo.getEndtime() != null) {
//                    row.createCell(18).setCellValue(sdf.format(taskreportinfo.getEndtime()));
//                }
            }
            j++;
        }
       /* }*/
        response1.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("GBK"), "ISO8859_1"));
        OutputStream fOut = response1.getOutputStream();
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        return null;
    }

    /**
     * 设备状态情况列表
     *
     * @param m
     * @param page
     * @param monthstr
     * @param checkid
     * @param siteid
     * @param areaid
     * @param equipid
     * @param sitename
     * @param areaname
     * @param equipname
     * @param checkname
     * @return
     * @throws Exception
     */
    @RequestMapping("equipstateinfo")
    public String equipstateinfo(Model m, int page, String monthstr, String checkid, String siteid, String areaid,
                                 String equipid, String sitename, String areaname, String equipname, String checkname,
                                 Integer needline, HttpServletRequest request) throws Exception {
        if (page < 0) {
            page = 1;
        }
//        if(areaname=="所有区域"){
//            areaname="";
//        }
//        if(checkname=="所有巡检项"){
//            checkname="";
//        }
//        if(equipname=="所有设备"){
//            equipname="";
//        }
        PageInfo<Reportcontent> equipstates = taskreportService.equipSateInfo2(page, 7, checkname, monthstr, sitename, areaname, equipname);
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        //查询所有厂区
        List<Siteareainfo> sites = siteService.selectByExample2(admininfo.getSiteid());
        //查询所有区域
        List<Areainfo> areainfos = areaService.selectByExample();
        //查询所有设备
        EquipmentinfoExample equipmentinfoExample = new EquipmentinfoExample();
        List<Equipmentinfo> equipmentinfos = equipmentService.selectByExample(equipmentinfoExample);
        //查询所有巡检项
        List<Checkiteminfo> checkiteminfos = checkInfoService.selectByExample();

        m.addAttribute("equipstates", equipstates);
        m.addAttribute("sites", sites);
        m.addAttribute("areainfos", areainfos);
        m.addAttribute("equipmentinfos", equipmentinfos);
        m.addAttribute("checkiteminfos", checkiteminfos);

        m.addAttribute("monthstr", monthstr);
        m.addAttribute("siteid", siteid);
        m.addAttribute("areaid", areaid);
        m.addAttribute("equipid", equipid);
        m.addAttribute("checkid", checkid);
        m.addAttribute("checkname", checkname);
        m.addAttribute("needline", needline);
        m.addAttribute("sitename", sitename);
        m.addAttribute("areaname", areaname);
        m.addAttribute("equipname", equipname);

        return "showtaskplan6";
    }

    /**
     * 设备状态情况折线统计
     *
     * @param m
     * @param page
     * @param monthstr
     * @param siteid
     * @param areaid
     * @param equipid
     * @param sitename
     * @param areaname
     * @param equipname
     * @param checkname
     * @return
     * @throws Exception
     */
    @RequestMapping("equipstatechart")
    @ResponseBody
    public Result equipstatechart(Model m, int page, String monthstr, String siteid, String areaid,
                                  String equipid, String sitename, String areaname, String equipname,
                                  String checkname) throws Exception {
        if (page <= 0) {
            page = 1;
        }
        WebChartRes2 chartRes2 = null;
        if (monthstr != "" && monthstr != null && checkname != null && checkname != "") {
            chartRes2 = taskreportService.equipStateChart(page, 7, checkname, monthstr, areaname, equipname);
        } else
            chartRes2 = null;
        m.addAttribute("siteid", siteid);
        m.addAttribute("areaid", areaid);
        m.addAttribute("equipid", equipid);
        m.addAttribute("sitename", sitename);
        m.addAttribute("areaname", areaname);
        m.addAttribute("equipname", equipname);
        m.addAttribute("checkname", checkname);
        m.addAttribute("monthstr", monthstr);
        return new JSONResult<>(chartRes2);
    }


    @RequestMapping("getchecktask")
    @ResponseBody
    public List<Checkiteminfo> getchecktask(Long equipid) {
        if (equipid == null)
            return checkInfoService.selectByExample();
        List<Long> taskids = new ArrayList<>();
        List<Long> checkids = new ArrayList<>();
        EquipmentTaskExample equipmentTaskExample = new EquipmentTaskExample();
        equipmentTaskExample.createCriteria().andEquipmentidEqualTo(equipid);
        List<EquipmentTask> equipmentTasks = equipmentTaskMapper.selectByExample(equipmentTaskExample);
        for (EquipmentTask equiptask : equipmentTasks) {
            taskids.add(equiptask.getTaskid());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskids", taskids);
        List<CheckitemTask> checkitemTasks = checkitemTaskMapper.getcheckid(map);
        for (CheckitemTask checktask : checkitemTasks) {
            checkids.add(checktask.getCheckitemid());
        }
        map.put("checkids", checkids);
        return checkInfoService.getcheckinfo(map);
    }

    @RequestMapping("dadiyreport")
    public String dadiyreport(Integer page1, Integer page2, String date1, Model m, Long taskidstr) {
        if (page1 == null || page1 <= 0) {
            page1 = 1;
        }
        if (page2 == null || page2 <= 0) {
            page2 = 1;
        }
        List<Tasktempinfo> tasktempinfos = taskreportService.dadiyreport(date1, taskidstr);
        List<Tasktempinfo> errortasktemp = new ArrayList<>();
        int num0 = 0;//总任务数
        int num1 = 0; //正常完成数
        int num2 = 0;//漏检数
        int num3 = 0;//超时数

        int num4 = 0;//发现异常数
        int num5 = 0;//总巡检项数
        int num6 = 0;//已完成数
        int num7 = 0;//未完成数
        num0 = tasktempinfos.size();
        for (Tasktempinfo item0 : tasktempinfos) {
            if (item0.getState() != 2 && item0.getOperationstate() != 3) {
                errortasktemp.add(item0);
            }
        }
        for (Tasktempinfo item : tasktempinfos) {
            if (item.getState() == 2 && item.getOperationstate() == 3) {
                num1++;
            }
            if (item.getState() == 2 && item.getOperationstate() == 1) {
                num2++;
            }
            if (item.getState() == 2 && item.getOperationstate() == 4) {
                num3++;
            }
        }
        List<Taskreportinfo> taskreportinfos = taskreportService.dadiyreportlist(taskidstr);
        List<Long> reportid = new ArrayList<>();
        List<Reportcontent> errorcontents = new ArrayList<>();
        List<Reportcontent> reportcontents = new ArrayList<>();
        if (taskreportinfos.size() > 0) {
            for (Taskreportinfo content : taskreportinfos) {
                reportid.add(content.getId());
            }
            ReportcontentExample reportcontentExample = new ReportcontentExample();
            reportcontentExample.createCriteria().andOperationtimeEqualTo(date1).andReportidIn(reportid);
            reportcontents = reportcontentMapper.selectByExample(reportcontentExample);

            for (Reportcontent item : reportcontents) {
                if (item.getAreaskip() == 0 && item.getEquipmentskip() == 0) {
                    num6++;
                } else {
                    num7++;
                }
                if (item.getReportstate().equals("1")) {
                    num4++;
                    errorcontents.add(item);
                }
            }
        }
        num5 = reportcontents.size();
        m.addAttribute("num0", num0);
        m.addAttribute("num1", num1);
        m.addAttribute("num2", num2);
        m.addAttribute("num3", num3);
        m.addAttribute("num4", num4);
        m.addAttribute("num5", num5);
        m.addAttribute("num6", num6);
        m.addAttribute("num7", num7);
        m.addAttribute("date1", date1);
        m.addAttribute("page1", page1);
        m.addAttribute("page2", page2);
        m.addAttribute("taskidstr", taskidstr);
        PageUtil temppagebean = new PageUtil(errorcontents.size());//初始化PageBean对象
        PageUtil taskpagebean = new PageUtil(errortasktemp.size());
        List<Reportcontent> courseList = new ArrayList<>();
        List<Tasktempinfo> errortasklist = new ArrayList<>();
        if (page1 != null) {
            temppagebean.setCurPage(page1); //这里page是从页面上获取的一个参数，代表页数
            int pagesize = temppagebean.getPageSize();
            //获得分页数据在list集合中的索引
            int firstIndex = (page1 - 1) * pagesize;
            int toIndex = page1 * pagesize;
            if (toIndex > errorcontents.size()) {
                toIndex = errorcontents.size();
            }
            if (firstIndex > toIndex) {
                firstIndex = 0;
                temppagebean.setCurPage(1);
            }
            //截取数据集合，获得分页数据
            courseList = errorcontents.subList(firstIndex, toIndex);
        }
        if (page2 != null) {
            taskpagebean.setCurPage(page2); //这里page是从页面上获取的一个参数，代表页数
            int pagesize = taskpagebean.getPageSize();
            //获得分页数据在list集合中的索引
            int firstIndex = (page2 - 1) * pagesize;
            int toIndex = page2 * pagesize;
            if (toIndex > errortasktemp.size()) {
                toIndex = errortasktemp.size();
            }
            if (firstIndex > toIndex) {
                firstIndex = 0;
                taskpagebean.setCurPage(1);
            }
            //截取数据集合，获得分页数据
            errortasklist = errortasktemp.subList(firstIndex, toIndex);
        }
        m.addAttribute("courseList", courseList);
        m.addAttribute("errortasklist", errortasklist);
        m.addAttribute("temppagebean", temppagebean);
        m.addAttribute("taskpagebean", taskpagebean);
        return "dadiyreport";
    }

    @RequestMapping("weekreport")
    public String weekreport(Integer page1, Integer page2, String date1, String date2, Model m, int type, Long taskidstr) {
        if (page1 == null || page1 <= 0) {
            page1 = 1;
        }
        if (page2 == null || page2 <= 0) {
            page2 = 1;
        }
        List<Tasktempinfo> tasktempinfos = taskreportService.dadiyreport2(date1, date2, taskidstr);
        List<Tasktempinfo> errortasktemp = new ArrayList<>();
        int num0 = 0;//总任务数
        int num1 = 0; //正常完成数
        int num2 = 0;//漏检数
        int num3 = 0;//超时数
        int num4 = 0;//发现异常数
        int num5 = 0;//总巡检项数
        int num6 = 0;//已完成数
        int num7 = 0;//未完成数
        num0 = tasktempinfos.size();
        for (Tasktempinfo item0 : tasktempinfos) {
            if (item0.getState() != 2 && item0.getOperationstate() != 3) {
                errortasktemp.add(item0);
            }
        }
        for (Tasktempinfo item : tasktempinfos) {
            if (item.getState() == 2 && item.getOperationstate() == 3) {
                num1++;
            }
            if (item.getState() == 2 && item.getOperationstate() == 1) {
                num2++;
            }
            if (item.getState() == 2 && item.getOperationstate() == 4) {
                num3++;
            }
        }
        List<Taskreportinfo> taskreportinfos = taskreportService.dadiyreportlist(taskidstr);
        List<Long> reportid = new ArrayList<>();
        for (Taskreportinfo content : taskreportinfos) {
            reportid.add(content.getId());
        }
        List<Reportcontent> reportcontents = new ArrayList<>();
        List<Reportcontent> errorcontents = new ArrayList<>();
        if (taskreportinfos.size() > 0) {
            ReportcontentExample reportcontentExample = new ReportcontentExample();
            reportcontentExample.createCriteria().andOperationtimeBetween(date1, date2).andReportidIn(reportid);
            reportcontents = reportcontentMapper.selectByExample(reportcontentExample);
            for (Reportcontent item : reportcontents) {
                if (item.getAreaskip() == 0 && item.getEquipmentskip() == 0) {
                    num6++;
                } else {
                    num7++;
                }
                if (item.getReportstate().equals("1")) {
                    num4++;
                    errorcontents.add(item);
                }
            }
        }
        num5 = reportcontents.size();
        m.addAttribute("num0", num0);
        m.addAttribute("num1", num1);
        m.addAttribute("num2", num2);
        m.addAttribute("num3", num3);
        m.addAttribute("num4", num4);
        m.addAttribute("num5", num5);
        m.addAttribute("num6", num6);
        m.addAttribute("num7", num7);
        m.addAttribute("date1", date1);
        m.addAttribute("date2", date2);
        m.addAttribute("type", type);
        m.addAttribute("page1", page1);
        m.addAttribute("page2", page2);
        m.addAttribute("taskidstr", taskidstr);
        m.addAttribute("errorcontents", errorcontents);
        m.addAttribute("errortasktemp", errortasktemp);
        PageUtil temppagebean = new PageUtil(errorcontents.size());//初始化PageBean对象
        PageUtil taskpagebean = new PageUtil(errortasktemp.size());
        List<Reportcontent> courseList = new ArrayList<>();
        List<Tasktempinfo> errortasklist = new ArrayList<>();
        if (page1 != null) {
            temppagebean.setCurPage(page1); //这里page是从页面上获取的一个参数，代表页数
            int pagesize = temppagebean.getPageSize();
            //获得分页数据在list集合中的索引
            int firstIndex = (page1 - 1) * pagesize;
            int toIndex = page1 * pagesize;
            if (toIndex > errorcontents.size()) {
                toIndex = errorcontents.size();
            }
            if (firstIndex > toIndex) {
                firstIndex = 0;
                temppagebean.setCurPage(1);
            }
            //截取数据集合，获得分页数据
            courseList = errorcontents.subList(firstIndex, toIndex);
        }
        if (page2 != null) {
            taskpagebean.setCurPage(page2); //这里page是从页面上获取的一个参数，代表页数
            int pagesize = taskpagebean.getPageSize();
            //获得分页数据在list集合中的索引
            int firstIndex = (page2 - 1) * pagesize;
            int toIndex = page2 * pagesize;
            if (toIndex > errortasktemp.size()) {
                toIndex = errortasktemp.size();
            }
            if (firstIndex > toIndex) {
                firstIndex = 0;
                taskpagebean.setCurPage(1);
            }
            //截取数据集合，获得分页数据
            errortasklist = errortasktemp.subList(firstIndex, toIndex);
        }
        m.addAttribute("courseList", courseList);
        m.addAttribute("errortasklist", errortasklist);
        m.addAttribute("temppagebean", temppagebean);
        m.addAttribute("taskpagebean", taskpagebean);
        if (type == 0) {
            return "weekreport";
        } else
            return "monthreport";
    }

}
