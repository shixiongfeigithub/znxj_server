package com.niule.znxj.web.controller;

import com.github.pagehelper.PageInfo;
import com.niule.znxj.core.common.Resources;
import com.niule.znxj.core.util.DateUtils;
import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.core.util.json.JsonUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuqb
 * Date: 2019-11-01 10:33
 */
@Controller
public class TaskDangerController {

    private String ip = Resources.ApplicationResources.getString("ip");
    @Resource
    private AdmininfoService admininfoService;
    @Autowired
    private SiteService siteService;
    @Resource
    private  CommonService commonService;
    @Resource
    private  QuickReportService quickReportService;
    @Resource
    private DangerhandlerinfoService dangerhandlerinfoService;

    protected PageBean pageBean = new PageBean();


    /**
     * 分页显示所有隐患列表
     */
    @RequestMapping("/showdanger")
    public String showdanger(Model m, int page,Long siteid,Integer dangerstate,
                                 String operatorname, String reportcode,String time1,String time2,HttpServletRequest request) {

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

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("page", (page - 1) * pagesize);
        map.put("pagesize", pagesize);
        map.put("siteids", siteids);//厂区
        map.put("dangerstate",dangerstate);//隐患处理状态
        map.put("operatorname",operatorname); //巡检责任人
        map.put("time1", time1==null?DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd") :time1);//开始时间
        map.put("time2", time2==null?DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd") :time2);//结束时间
        map.put("reportcode",reportcode==null||reportcode.isEmpty()?"":"%"+reportcode+"%");
        //判断用户角色
        if(admininfo.getRoleid()==3){ //操作员
            map.put("operatorid",admininfo.getId());
        }
        List<Quickreport> quickreportList =quickReportService.showQuickreport2(map);
        int rows = quickReportService.countByExample2(map);
        int closenum = quickReportService.countByCloseState(map);
        int surplusnum = rows-closenum;

        totalpage = PageBean.counTotalPage(pagesize, rows);
        pageBean.setList(quickreportList);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean", pageBean);
        m.addAttribute("siteid", siteid);
        m.addAttribute("dangerstate", dangerstate);
        m.addAttribute("operatorname",operatorname);
        m.addAttribute("reportcode", reportcode);
        m.addAttribute("time1", time1==null?DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd") :time1);
        m.addAttribute("time2", time2==null?DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd") :time2);
        m.addAttribute("sites", siteareainfos);
        m.addAttribute("totalnum",rows);
        m.addAttribute("closenum",closenum);
        m.addAttribute("surplusnum",surplusnum);
        m.addAttribute("ip", ip);
        return "showdanger";
    }


    /*跳转到关闭隐患页面*/
    @RequestMapping("/toclosedanger")
    @RequiresPermissions("item:closedanger")
    public String toclosedanger(Model m, Long id,HttpServletRequest request) {
        Quickreport quickreport = quickReportService.selectByPrimaryKey(id);
        m.addAttribute("quickreport",quickreport);
        return "closedanger";
    }

    @RequestMapping("/closedanger")
    @ResponseBody
    public int closedanger(Dangerhandlerinfo dangerhandlerinfo,HttpServletRequest request){

        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        DangerhandlerinfoExample example = new DangerhandlerinfoExample();
        example.createCriteria().andReportidEqualTo(dangerhandlerinfo.getReportid());
        List<Dangerhandlerinfo> infoList = dangerhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
            Dangerhandlerinfo info = infoList.get(0);
           if (info.getDangerclosetime()!=null && info.getDangerstate()==1) //已关闭
                return 0;

            List<String> attach=new ArrayList<>();
            String [] stringArr= dangerhandlerinfo.getAttachment().split(",");
            for(int i=0;i<stringArr.length;i++){
                attach.add(stringArr[i]);
            }
            info.setAttachment(JsonUtil.toJSON(attach));
            info.setDescontent(dangerhandlerinfo.getDescontent());
            info.setCheckuserid(admininfo.getId());
            info.setDangerclosetime(new Date());
            info.setDangerstate(1); //已关闭
            int result=dangerhandlerinfoService.updateByExample(info,example);
            return result;
        }
        return -1;
    }

    /*异常巡检任务指定责任人页面*/
    @RequestMapping("/toassigndanger")
    @RequiresPermissions("item:assigndanger")
    public String toassigndanger(Model m, Long id, HttpServletRequest request) {
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");
        List<Admininfo> operationuserList = admininfoService.selectByRoleId(3); //查询所有角色为操作员的用户
        Quickreport quickreport = quickReportService.selectByPrimaryKey(id);
        for (Admininfo user : operationuserList){
            if (user.getId()==admininfo.getId()){
                operationuserList.remove(user);
                break;
            }
        }
        if(quickreport.getContent()!=null){
            List<String> contentList = JsonUtil.toObject(quickreport.getContent(),List.class);
            if(contentList!=null && contentList.size()>=2){
                m.addAttribute("yinhuantype",contentList.get(0));
                m.addAttribute("levertype",contentList.get(1));
            }
        }
        List<Warningtasktype> yinhuantypeList = commonService.getWarningTypeOrLevels(0); //隐患类型
        List<Warningtasktype> levertype1List = commonService.getWarningTypeOrLevels(1); //隐患等级
        m.addAttribute("levertype1List",levertype1List);
        m.addAttribute("yinhuantypeList",yinhuantypeList);
        m.addAttribute("quickreport", quickreport);
        m.addAttribute("operationuserList",operationuserList);
        return "assigndanger";
    }

    @RequestMapping("/assigndanger")
    @ResponseBody
    public int assigndanger(Dangerhandlerinfo dangerhandlerinfo,HttpServletRequest request){
        Admininfo admininfo = (Admininfo) request.getSession().getAttribute("userInfo");

        Admininfo user = admininfoService.selectByPrimaryKey(dangerhandlerinfo.getOperatorid());
        DangerhandlerinfoExample example = new DangerhandlerinfoExample();
        example.createCriteria().andReportidEqualTo(dangerhandlerinfo.getReportid());
        List<Dangerhandlerinfo> infoList = dangerhandlerinfoService.selectByExample(example);
        if(infoList!=null && infoList.size()>0){
            Dangerhandlerinfo info = infoList.get(0);
            if(info.getAppointedtime()==null){
                info.setCheckuserid(admininfo.getId());
                info.setAppointedtime(new Date());
            }
            info.setOperatorid(dangerhandlerinfo.getOperatorid());
            info.setOperatorname(user.getUsername());
            info.setDangerstate(2); //已分配责任人
            int result=dangerhandlerinfoService.updateByExample(info,example);

            //更新隐患类型和等级
            Quickreport quickreport = quickReportService.selectByPrimaryKey(info.getReportid());
            if(quickreport.getType()==1){
                List<String> contents = JsonUtil.toObject(quickreport.getContent(),List.class);
                if(contents!=null && contents.size()>=2){
                    contents.subList(1,contents.size());
                }
                contents.add(0,dangerhandlerinfo.getYinhuantype());
                contents.add(1,dangerhandlerinfo.getYinhuanlevel());
                quickreport.setContent(JsonUtil.toJSON(contents));
                quickReportService.updateByPrimaryKeySelective(quickreport);
            }
            //发送隐患信息给负责人
            commonService.sendDangerEmail(dangerhandlerinfo.getOperatorid(),info.getReportid());
            return result;
        }
        return 0;
    }


    /*查看异常处理详情页面*/
    @RequestMapping("/handlerdangerdetail")
    public String handlerdangerdetail(Model m, Long id, HttpServletRequest request) {
        Dangerhandlerinfo dangerhandlerinfo = dangerhandlerinfoService.selectInfoByReportId(id);
        List<String> imgList = new ArrayList<String>();
        if(dangerhandlerinfo.getAttachment()!=null && !"".equals(dangerhandlerinfo.getAttachment())){
           imgList = JsonUtil.toObject(dangerhandlerinfo.getAttachment(),List.class);
        }
        m.addAttribute("dangerhandlerinfo",dangerhandlerinfo);
        m.addAttribute("imgList",imgList);
        m.addAttribute("ip",ip);
        return "handlerdangerdetail";
    }

    public PageBean getPageBean() {
        return pageBean;
    }
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
