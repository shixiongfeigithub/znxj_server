package com.niule.znxj.web.controller;

import com.niule.znxj.core.common.Resources;
import com.niule.znxj.core.util.DateUtils;
import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.core.util.json.JsonUtil;
import com.niule.znxj.web.dao.ReportcontentMapper;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    private String ip = Resources.ApplicationResources.getString("ip");
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
    private SystemService systemService;
    @Autowired
    private ExceptionhandlerinfoService exceptionhandlerinfoService;
    @Resource
    private  CommonService commonService;

    protected PageBean pageBean = new PageBean();


    /**
     * 分页显示所有异常巡检项 - 列表
     */
    @RequestMapping("/showexceptionreport")
    public String showexceptionreport(Model m, int page,Long siteid,Long areaid,Long equipmentid,Integer exceptionstate,
                                 String operatorname,String exceptiontype,String exceptionlever, String time1, String time2,HttpServletRequest request) {

        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> siteareainfos = null;
        List<Long> siteids = new ArrayList<>();
        if (admininfo.getSiteid() == null) {
            List<Siteareainfo> sites = siteService.queryAllSite();
            for (Siteareainfo siteareainfo : sites) {
                siteids.add(siteareainfo.getId());
            }
        } else {
            siteids.add(Long.valueOf(admininfo.getSiteid()));
        }
        siteareainfos = siteService.selectByExample3(siteids); //查询角色对应的所有厂区
        if(siteid !=null){
            siteids.clear();
            siteids.add(siteid);
        }

        if (page <= 0) {
            page = 1;
        }
        page = PageBean.countCurrentPage(page);
        long totalpage = 0;
        int pagesize = 15;

        Areainfo areainfo = null;
        Equipmentinfo equipmentinfo = null;
        if (areaid!=null)
            areainfo = areaService.selectByPrimaryKey(areaid);
        if (equipmentid !=null)
            equipmentinfo = equipmentService.selectByPrimaryKey(equipmentid);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("page", (page - 1) * pagesize);
        map.put("pagesize", pagesize);
        map.put("siteids", siteids);//厂区
        map.put("areaname", areainfo!=null?areainfo.getCustomid():null);
        map.put("equipname",equipmentinfo!=null?equipmentinfo.getName():null);
        map.put("exceptionstate",exceptionstate);//异常处理状态
        if (exceptionstate!=null && exceptionstate==0)
            map.put("showexceptiontype","1");
        map.put("operatorname",operatorname); //巡检责任人
        map.put("exceptionlever",exceptionlever); //异常分级
        map.put("exceptiontype",exceptiontype); //异常分类
        map.put("time1", time1==null?DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd") :time1);//开始时间
        map.put("time2", time2==null?DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd") :time2);//结束时间

        //判断用户角色
        if(admininfo.getRoleid()==3){ //操作员
            map.put("operatorid",admininfo.getId());
        }
        List<Reportcontent>  reportcontents = taskreportService.selectReportcontentByParam(map);
        int rows = taskreportService.countReportcontentByParam(map);
        int closenum = taskreportService.countReportcontentByExceptionState(map);
        int surplusnum = rows-closenum;

        totalpage = PageBean.counTotalPage(pagesize, rows);
        pageBean.setList(reportcontents);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        List<Warningtasktype> exceptiontypeList = commonService.getWarningTypeOrLevels(3); //异常类型
        List<Warningtasktype> levertype1List = commonService.getWarningTypeOrLevels(4); //异常等级
        m.addAttribute("levertype1List",levertype1List);
        m.addAttribute("exceptiontypeList",exceptiontypeList);
        m.addAttribute("pageBean", pageBean);
        m.addAttribute("siteid", siteid);
        m.addAttribute("areaid", areaid);
        m.addAttribute("equipmentid", equipmentid);
        m.addAttribute("exceptionstate", exceptionstate);
        m.addAttribute("operatorname",operatorname);
        m.addAttribute("exceptionlever",exceptionlever); //异常分级
        m.addAttribute("exceptiontype",exceptiontype); //异常分类
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);

        m.addAttribute("sites", siteareainfos);
        m.addAttribute("roleid", admininfo.getRoleid());
        m.addAttribute("totalnum",rows);
        m.addAttribute("closenum",closenum);
        m.addAttribute("surplusnum",surplusnum);
        m.addAttribute("ip", ip);
        return "showexceptionreport";
    }


    /*跳转到关闭异常巡检项页面*/
    @RequestMapping("/toclosetaskreport")
    @RequiresPermissions("item:closereport")
    public String toclosetaskreport(Model m, Long id,Long siteid,Long areaid,Long equipmentid,Integer exceptionstate,
                                    String operatorname,String exceptiontype,String exceptionlever, String time1, String time2) {
        Reportcontent reportcontent = taskreportService.selectReportContentByPrimaryKey(id);
        List<Warningtasktype> exceptiontypeList = commonService.getWarningTypeOrLevels(3); //异常类型
        List<Warningtasktype> levertype1List = commonService.getWarningTypeOrLevels(4); //异常等级

        ExceptionhandlerinfoExample example = new ExceptionhandlerinfoExample();
        example.createCriteria().andReportcontentidEqualTo(id);
        List<Exceptionhandlerinfo> infoList = exceptionhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
            m.addAttribute("exceptionhandlerinfo", infoList.get(0));
        }
        m.addAttribute("reportcontent", reportcontent);
        m.addAttribute("levertype1List",levertype1List);
        m.addAttribute("exceptiontypeList",exceptiontypeList);

        m.addAttribute("siteid", siteid);
        m.addAttribute("areaid", areaid);
        m.addAttribute("equipmentid", equipmentid);
        m.addAttribute("exceptionstate", exceptionstate);
        m.addAttribute("operatorname",operatorname);
        m.addAttribute("exceptionlever",exceptionlever); //异常分级
        m.addAttribute("exceptiontype",exceptiontype); //异常分类
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        return "closetaskreport";
    }

    @RequestMapping("/closetaskreport")
    @ResponseBody
    public int closetaskreport(Exceptionhandlerinfo exceptionhandlerinfo,HttpServletRequest request){

        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        ExceptionhandlerinfoExample example = new ExceptionhandlerinfoExample();
        example.createCriteria().andReportcontentidEqualTo(exceptionhandlerinfo.getReportcontentid());
        List<Exceptionhandlerinfo> infoList = exceptionhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
           Exceptionhandlerinfo info = infoList.get(0);
           if (info.getExceptionclosetime()!=null && info.getExceptionstate()==1) //已关闭
                return 0;

            List<String> attach=new ArrayList<>();
            String [] stringArr= exceptionhandlerinfo.getAttachment().split(",");
            for(int i=0;i<stringArr.length;i++){
                attach.add(stringArr[i]);
            }
            info.setAttachment(JsonUtil.toJSON(attach));
            info.setDescontent(exceptionhandlerinfo.getDescontent());
            info.setCheckuserid(admininfo.getId());
            info.setExceptionclosetime(new Date());
            info.setExceptionstate(1); //已关闭
            info.setUploadstate(0); //设置上传状态为未上传
            if(info.getExceptiontype()==null){
                info.setExceptiontype(exceptionhandlerinfo.getExceptiontype());
            }
            if(info.getExceptionlever()==null){
                info.setExceptionlever(exceptionhandlerinfo.getExceptionlever());
            }
            if (info.getOperatorid()==null){
                info.setOperatorid(admininfo.getId());
                info.setOperatorname(admininfo.getUsername());
            }

            int result=exceptionhandlerinfoService.updateByExample(info,example);
            return result;
        }
        return -1;
    }

    /*异常巡检任务指定责任人页面*/
    @RequestMapping("/toassignprincipal")
    @RequiresPermissions("item:assignprincipal")
    public String toassignprincipal(Model m, Long id,Long siteid,Long areaid,Long equipmentid,Integer exceptionstate,
                                    String operatorname,String exceptiontype,String exceptionlever, String time1, String time2, HttpServletRequest request) {
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        List<Admininfo> operationuserList = admininfoService.selectByRoleId(3); //查询所有角色为操作员的用户
        Reportcontent reportcontent = taskreportService.selectReportContentByPrimaryKey(id);
        for (Admininfo user : operationuserList){
            if (user.getId()==admininfo.getId()){
                operationuserList.remove(user);
                break;
            }
        }
        List<Warningtasktype> exceptiontypeList = commonService.getWarningTypeOrLevels(3); //异常类型
        List<Warningtasktype> levertype1List = commonService.getWarningTypeOrLevels(4); //异常等级
        ExceptionhandlerinfoExample example = new ExceptionhandlerinfoExample();
        example.createCriteria().andReportcontentidEqualTo(id);
        List<Exceptionhandlerinfo> infoList = exceptionhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
            m.addAttribute("exceptionhandlerinfo", infoList.get(0));
        }
        m.addAttribute("reportcontent", reportcontent);
        m.addAttribute("operationuserList",operationuserList);
        m.addAttribute("levertype1List",levertype1List);
        m.addAttribute("exceptiontypeList",exceptiontypeList);

        m.addAttribute("siteid", siteid);
        m.addAttribute("areaid", areaid);
        m.addAttribute("equipmentid", equipmentid);
        m.addAttribute("exceptionstate", exceptionstate);
        m.addAttribute("operatorname",operatorname);
        m.addAttribute("exceptionlever",exceptionlever); //异常分级
        m.addAttribute("exceptiontype",exceptiontype); //异常分类
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        return "assignprincipal";
    }

    @RequestMapping("/assignprincipal")
    @ResponseBody
    public int assignprincipal(Exceptionhandlerinfo exceptionhandlerinfo,HttpServletRequest request){
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");

        Admininfo user = admininfoService.selectByPrimaryKey(exceptionhandlerinfo.getOperatorid());
        ExceptionhandlerinfoExample example = new ExceptionhandlerinfoExample();
        example.createCriteria().andReportcontentidEqualTo(exceptionhandlerinfo.getReportcontentid());
        List<Exceptionhandlerinfo> infoList = exceptionhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
            Exceptionhandlerinfo info = infoList.get(0);
            if(info.getAppointedtime()==null){
                info.setCheckuserid(admininfo.getId());
                info.setAppointedtime(new Date());
            }
            info.setOperatorid(exceptionhandlerinfo.getOperatorid());
            info.setOperatorname(user.getUsername());
            info.setExceptionstate(2); //已分配责任人
            info.setExceptiontype(exceptionhandlerinfo.getExceptiontype());
            info.setExceptionlever(exceptionhandlerinfo.getExceptionlever());
            info.setUploadstate(0); //设置上传状态为未上传
            int result=exceptionhandlerinfoService.updateByExample(info,example);

            //发送异常报告给负责人
            commonService.sendReportEmail(exceptionhandlerinfo.getOperatorid(),info.getReportcontentid());
            return result;
        }
        return 0;
    }


    /*查看异常处理详情页面*/
    @RequestMapping("/handlerexceptiondetail")
    public String handlerexceptiondetail(Model m, Long id, HttpServletRequest request) {
        Exceptionhandlerinfo exceptionhandlerinfo = exceptionhandlerinfoService.selectInfoById(id);
        List<String> imgList = new ArrayList<String>();
        if(exceptionhandlerinfo.getAttachment()!=null && !"".equals(exceptionhandlerinfo.getAttachment())){
           imgList = JsonUtil.toObject(exceptionhandlerinfo.getAttachment(),List.class);
        }
        m.addAttribute("exceptionhandlerinfo",exceptionhandlerinfo);
        m.addAttribute("imgList",imgList);
        m.addAttribute("ip",ip);
        return "handlerexceptiondetail";
    }

    public PageBean getPageBean() {
        return pageBean;
    }
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
