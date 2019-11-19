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
    public String showdanger(Model m, int page,Long siteid,Integer type,Integer dangerstate,
                                 String operatorname, String reportcode,String uploadtime,HttpServletRequest request) {

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
        map.put("type",type==null?1:type);//隐患或即拍即传
        if(type!=null && type==0){
            map.put("typedesc","HSE");//隐患或即拍即传
        }
        map.put("dangerstate",dangerstate);//隐患处理状态
        map.put("operatorname",operatorname); //巡检责任人
        map.put("time", uploadtime);//上传时间
        map.put("reportcode",reportcode==null||reportcode.isEmpty()?"":"%"+reportcode+"%");
        //判断用户角色
        if(admininfo.getRoleid()==3){ //操作员
            map.put("operatorid",admininfo.getId());
        }
        List<Quickreport> quickreportList =quickReportService.showQuickreport2(map);
        int rows = quickReportService.countByExample2(map);
       /* int closenum = taskreportService.countReportcontentByExceptionState(map);
        int surplusnum = rows-closenum;*/

        totalpage = PageBean.counTotalPage(pagesize, rows);
        pageBean.setList(quickreportList);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean", pageBean);
        m.addAttribute("siteid", siteid);
        m.addAttribute("type", type==null?1:type);
        m.addAttribute("dangerstate", dangerstate);
        m.addAttribute("operatorname",operatorname);
        m.addAttribute("reportcode", reportcode);
        m.addAttribute("uploadtime", uploadtime);
        m.addAttribute("sites", siteareainfos);
        /*m.addAttribute("totalnum",rows);
        m.addAttribute("closenum",closenum);
        m.addAttribute("surplusnum",surplusnum);*/
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

            //发送隐患信息给负责人
            commonService.sendDangerEmail(info.getOperatorid(),info.getReportid());
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
