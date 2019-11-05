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
    public String showexceptionreport(Model m, int page,String taskcode,Long siteid,String operationstate,
                                 String worker, String time1, String time2,HttpServletRequest request) {

        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        List<Long> siteids = new ArrayList<>();
        if (admininfo.getSiteid() == null) {
            List<Siteareainfo> sites = siteService.queryAllSite();
            for (Siteareainfo siteareainfo : sites) {
                siteids.add(siteareainfo.getId());
            }
        } else {
            siteids.add(Long.valueOf(admininfo.getSiteid()));
        }
        List<Siteareainfo> siteareainfos = siteService.selectByExample3(siteids); //查询角色对应的所有厂区
        //List<Areainfo> areainfos = areaService.selectByExample1(siteid);
        //List<Equipmentinfo> equipmentinfos = equipmentService.queryequip(areaid);

        //判断用户角色
        if(admininfo.getRoleid()==3){ //操作员
            //查询操作员为该用户的异常任务
        }

        if (page <= 0) {
            page = 1;
        }
        page = PageBean.countCurrentPage(page);
        List<Taskreportinfo> taskreportinfos = null;
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
        map.put("taskcode", taskcode); //任务号
        map.put("reportstate", 1); //异常报告
        map.put("time1", time1);//开始时间
        map.put("time2", time2);//结束时间
        map.put("siteids", siteids);//厂区
        map.put("worker",worker); //任务执行者

        List<Integer> stateList = new ArrayList<>();
            if (operationstate!=null && operationstate.equals("5")) {
                stateList.add(3);
                map.put("stopstate", 1);
                map.put("operationstate", null);
            } else {
                if (operationstate==null) {
                    stateList.add(3);
                    stateList.add(2);
                } else if (operationstate.equals("4")) {
                    stateList.add(3);
                } else
                    stateList.add(2);
                map.put("operationstate", operationstate);
            }

            map.put("state", stateList);
            taskreportinfos = taskreportService.findByPageReport3(map);
            int rows2 = taskreportService.countReport3(map);
            totalpage = PageBean.counTotalPage(pagesize, rows2);



        pageBean.setList(taskreportinfos);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean", pageBean);
        m.addAttribute("taskcode", taskcode);
        m.addAttribute("siteid", siteid);
        m.addAttribute("worker",worker);
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        m.addAttribute("operationstate", operationstate);
        m.addAttribute("sites", siteareainfos);
        m.addAttribute("roleid", admininfo.getRoleid());
        //ShowReportParam param = new ShowReportParam(page, tasktype, taskcode, "1", time1, time2, operationstate, siteid, tasktype);
        //request.getSession().setAttribute("reportParam", param);
        return "showexceptionreport";
    }

    /**
     * 异常任务报告的详细信息
     *
     * @param id   任务报告编号
     * @param m
     * @param type 任务类型
     * @return
     */
    @RequestMapping("/showexceptiondetail")
    public String showexceptiondetail(Long id, Model m, int type, int page, Integer type2) throws Exception {
        //得到报告明细
        Taskreportinfo taskreportinfo = taskreportService.selectByPrimaryKey(id);
        //得到前一次报告
        List<Taskreportinfo> taskreportinfos = getTaskCode(taskreportinfo.getTaskcode());
        List<Reportcontent> reportcontentList = new ArrayList<>();
        if (taskreportinfos.size() > 0) {
            if (taskreportinfos.get(0).getId() != null) {
                ReportcontentExample example = new ReportcontentExample();
                example.createCriteria().andReportidEqualTo(taskreportinfos.get(0).getId());
                reportcontentList = reportcontentMapper.selectByExample(example);
            }
        }

        //根据任务报告ID得到所有报告内容reportcontent
        List<Reportcontent> reportcontents = reportcontentMapper.selectByExample2(id);
        //得到波动值
        List<Systemsettinginfo> systems = systemService.selectByExample();
        Double fluctudte = 0.0;
        for (Systemsettinginfo info : systems) {
            m.addAttribute(info.getKeyname(), info.getValue());
            if (info.getKeyname().equals("FLUCTUATE"))
                fluctudte = Double.valueOf(info.getValue());
        }
        Iterator<Reportcontent> iterator = reportcontents.iterator();
        while (iterator.hasNext()){
            Reportcontent reportcontent = iterator.next();
            String bodongzhi = "";
            if (reportcontentList.size() > 0 ) {
                for (Reportcontent reportcontent2 : reportcontentList) {
                    if (reportcontent2.getCheckname().equals(reportcontent.getCheckname())) {
                        if (taskreportinfo.getExamstate() != 0) {
                            if (reportcontent.getCheckvalue() != null && !reportcontent.getCheckvalue().isEmpty() && reportcontent2.getCheckvalue() != null && !reportcontent2.getCheckvalue().isEmpty()) {
                                double maxfluctudte = 1 + fluctudte;
                                double minfluctudte = 1 - fluctudte;
                                if (Double.parseDouble(reportcontent.getCheckvalue()) / Double.parseDouble(reportcontent2.getCheckvalue()) > maxfluctudte) {
                                    bodongzhi = "/↑↑↑";
                                }
                                if (Double.parseDouble(reportcontent.getCheckvalue()) / Double.parseDouble(reportcontent2.getCheckvalue()) < minfluctudte) {
                                    bodongzhi = "/↓↓↓";
                                }
                            }
                        } else {
                            if (reportcontent.getNumvalue() != null && !reportcontent.getNumvalue().isEmpty() && reportcontent2.getCheckvalue() != null && !reportcontent2.getCheckvalue().isEmpty()) {
                                if (Double.parseDouble(reportcontent.getNumvalue()) / Double.parseDouble(reportcontent2.getCheckvalue()) > (1 + fluctudte)) {
                                    bodongzhi = "/↑↑↑";
                                }
                                if (Double.parseDouble(reportcontent.getNumvalue()) / Double.parseDouble(reportcontent2.getCheckvalue()) < (1 - fluctudte)) {
                                    bodongzhi = "/↓↓↓";
                                }
                            }
                        }
                    }
                }
            }
            if (taskreportinfo.getExamstate() != 0 && reportcontent.getCheckvalue() != null && !reportcontent.getCheckvalue().isEmpty()) {
                //大于低值 小于高值
                if (!reportcontent.getNormalmin().equals("-") && !reportcontent.getNormalmax().equals("-") &&
                        Double.parseDouble(reportcontent.getNormalmin()) <= Double.parseDouble(reportcontent.getCheckvalue()) &&
                        Double.parseDouble(reportcontent.getCheckvalue()) <= Double.parseDouble(reportcontent.getNormalmax())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else{
                        reportcontent.setErrcontent("-" + bodongzhi);
                        iterator.remove();
                    }

                }
                //大于最低值  小于低值
                else if (!reportcontent.getNormalmin().equals("-")
                        && Double.parseDouble(reportcontent.getCheckvalue()) < Double.parseDouble(reportcontent.getNormalmin())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else reportcontent.setErrcontent("↓" + bodongzhi);
                }
                //大于高值  小于最高值
                else if (!reportcontent.getNormalmax().equals("-")
                        && Double.parseDouble(reportcontent.getCheckvalue()) > Double.parseDouble(reportcontent.getNormalmax())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else reportcontent.setErrcontent("↑" + bodongzhi);
                }
                //大于最高上限值
                else if (!reportcontent.getUpperwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getCheckvalue()) > Double.parseDouble(reportcontent.getUpperwarning())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else
                        reportcontent.setErrcontent("↑↑" + bodongzhi);
                }
                //小于最低上限值
                else if (!reportcontent.getLowerwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getCheckvalue()) < Double.parseDouble(reportcontent.getLowerwarning())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else
                        reportcontent.setErrcontent("↓↓" + bodongzhi);
                } else {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else {
                        reportcontent.setErrcontent("-" + bodongzhi);
                    }
                }
            } else if (taskreportinfo.getExamstate() == 0 && !reportcontent.getNumvalue().isEmpty()) {
                if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                    reportcontent.setErrcontent(reportcontent.getErrcontent());
                } else {
                    reportcontent.setErrcontent("-");
                    iterator.remove();
                }
                //大于低值 小于高值
                if (!reportcontent.getNormalmin().equals("-") && !reportcontent.getNormalmax().equals("-") &&
                        Double.parseDouble(reportcontent.getNormalmin()) <= Double.parseDouble(reportcontent.getNumvalue()) &&
                        Double.parseDouble(reportcontent.getNumvalue()) <= Double.parseDouble(reportcontent.getNormalmax())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else {
                        reportcontent.setErrcontent("-" + bodongzhi);
                        iterator.remove();
                    }
                }
                // 小于低值
                if (!reportcontent.getNormalmin().equals("-")
                        && Double.parseDouble(reportcontent.getNumvalue()) < Double.parseDouble(reportcontent.getNormalmin())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else reportcontent.setErrcontent("↓" + bodongzhi);
                }
                //大于高值
                if (!reportcontent.getNormalmax().equals("-")
                        && Double.parseDouble(reportcontent.getNumvalue()) > Double.parseDouble(reportcontent.getNormalmax())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else reportcontent.setErrcontent("↑" + bodongzhi);
                }
                //大于最高上限值
                if (!reportcontent.getUpperwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getNumvalue()) > Double.parseDouble(reportcontent.getUpperwarning())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else reportcontent.setErrcontent("↑↑" + bodongzhi);
                }
                //小于最低上限值
                if (!reportcontent.getLowerwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getNumvalue()) < Double.parseDouble(reportcontent.getLowerwarning())) {
                    if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                        reportcontent.setErrcontent(reportcontent.getErrcontent());
                    } else reportcontent.setErrcontent("↓↓" + bodongzhi);
                }

            } else {
                if (taskreportinfo.getReportstate() == 1 && reportcontent.getErrcontent() != null) {
                    reportcontent.setErrcontent(reportcontent.getErrcontent());
                } else {
                    reportcontent.setErrcontent("-");
                    iterator.remove();
                }
            }
        }
        m.addAttribute("reportinfos", reportcontents);
        m.addAttribute("type", type);
        m.addAttribute("taskreportid", id);
        m.addAttribute("taskcode", taskreportinfo.getTaskcode());
        Date now = new Date();
        if (taskreportinfos.size() > 0) {
            m.addAttribute("taskreport", taskreportinfos.get(0));
        } else {
            m.addAttribute("taskreport", null);
        }
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long diff = now.getTime() - taskreportinfo.getDonetime().getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        long hours = day * 24 + hour;
        if (hours <= 36) {
            if (taskreportinfo.getExamstate() == 0)
                m.addAttribute("examtime", 0);
            else
                m.addAttribute("examtime", 1);
        }

        List<Reportsetting> reportsettings = systemService.showReportSetting();
        for (Reportsetting reportsetting : reportsettings) {
            m.addAttribute(reportsetting.getName(), reportsetting.getIsshow());
        }
        m.addAttribute("taskreportinfo", taskreportinfo);
        m.addAttribute("page", page);
        m.addAttribute("type2", type2);
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
            exceptionhandlerinfo.setCheckuserid(admininfo.getId().intValue());
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
                exceptionhandlerinfo.setCheckuserid(admininfo.getId().intValue());
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
