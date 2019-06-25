package com.niule.znxj.web.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageInfo;
import com.niule.znxj.core.entity.JSONResult;
import com.niule.znxj.core.entity.Result;
import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.core.util.json.JsonUtil;
import com.niule.znxj.web.dao.TaskstopinfoMapper;
import com.niule.znxj.web.dao.TasktempinfoMapper;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.model.taskcontent.TaskArea;
import com.niule.znxj.web.model.taskcontent.TaskContent;
import com.niule.znxj.web.service.*;
import net.sf.json.JSON;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by administor on 2017/3/27.
 */
@Controller
public class WebTaskPlanController {
    @Resource
    private TaskPlanService taskPlanService;
    @Resource
    private ClassinfoService classinfoService;
    @Resource
    private UserinfoService userinfoService;
    @Resource
    private AreaService areaService;
    @Resource
    private EquipmentService equipmentService;
    @Resource
    private CheckInfoService checkInfoService;
    @Resource
    private CommonService commonService;
    @Resource
    private SiteService siteService;
    @Resource
    private TasktempinfoMapper tasktempinfoMapper;
    @Resource
    private TaskstopinfoMapper taskstopinfoMapper;
    @Resource
    private OperateLogService operateLogService;
    protected PageBean pageBean = new PageBean();

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
      /*日常任务巡检部分*/

    /**
     * 分页显示所有任务的信息
     * @param page 当前页
     * @param identifyingid 任务编号
     * @param m
     * @param state 任务状态 0 停止运行 1 运行中
     * @param type 任务类型 0日常巡检 1计划巡检 2隐患排查 3视频巡检 4临时任务
     * @return
     */
    @RequestMapping("showtaskplan")/*
    @RequiresPermissions("item:task")*/
    public String showtaskplan(int page, String identifyingid, Model m, String state, int type, HttpServletRequest request){
        if(page<=0){
            page=1;
        }
        int pagesize=15;
        page = PageBean.countCurrentPage(page);

        long totalpage=0;
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List ids=new ArrayList() ;
        if(admininfo.getSiteid()==null){
            List<Siteareainfo> sites=siteService.queryAllSite();
            if(sites.size()>0){
                for(Siteareainfo siteareainfo:sites){
                    ids.add(siteareainfo.getId());
                }
            }else
                ids=null;
        }else{
            ids.add(admininfo.getSiteid());
        }
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("page",(page-1)*pagesize);
        map.put("size",pagesize);
        map.put("ids",ids);
        map.put("identifyingid","%"+identifyingid+"%");
        map.put("state",state);
        map.put("type",type);
        List<Taskplaninfo> taskplaninfos=null;
        if((identifyingid==null||"".equals(identifyingid)) && (state==null || "".equals(state)) ){
            taskplaninfos = taskPlanService.findByPageTask(map);
            int rows1 = taskPlanService.countTask(map);
            totalpage = PageBean.counTotalPage(pagesize, rows1);
            state="";
        }else{
            taskplaninfos = taskPlanService.findByPageTask2(map);
            int rows2 = taskPlanService.countTask2(map);
            totalpage = PageBean.counTotalPage(pagesize, rows2);
        }
        pageBean.setList(taskplaninfos);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean",pageBean);
        m.addAttribute("type",type);
        m.addAttribute("identifyingid",identifyingid);
        m.addAttribute("states",state);
        return "showtaskplan";
    }
    /*跳转到新增日常巡检任务页面*/
    @RequestMapping("toaddtaskplan")
    public String toaddtaskplan(Model m,int type,HttpServletRequest request){
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List<Classinfo> classinfos=classinfoService.selectByExample2(admininfo.getSiteid());
        List<Siteareainfo> siteareainfos=siteService.selectByExample2(admininfo.getSiteid());

        m.addAttribute("classinfos",classinfos);
        m.addAttribute("type",type);
        m.addAttribute("siteareainfos",siteareainfos);
        return "addtaskplan";
    }
    /*新增日常任务*/
    @RequestMapping("addtaskplan")
    @ResponseBody
    public Result addtaskplan(Taskplaninfo taskplaninfo, HttpSession session){
        //插入任务
        taskPlanService.insert(taskplaninfo);
        //往temp表插入相关信息
        commonService.generateByTask(taskplaninfo);
        String info="添加了任务"+taskplaninfo.getCustomid();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        return new JSONResult<>();
    }
    /*显示班组（级联负责人和班组）*/
    @RequestMapping("querydirectorid")
    @ResponseBody
    public Long querydirectorid(Long classid,Model m){
        Classinfo classinfo=classinfoService.selectByPrimaryKey(classid);
        return classinfo.getDirectorid();
    }
    @RequestMapping("querydirectorname")
    @ResponseBody
    public Userinfo querydirectorname(Long uid,Model m){
        Userinfo userinfo=userinfoService.queryRealname(uid);
        return userinfo;
    }
    /*显示所有区域*/
    @RequestMapping("showarea")
    @ResponseBody
    public List<Areainfo> showarea(){
        return areaService.selectByExample();
    }
    /*显示某个区域下的所有设备*/
    @RequestMapping("showequipment")
    @ResponseBody
    public List<Equipmentinfo> showequipment(int areaid){
        return equipmentService.queryequip(areaid);
    }
    /*显示所有巡检项*/
    @RequestMapping("showcheck")
    @ResponseBody
    public List<Checkiteminfo> showcheck(){
        return checkInfoService.selectByExample();
    }
    /*显示巡检项内容*/
    @RequestMapping("showdatarecord")
    @ResponseBody
    public Checkiteminfo showdatarecord(int recordid){
        return checkInfoService.queryByRecord(recordid);
    }

