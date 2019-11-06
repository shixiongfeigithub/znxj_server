package com.niule.znxj.web.controller;

import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.core.util.json.JsonUtil;
import com.niule.znxj.web.dao.ReportcontentMapper;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuqb
 * Date: 2019-11-01 10:33
 */
@Controller
public class TaskExceptionController {

    @Resource
    private AdmininfoService admininfoService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private AreaService areaService;

    @Resource
    private EquipmentService equipmentService;

    @Autowired
    private TaskreportService taskreportService;

    @Autowired
    private TaskPlanService taskPlanService;

    @Resource
    private ReportcontentMapper reportcontentMapper;

    @Resource
    private SystemService systemService;

    @Autowired
    private ExceptionhandlerinfoService exceptionhandlerinfoService;

    protected PageBean pageBean = new PageBean();


    /**
     * 分页显示所有任务异常的报告 - 列表
     */
    @RequestMapping("/showexceptionreport")
    public String showexceptionreport(Model m, int page,String taskAcode,Long siteid,Integer exceptionstate,
                                 String worker, String time1, String time2,HttpServletRequest request) {

        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> siteareainfos = null;
        List<Long> siteids = new ArrayList<>();
        if(siteid==null){
            if (admininfo.getSiteid() == null) {
                List<Siteareainfo> sites = siteService.queryAllSite();
                for (Siteareainfo siteareainfo : sites) {
                    siteids.add(siteareainfo.getId());
                }
            } else {
                siteids.add(Long.valueOf(admininfo.getSiteid()));
            }
            siteareainfos = siteService.selectByExample3(siteids); //查询角色对应的所有厂区
        }else{
            siteids.add(siteid);
        }

        if (page <= 0) {
            page = 1;
        }
        page = PageBean.countCurrentPage(page);
        long totalpage = 0;
        int pagesize = 15;

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("page", (page - 1) * pagesize);
        map.put("pagesize", pagesize);
        List<Integer> tasktypeList = new ArrayList<>();
        tasktypeList.add(0); //日常巡检
        tasktypeList.add(1); //计划巡检
        tasktypeList.add(3); //视频巡检
        map.put("tasktypes",tasktypeList);//任务类型
        map.put("taskAcode", taskAcode); //任务号(T3000A)
        map.put("reportstate", 1); //异常报告
        map.put("time1", time1);//开始时间
        map.put("time2", time2);//结束时间
        map.put("siteids", siteids);//厂区
        map.put("worker",worker); //巡检责任人
        map.put("exceptionstate",exceptionstate);

        List<Taskreportinfo> taskreportinfos = null;
        int rows = 0;
        //判断用户角色
        if(admininfo.getRoleid()==3){ //操作员
            //查询操作员为该用户的异常任务
            taskreportinfos = taskreportService.findByPageReport4(map);
            rows = taskreportService.countReport4(map);
        }else{
            taskreportinfos = taskreportService.findByPageReport3(map);
            rows = taskreportService.countReport3(map);
        }
        totalpage = PageBean.counTotalPage(pagesize, rows);
        pageBean.setList(taskreportinfos);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean", pageBean);
        m.addAttribute("taskAcode", taskAcode);
        m.addAttribute("siteid", siteid);
        m.addAttribute("worker",worker);
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        m.addAttribute("exceptionstate", exceptionstate);
        m.addAttribute("sites", siteareainfos);
        m.addAttribute("roleid", admininfo.getRoleid());
        return "showexceptionreport";
    }

    /**
     * 异常任务报告的详细信息
     *
     * @param reportid   任务报告编号
     * @return
     */
    @RequestMapping("/showexceptiondetail")
    public String showexceptiondetail(Model m, int page, Long reportid,Long areaid,Long equipmentid) throws Exception {
        //得到报告明细
        Taskreportinfo taskreportinfo = taskreportService.selectByPrimaryKey(reportid);
        Taskplaninfo taskplaninfo = taskPlanService.selectByPrimaryKey(taskreportinfo.getTaskid());
        List<Areainfo> areainfoList = areaService.selectByExample1(taskplaninfo.getSiteid().intValue());
        Areainfo areainfo = areaService.selectByPrimaryKey(areaid);
        Equipmentinfo equipmentinfo = equipmentService.selectByPrimaryKey(equipmentid);
        //根据任务报告ID得到所有报告内容reportcontent
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("reportid", reportid);
        map.put("areaname", areainfo.getCustomid());
        map.put("equipname",equipmentinfo.getName());
        List<Reportcontent> reportcontents = reportcontentMapper.selectByExample2(map);
        List<Reportsetting> reportsettings = systemService.showReportSetting();
        for (Reportsetting reportsetting : reportsettings) {
            m.addAttribute(reportsetting.getName(), reportsetting.getIsshow());
        }
        m.addAttribute("reportinfos", reportcontents);
        m.addAttribute("reportid", reportid);
        m.addAttribute("taskcode", taskreportinfo.getTaskcode());
        m.addAttribute("taskreportinfo", taskreportinfo);
        m.addAttribute("areaid",areaid);
        m.addAttribute("areainfos",areainfoList);
        m.addAttribute("equipmentid",equipmentid);
        m.addAttribute("page", page);
        return "exceptionreportdetail";
    }

