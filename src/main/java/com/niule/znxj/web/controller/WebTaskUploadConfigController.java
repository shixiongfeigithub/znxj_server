package com.niule.znxj.web.controller;

import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * xuqb add 20191111
 */
@Controller
public class WebTaskUploadConfigController {
    @Resource
    private InterfaceService interfaceService;
    @Resource
    private SiteService siteService;
    @Resource
    private TaskUploadConfigService taskUploadConfigService;
    @Resource
    private OperateLogService operateLogService;
    @Resource
    private ContactinfoService contactinfoService;

    protected PageBean pageBean = new PageBean();

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    /**
     * 显示任务上传列表
     * @param page 当前页
     * @param m
     * @param request
     * @return
     */
    @RequestMapping("showalltaskupload")
    @RequiresPermissions("item:taskupload")
    public String showalltaskupload(int page, Model m,Long siteid,Long taskid, HttpServletRequest request){
        if(page<=0){
            page=1;
        }
        int pagesieze=15;
        page = PageBean.countCurrentPage(page);
        List<Taskuploadconfig> taskuploadconfigList=null;
        long totalpage=0;

        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> sites=siteService.queryAllSite();
        List ids=new ArrayList() ;
        if(admininfo.getSiteid()==null){
            if(sites.size()>0) {
                for (Siteareainfo siteareainfo : sites) {
                    ids.add(siteareainfo.getId());
                }
            }
        }else{
            ids.add(admininfo.getSiteid());
        }
        if(siteid!=null){
            ids.clear();
            ids.add(siteid);
        }
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("page",(page-1)*pagesieze);
        map.put("size",pagesieze);
        map.put("ids",ids);
        map.put("taskid",taskid);
        taskuploadconfigList = taskUploadConfigService.selectByPageParam(map);
        Integer rows = taskUploadConfigService.countByPageParam(map);
        totalpage = PageBean.counTotalPage(pagesieze, rows);
        pageBean.setList(taskuploadconfigList);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean",pageBean);
        m.addAttribute("sites",sites);
        m.addAttribute("siteid",siteid);
        m.addAttribute("taskid",taskid);
        return "showtaskupload";
    }

    /**
     * 跳转到添加任务上传页面
     * @param m
     * @param request
     * @return
     */
    @RequestMapping("toaddtaskupload")
    public String toaddtaskupload(Model m,HttpServletRequest request){
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> siteareainfos=siteService.selectByExample2(admininfo.getSiteid());
        List<Contactinfo> contactinfoList = contactinfoService.sendAllPersion();
        m.addAttribute("siteareainfos",siteareainfos);
        m.addAttribute("contactinfoList",contactinfoList);
        return "addtaskupload";
    }

    /**
     * 添加任务上传
     * @param taskuploadconfig
     * @param session
     * @return
     */
    @RequestMapping("addtaskupload")
    public String addtaskupload(Model m,Taskuploadconfig taskuploadconfig, HttpSession session){

        TaskuploadconfigExample example = new TaskuploadconfigExample();
        example.createCriteria().andTaskidEqualTo(taskuploadconfig.getTaskid());
        List<Taskuploadconfig> list = taskUploadConfigService.selectByExample(example);
        if (list!=null && list.size()>0){
            Admininfo admininfo=(Admininfo) session.getAttribute("userInfo");
            List<Siteareainfo> siteareainfos=siteService.selectByExample2(admininfo.getSiteid());
            List<Contactinfo> contactinfoList = contactinfoService.sendAllPersion();
            m.addAttribute("siteareainfos",siteareainfos);
            m.addAttribute("contactinfoList",contactinfoList);
            m.addAttribute("message","一个任务只能添加一次，不能重复添加！");
            return "addtaskupload";
        }

        int addresult=taskUploadConfigService.insert(taskuploadconfig);
        //获取登录用户的信息
        String info="新增了任务上传配置："+taskuploadconfig.getTaskid();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);

        if(addresult>0&&addlog>0){
            return "redirect:showalltaskupload?page=1";
        }
        return "addtaskupload";
    }

    /**
     * 删除任务上传
     * @param id ID
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("deltaskupload")
    @RequiresPermissions("del:taskupload")
    @ResponseBody
    public int deltaskupload(Long id,HttpServletRequest request,HttpSession session){
        int deltaskupload=taskUploadConfigService.deleteByPrimaryKey(id);
        //获取登录用户的信息
        String info="删除了任务上传配置";
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(deltaskupload>0&&addlog>0){
            return 1;
        }else
            return 0;
    }
    @RequestMapping("findbytaskuploadid")
    @RequiresPermissions("upd:taskupload")
    public String findbytaskuploadid(Long id,Model m,HttpServletRequest request,int page){
        Taskuploadconfig taskuploadconfig =taskUploadConfigService.selectByPrimaryKey(id);
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> siteareainfos=siteService.selectByExample2(admininfo.getSiteid());
        List<Contactinfo> contactinfoList = contactinfoService.sendAllPersion();
        m.addAttribute("siteareainfos",siteareainfos);
        m.addAttribute("taskuploadconfig",taskuploadconfig);
        m.addAttribute("contactinfoList",contactinfoList);
        m.addAttribute("page",page);
        return "updatetaskupload";
    }
    @RequestMapping("updtaskupload")
    public String updtaskupload(Taskuploadconfig taskuploadconfig, Model model,HttpSession session,int page){
        taskuploadconfig.setUpdatetime(new Date());
        int updresult =taskUploadConfigService.updateByPrimaryKeySelective(taskuploadconfig);
        //获取登录用户的信息
        String info="修改了任务上产配置"+taskuploadconfig.getTaskid();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(updresult>0 && addlog>0){
            return "redirect:showalltaskupload?page="+page;
        }
        return "redirect:findbytaskuploadid?id="+ taskuploadconfig.getId()+"&page="+page;
    }
}