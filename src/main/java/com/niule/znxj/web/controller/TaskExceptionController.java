package com.niule.znxj.web.controller;

import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.model.response.ShowReportParam;
import com.niule.znxj.web.service.AreaService;
import com.niule.znxj.web.service.EquipmentService;
import com.niule.znxj.web.service.SiteService;
import com.niule.znxj.web.service.TaskreportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuqb
 * Date: 2019-11-01 10:33
 */
@Controller
public class TaskExceptionController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private AreaService areaService;

    @Resource
    private EquipmentService equipmentService;

    @Autowired
    private TaskreportService taskreportService;

    protected PageBean pageBean = new PageBean();


    /**
     * 分页显示所有任务异常的报告 - 列表
     */
    @RequestMapping("/showexceptionreport")
    public String showexceptionreport(Model m, int page, Integer tasktype,String taskcode,Integer siteid,String operationstate,
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
        if(tasktype!=null){
            tasktypeList.add(tasktype);
        }else {
            tasktypeList.add(0); //日常巡检
            tasktypeList.add(1); //计划巡检
            tasktypeList.add(3); //视频巡检
        }
        map.put("tasktypes",tasktypeList);//任务类型
        map.put("taskcode", taskcode); //任务号
        map.put("reportstate", 1); //异常报告
        map.put("time1", time1);//开始时间
        map.put("time2", time2);//结束时间
        map.put("siteids", siteids);//厂区
        map.put("worker",worker); //任务执行者

        List<Integer> stateList = new ArrayList<>();
            if (!operationstate.isEmpty() && operationstate.equals("5")) {
                stateList.add(3);
                map.put("stopstate", 1);
                map.put("operationstate", null);
            } else {
                if (operationstate.isEmpty()) {
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
        m.addAttribute("tasktype", tasktype);
        m.addAttribute("taskcode", taskcode);
        m.addAttribute("siteid", siteid);
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);
        m.addAttribute("operationstate", operationstate);
        m.addAttribute("sites", siteareainfos);
        m.addAttribute("roleid", admininfo.getRoleid());
        ShowReportParam param = new ShowReportParam(page, tasktype, taskcode, "1", time1, time2, operationstate, Long.valueOf(siteid), tasktype);
        request.getSession().setAttribute("reportParam", param);
        return "showexceptionreport";
    }

    public PageBean getPageBean() {
        return pageBean;
    }
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
