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
public class WebInterfaceController {
    @Resource
    private InterfaceService interfaceService;
    @Resource
    private SiteService siteService;
    @Resource
    private OperateLogService operateLogService;

    protected PageBean pageBean = new PageBean();

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    /**
     * 显示接口列表
     * @param page 当前页
     * @param m
     * @param request
     * @return
     */
    @RequestMapping("showallinterface")
    @RequiresPermissions("item:interface")
    public String showallinterface(int page, Model m, HttpServletRequest request){
        if(page<=0){
            page=1;
        }
        int pagesieze=15;
        page = PageBean.countCurrentPage(page);
        List<Interfaceengine> engines=null;
        long totalpage=0;

        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List ids=new ArrayList() ;
        if(admininfo.getSiteid()==null){
            List<Siteareainfo> sites=siteService.queryAllSite();
            if(sites.size()>0){
                for(Siteareainfo siteareainfo:sites){
                    ids.add(siteareainfo.getId());
                }
            }else{
                ids=null;
            }
        }else{
            ids.add(admininfo.getSiteid());
        }
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("page",(page-1)*pagesieze);
        map.put("size",pagesieze);
        map.put("ids",ids);
        engines = interfaceService.selectAllListBySite(map);
        int rows = interfaceService.countInterface(map);
        totalpage = PageBean.counTotalPage(pagesieze, rows);
        pageBean.setList(engines);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean",pageBean);
        return "showinterface";
    }

    /**
     * 跳转到添加接口页面
     * @param m
     * @param request
     * @return
     */
    @RequestMapping("toaddinterface")
    public String toaddinterface(Model m,HttpServletRequest request){
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> siteareainfos=siteService.selectByExample2(admininfo.getSiteid());
        m.addAttribute("siteareainfos",siteareainfos);
        return "addinterface";
    }

    /**
     * 添加接口
     * @param engine
     * @param session
     * @return
     */
    @RequestMapping("addinterface")
    public String addinterface(Interfaceengine engine, HttpSession session){
        engine.setCreatettime(new Date());
        int addresult=interfaceService.insert(engine);
        //获取登录用户的信息
        String info="新增了接口配置："+engine.getIp()+" "+engine.getPort();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);

        if(addresult>0&&addlog>0){
            return "redirect:showallinterface?page=1";
        }
        return "addinterface";
    }

    /**
     * 删除接口
     * @param id ID
     * @param request
     * @param session
     * @return
     */
    @RequestMapping("delinterface")
    @RequiresPermissions("del:interface")
    @ResponseBody
    public int delinterface(Long id,HttpServletRequest request,HttpSession session){
        int delinterface=interfaceService.deleteByPrimaryKey1(id);
        //获取登录用户的信息
        String info="删除了接口配置";
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(delinterface>0&&addlog>0){
            return 1;
        }else
            return 0;
    }
    @RequestMapping("findbyinterfaceid")
    @RequiresPermissions("upd:interface")
    public String findbyinterfaceid(Long id,Model m,HttpServletRequest request,int page){
        Interfaceengine interfaceengine =interfaceService.selectByPrimaryKey(id);
        Admininfo admininfo=(Admininfo) request.getSession().getAttribute("userInfo");
        List<Siteareainfo> siteareainfos=siteService.selectByExample2(admininfo.getSiteid());

        m.addAttribute("siteareainfos",siteareainfos);
        m.addAttribute("engine",interfaceengine);
        m.addAttribute("page",page);
        return "updateinterface";
    }
    @RequestMapping("updinterface")
    public String updinterface(Interfaceengine engine, Model model,HttpSession session,int page){
        Long id=engine.getId();
        engine.setUpdatetime(new Date());
        int updresult =interfaceService.updateByPrimaryKeySelective(engine);
        //获取登录用户的信息
        String info="修改了接口配置"+engine.getIp()+" "+engine.getPort();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(updresult>0 && addlog>0){
            return "redirect:showallinterface?page="+page;
        }
        return "redirect:findbyinterfaceid?id="+ id+"&page="+page;
    }
}