    /*删除指定任务*/
    @RequestMapping("deltask")
    @ResponseBody
    public int deltask(Long id,HttpServletRequest request){
        HttpSession session=request.getSession();
        String name=request.getParameter("taskname");
        int deltask= taskPlanService.deleteByPrimaryKey(id);
        String info="删除了任务"+name;
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(deltask>0&&addlog>0){
            return 1;
        }else
            return 0;
    }

    /**
     * 根据编号id查询当前任务的详细信息
     * @param id 编号id
     * @param m
     * @return 跳到修改日常页面
     */
    @RequestMapping("querybytaskid")
    public String querybytaskid(Long id,Model m,int type,HttpServletRequest request){
        Taskplaninfo taskplaninfo=taskPlanService.selectByPrimaryKey(id);
        List<Userinfo> userinfos=userinfoService.selectByExample();

        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List<Classinfo> classinfos=classinfoService.selectByExample2(admininfo.getSiteid());
        List<Siteareainfo> siteareainfos=siteService.selectByExample2(admininfo.getSiteid());

        m.addAttribute("taskplaninfo",taskplaninfo);
        m.addAttribute("classinfos",classinfos);
        m.addAttribute("userinfos",userinfos);
        m.addAttribute("type",type);
        m.addAttribute("siteareainfos",siteareainfos);

        return "updatetaskplan";
    }

    /**
     * 修改日常巡检任务
     * @param taskplaninfo
     * @return
     */
    @RequestMapping("updtaskplan")
    @ResponseBody
    public Result updtaskplan(Taskplaninfo taskplaninfo,String isfinal,HttpSession session){
        if(("0").equals(isfinal)){
            taskPlanService.updateByPrimaryKeySelective(taskplaninfo);
            taskPlanService.deleteTaskTempByTaskId(taskplaninfo.getId());
            commonService.generateByTask(taskplaninfo);
            return new JSONResult<>();
        }else
            taskPlanService.updateByPrimaryKeySelective(taskplaninfo);

        String info="修改了任务"+taskplaninfo.getCustomid();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);

        return new JSONResult<>();
    }
    /**
     * 查看日常巡检任务详情
     */
    @RequestMapping("taskplandetail")
   /* @RequiresPermissions("item:taskdetail")*/
    public String taskplandetail(Long id,Model m,int type){
        Taskplaninfo taskplaninfo=taskPlanService.selectByPrimaryKey(id);
        List<Classinfo> classinfos=classinfoService.selectByExample();
        List<Userinfo> userinfos=userinfoService.selectByExample();

        m.addAttribute("taskplaninfo",taskplaninfo);
        m.addAttribute("classinfos",classinfos);
        m.addAttribute("userinfos",userinfos);
        m.addAttribute("type",type);
        return "taskplandetail";
    }

    @RequestMapping("showstoptask")
    public String showstoptask(int page,int state,Model m,int type){
        int size=15;
        PageInfo info = new PageInfo<>(taskPlanService.getStopTask(page,size,state,type));
        m.addAttribute("info",info);
        m.addAttribute("type",type);
        return "showstoptask";
    }
    @RequestMapping("deltemp")
    @ResponseBody
    public int deltemp(Long tempid){
        TaskstopinfoExample taskstopinfoExample=new TaskstopinfoExample();
        taskstopinfoExample.createCriteria().andTasktempidEqualTo(tempid);
        int delstop=taskstopinfoMapper.deleteByExample(taskstopinfoExample);

        int deltemp=tasktempinfoMapper.deleteByPrimaryKey(tempid);

        if(delstop>0&&deltemp>0){
            return 1;
        }else
            return 0;
    }
}
