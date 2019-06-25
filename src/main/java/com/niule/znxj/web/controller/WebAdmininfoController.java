package com.niule.znxj.web.controller;

import com.github.pagehelper.PageInfo;
import com.niule.znxj.web.model.Admininfo;
import com.niule.znxj.web.model.Operatelog;
import com.niule.znxj.web.model.Roles;
import com.niule.znxj.web.model.Siteareainfo;
import com.niule.znxj.web.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by administor on 2017/3/16.
 */
@Controller
public class WebAdmininfoController {
    @Resource
    private AdmininfoService admininfoService;
    @Resource
    private RolesService rolesService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private SiteService siteService;
    @Resource
    private OperateLogService operateLogService;
    /**
     * 用户登录
     * @param model
     * @param admininfo
     * @return
     */
    @RequestMapping("/admin/login")
    public String login(Model model, Admininfo admininfo, HttpServletRequest request ){
        try {
            Subject subject = SecurityUtils.getSubject();
            // 已登录则 跳到首页
            if (subject.isAuthenticated()) {
                return "redirect:/";
            }
            // 身份验证
            subject.login(new UsernamePasswordToken(admininfo.getUsername(), admininfo.getPassword()));
            // 验证成功在Session中保存用户信息
            final Admininfo authUserInfo = admininfoService.login(admininfo);
            String info="用户"+authUserInfo.getUsername()+"已登录";
            String username=authUserInfo.getUsername();
            int addlog=operateLogService.insertSelective(username,info);
            request.getSession().setAttribute("userInfo", authUserInfo);
        } catch (AuthenticationException e) {
            // 身份验证失败
            model.addAttribute("error", "用户名或密码错误 ！");
            return "login";
        }
        return "redirect:/";
    }
    /**
     * 显示所有管理员信息
     * @param m
     * @return
     */
    @RequestMapping(value = "/showadmin",method = RequestMethod.GET)
    @RequiresPermissions("item:admin")
    public String showadmin( int page,Model m){
        int size=15;
        PageInfo<Admininfo> admininfos=null;
        admininfos=admininfoService.selectByExample(page,size);
        m.addAttribute("admininfos",admininfos);
        return "showadmin";
    }

    @RequestMapping("/toaddadmin")
    @RequiresPermissions("add:admin")
    public String toaddadmin(Model model) {
        List<Roles> roles=rolesService.selectByExample1();
        List<Siteareainfo> siteareainfos=siteService.queryAllSite();

        model.addAttribute("roles",roles);
        model.addAttribute("siteareainfos",siteareainfos);
        return "addadmin";
    }
    /**
     * 添加新的管理员信息
     * @param model
     * @param admininfo
     * @return
     */
    @RequestMapping("/addadmin")
    public String addadmin(Admininfo admininfo,HttpSession session,Model model) {
        int addresult=admininfoService.insert(admininfo);

        String info="新增了管理员"+admininfo.getUsername();
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);
        if(addresult>0&&addlog>0){
            return "redirect:showadmin?page=1";
        }else
            return "addadmin";
    }
    /**
     * 删除管理员
     * @param id
     * @return
     */
    @RequestMapping("/deladmin")
    @RequiresPermissions("del:admin")
    @ResponseBody
    public int deladmin(Long id,HttpServletRequest request) throws UnsupportedEncodingException {
       String username=request.getParameter("username");
        //删除管理员
        int deladmin=admininfoService.deleteByPrimaryKey(id);
        //添加操作日志
        HttpSession session=request.getSession();

        String info="删除了管理员"+username+"的信息";
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username1=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username1,info);
        if(deladmin>0&&addlog>0){
            return 1;
        }else
            return 0;
    }
    /**
     * 查询管理员的详细资料
     * @param id
     * @param m
     * @return
     */
    @RequestMapping("/selectbyid")
    @RequiresPermissions("upd:admin")
    public String selectbyid(Long id,Model m){
        List<Roles> roles=rolesService.selectByExample1();
        Admininfo ad=admininfoService.selectByPrimaryKey(id);
        List<Siteareainfo> siteareainfos=siteService.queryAllSite();

        if(ad!=null)
            m.addAttribute("ad",ad);
        m.addAttribute("roles",roles);
        m.addAttribute("siteareainfos",siteareainfos);
        return "updateadmin";
    }
    @RequestMapping("/updateadmin")
    public String updateadmin(Admininfo admininfo,HttpSession session,HttpServletRequest request){
        int updresult=admininfoService.updateByPrimaryKeySelective(admininfo);
        //获取登录用的信息
        String info="修改了管理员"+admininfo.getUsername()+"的基本信息";
        Admininfo logadmininfo=(Admininfo)session.getAttribute("userInfo");
        String username=logadmininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);

        if(updresult>0&&addlog>0){
            return "redirect:showadmin?page=1";
        }
        return "updateadmin";
    }
    @RequestMapping("/selectbyadminid")
   /* @RequiresPermissions("item:admindetail")*/
    public String selectbyadminid(Long id,Model m){
        Admininfo adm=admininfoService.selectByPrimaryKey(id);
        if(adm!=null)
            m.addAttribute("adm",adm);
        return "showadmindetail";
    }

    @RequestMapping("queryexistname")
    @ResponseBody
    public String queryexistname(String username){
        Admininfo admininfo= admininfoService.getexistuname(username);
        if(admininfo==null){
            return "0";
        }else{
            return "1";
        }
    }
    @RequestMapping("logout")
    public String logout(HttpSession session) {
        Admininfo admininfo=(Admininfo) session.getAttribute("userInfo");
        String info="用户"+admininfo.getUsername()+"已退出";
        String username=admininfo.getUsername();
        int addlog=operateLogService.insertSelective(username,info);

        session.removeAttribute("userInfo");
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
}
