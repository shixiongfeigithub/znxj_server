package com.niule.znxj.web.controller;

import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.web.dao.CheckitemTaskMapper;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.service.CheckInfoService;
import com.niule.znxj.web.service.DateRecordService;
import com.niule.znxj.web.service.OperateLogService;
import com.niule.znxj.web.service.SiteService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by administor on 2017/3/22.
 */
@Controller
public class WebCheckController {
    @Resource
    private CheckInfoService checkInfoService;
    @Resource
    private DateRecordService dateRecordService;
    @Resource
    private OperateLogService operateLogService;
    @Resource
    private CheckitemTaskMapper checkitemTaskMapper;
    @Resource
    private SiteService siteService;

    protected PageBean pageBean = new PageBean();

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    @RequestMapping("showallcheck")
    @RequiresPermissions("item:check")
    public String showallcheck(int page, String itemname,String siteid, Model m,HttpServletRequest request){
        if(page<=0){
            page=1;
        }
        int pagesieze=15;
        page = PageBean.countCurrentPage(page);
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        HashMap<String,Object> map=new HashMap<String,Object>();
        //单个工厂登录者所属工厂 或者 多个工厂登陆者下拉框所选工厂
        if(siteid ==null || "".equals(siteid) ) {
            map.put("siteid", admininfo.getSiteid());
            m.addAttribute("siteid",admininfo.getSiteid());
        }else{
            map.put("siteid", siteid);
            m.addAttribute("siteid",siteid);
        }
        List<Checkiteminfo> checkiteminfos=null;
        long totalpage=0;
        map.put("page",(page-1)*pagesieze);
        map.put("pagesize",pagesieze);
        if(itemname ==null || "".equals(itemname) ) {
            map.put("itemname", "");
        }else{
            map.put("itemname","%"+itemname+"%");
        }
        checkiteminfos = checkInfoService.findByPageCheck2(map);
        int rows2 = checkInfoService.countCheck2(map);
        totalpage = PageBean.counTotalPage(pagesieze, rows2);
        m.addAttribute("ad",admininfo);
        pageBean.setList(checkiteminfos);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean",pageBean);
        m.addAttribute("itemname",itemname);
        List<Siteareainfo> siteareainfos=siteService.queryAllSite();
        m.addAttribute("siteareainfos", siteareainfos);
        return "showcheckiteminfo";
    }
    @RequestMapping("toaddcheck")
    @RequiresPermissions("add:check")
    public String toaddcheck(Model m,Integer recodeType,HttpServletRequest request){
        List<Daterecordinfo> daterecordinfos=dateRecordService.selectByExample(recodeType);
        m.addAttribute("daterecordinfos",daterecordinfos);
        List<Siteareainfo> siteareainfos=siteService.queryAllSite();
        m.addAttribute("siteareainfos",siteareainfos);
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        m.addAttribute("ad",admininfo);
        return "addcheckitem";
    }
    @RequestMapping("addcheck")
    public String addcheck(Checkiteminfo checkiteminfo, HttpSession session){
        int addresult=checkInfoService.insert(checkiteminfo);
        //获取登录用户的信息

        String info="新增了巡检项"+checkiteminfo.getCustomid();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(addresult>0&&addlog>0){
            return "redirect:showallcheck?page=1";
        }
        return "addcheckitem";
    }

    @RequestMapping("delcheck")
    @RequiresPermissions("del:check")
    @ResponseBody
    public int delcheck(Long id, HttpServletRequest request){
        //删除巡检项
        int delcheck= checkInfoService.deleteByPrimaryKey(id);
        //2018年7月16日16:46:06 新加   --
        if (delcheck == 2)
            return delcheck;
        //2018年7月16日16:46:06 新加   --
        String checkname=request.getParameter("checkname");
        //添加操作日志
        HttpSession session=request.getSession();

        String info="删除了巡检项"+checkname+"的信息";
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(delcheck>0&&addlog>0){
            return 1;
        }else
            return 0;
    }
    @RequestMapping("querybycheckid")
    @RequiresPermissions("upd:check")
    public String querybycheckid(Long id,Model m,int page,Integer type,HttpServletRequest request){
        Checkiteminfo checkiteminfo=checkInfoService.selectByPrimaryKey(id);
        List<Daterecordinfo> daterecordinfos = null;
        if(type == 3){
            daterecordinfos=dateRecordService.selectByExample(2);
        }else if (type == 2){
            daterecordinfos=dateRecordService.selectByExample(1);
        }
        List<Siteareainfo> siteareainfos=siteService.queryAllSite();
        m.addAttribute("siteareainfos",siteareainfos);
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        m.addAttribute("ad",admininfo);

        m.addAttribute("checkiteminfo",checkiteminfo);
        m.addAttribute("daterecordinfos",daterecordinfos);
        m.addAttribute("page",page);
        return "updatecheckiteminfo";
    }
    @RequestMapping("updcheck")
    public String updcheck(Checkiteminfo checkiteminfo,HttpSession session,int page){
        int updresult=checkInfoService.updateByPrimaryKeySelective(checkiteminfo);
        //获取登录用的信息

        String info="修改了巡检项"+checkiteminfo.getCustomid()+"的基本信息";
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);

        if(updresult>0&&addlog>0){
            return "redirect:showallcheck?page="+page;
        }
        return "updatecheckiteminfo";
    }
    @RequestMapping("querycheckdetail")/*
    @RequiresPermissions("item:checkdetail")*/
    public String querycheckdetail(Long id,Model m){
        Checkiteminfo checkiteminfo=checkInfoService.selectByPrimaryKey(id);
        m.addAttribute("checkiteminfo",checkiteminfo);
        return "showcheckitemdetail";
    }
    @RequestMapping("getChecknameByid")
    @ResponseBody
    public int getChecknameByid(Long recordid){
        return checkInfoService.selectByExample2(recordid);
    }


    //查询是否有使用的巡检项
    @RequestMapping("selectUsrCheckItem")
    @ResponseBody
    public int selectUsrCheckItem(Long id){
       return checkInfoService.selectByExample3(id);
    }
}