    public List<Taskreportinfo> getTaskCode(String taskCode) throws Exception {
        String code = taskCode.substring(0, taskCode.lastIndexOf("-"));
        //得到当前子任务是第几次执行
        Integer i = Integer.valueOf(taskCode.substring(taskCode.lastIndexOf("-") + 1));
        String oldTaskCode = "";
        if (i != 1) {
            oldTaskCode = code + "-" + (i - 1);
        } else {
            //如果当前子任务是第一次执行，获取到昨天当前任务最后一次执行的任务报告
            int index = taskCode.indexOf("-");
//            //根据第一个点的位置 获得第二个点的位置
            index = taskCode.indexOf("-", index + 1);
            int taskNameIndex = taskCode.indexOf("-");
            String taskName = taskCode.substring(0, taskNameIndex);
            String time = taskCode.substring(index + 1, taskCode.lastIndexOf("-"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(time);
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);
            String time2 = sdf.format(now.getTime());
            TaskreportinfoExample example = new TaskreportinfoExample();
            example.createCriteria().andTaskcode2Like(taskName + "%" + time2 + "%");
            List<Taskreportinfo> taskreportinfos = taskreportService.selectByExample(example);
            Integer k = null;
            String taskversion = "";
            if (taskreportinfos.size() > 0) {
                String newTaskCode = taskreportinfos.get(0).getTemp().getTaskcode();
                int aa = newTaskCode.indexOf("-");
                int bb = newTaskCode.indexOf("-", aa + 1);
                taskversion = newTaskCode.substring(aa + 1, bb);//昨天的任务版本
                List<Integer> counts = new ArrayList<>();
                for (Taskreportinfo taskreportinfo : taskreportinfos) {
                    String taskcode2 = taskreportinfo.getTemp().getTaskcode();
                    Integer j = Integer.valueOf(taskcode2.substring(taskcode2.lastIndexOf("-") + 1));
                    counts.add(j);
                }
                k = Collections.max(counts);
            }
            if (k == null)
                oldTaskCode = null;
            else
                oldTaskCode = taskName + "-" + taskversion + "-" + time2 + "-" + k;//昨天最后一个子任务
        }
        List<Taskreportinfo> taskreportinfos = new ArrayList<>();
        if (oldTaskCode != null) {
            TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
            taskreportinfoExample.createCriteria().andTaskcode2EqualTo(oldTaskCode);
            taskreportinfos = taskreportService.selectByExample(taskreportinfoExample);
        }
        return taskreportinfos;
    }


    /*跳转到关闭异常巡检任务页面*/
    @RequestMapping("/toclosetaskreport")
    @RequiresPermissions("item:closereport")
    public String toclosetaskreport(Model m, Long reportid,Long tempid, HttpServletRequest request) {
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        Taskreportinfo taskreportinfo = taskreportService.selectByPrimaryKey(reportid);
        m.addAttribute("reportid", reportid);
        m.addAttribute("tempid", tempid);
        m.addAttribute("taskcode",taskreportinfo.getTaskcode());
        return "closetaskreport";
    }

    @RequestMapping("/closetaskreport")
    @ResponseBody
    public int closetaskreport(Exceptionhandlerinfo exceptionhandlerinfo,HttpServletRequest request){
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        ExceptionhandlerinfoExample example = new ExceptionhandlerinfoExample();
        example.createCriteria().andReportidEqualTo(exceptionhandlerinfo.getReportid());
        List<Exceptionhandlerinfo> infoList = exceptionhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
           Exceptionhandlerinfo info = infoList.get(0);
           if (info.getExceptionclosetime()!=null)
                return 0;

            List<String> attach=new ArrayList<>();
            String [] stringArr= exceptionhandlerinfo.getAttachment().split(",");
            for(int i=0;i<stringArr.length;i++){
                attach.add(stringArr[i]);
            }
            BeanUtils.copyProperties(exceptionhandlerinfo,info);
            exceptionhandlerinfo.setAttachment(JsonUtil.toJSON(attach));
            exceptionhandlerinfo.setCheckuserid(admininfo.getId());
            exceptionhandlerinfo.setExceptionclosetime(new Date());
            int result=exceptionhandlerinfoService.updateByExample(exceptionhandlerinfo,example);
            return result;
        }
        return -1;
    }

    /*异常巡检任务指定责任人页面*/
    @RequestMapping("/toassignprincipal")
    @RequiresPermissions("item:assignprincipal")
    public String toassignprincipal(Model m, Long reportid,Long tempid, HttpServletRequest request) {
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");

        List<Admininfo> operationuserList = admininfoService.selectByRoleId(3); //查询所有角色为操作员的用户
        Taskreportinfo taskreportinfo = taskreportService.selectByPrimaryKey(reportid);
        for (Admininfo user : operationuserList){
            if (user.getId()==admininfo.getId()){
                operationuserList.remove(user);
                break;
            }
        }
        m.addAttribute("reportid", reportid);
        m.addAttribute("tempid", tempid);
        m.addAttribute("taskcode",taskreportinfo.getTaskcode());
        m.addAttribute("operationuserList",operationuserList);
        return "assignprincipal";
    }

    @RequestMapping("/assignprincipal")
    @ResponseBody
    public int assignprincipal(Exceptionhandlerinfo exceptionhandlerinfo,HttpServletRequest request){
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        ExceptionhandlerinfoExample example = new ExceptionhandlerinfoExample();
        example.createCriteria().andReportidEqualTo(exceptionhandlerinfo.getReportid());
        List<Exceptionhandlerinfo> infoList = exceptionhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
            Exceptionhandlerinfo info = infoList.get(0);
            if(info.getAppointedtime()==null){
                exceptionhandlerinfo.setCheckuserid(admininfo.getId());
                exceptionhandlerinfo.setAppointedtime(new Date());
            }

            BeanUtils.copyProperties(exceptionhandlerinfo,info);
            int result=exceptionhandlerinfoService.updateByExample(exceptionhandlerinfo,example);
            return result;
        }
        return 0;
    }

    public PageBean getPageBean() {
        return pageBean;
    }
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
