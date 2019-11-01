package com.niule.znxj.web.controller;

import com.niule.znxj.core.util.PageBean;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.model.response.ShowReportParam;
import com.niule.znxj.web.service.AreaService;
import com.niule.znxj.web.service.EquipmentService;
import com.niule.znxj.web.service.SiteService;
import com.niule.znxj.web.service.TaskreportService;
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
    public String showexceptionreport(Model m, int page, Integer siteid, Integer areaid, Integer equipid,String operationstate,
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
        List<Siteareainfo> siteareainfos = siteService.selectByExample3(siteids);
        List<Areainfo> areainfos = areaService.selectByExample1(siteid);
        List<Equipmentinfo> equipmentinfos = equipmentService.queryequip(areaid);

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
        map.put("tasktypes",tasktypeList);
        map.put("time1", time1);//开始时间
        map.put("time2", time2);//结束时间
        map.put("siteids", siteids);
        map.put("areaid",areaid);
        map.put("equipmentid",equipid);
        map.put("worker",worker);
        List<Integer> operationstates = new ArrayList<>();
        operationstates.add(1); //漏检
        operationstates.add(2); //跳检
        operationstates.add(4); //超时
        map.put("operationstates", operationstates);

        taskreportinfos = taskreportService.findByPageReport3(map);
        int rows2 = taskreportService.countReport3(map);
        totalpage = PageBean.counTotalPage(pagesize, rows2);

        pageBean.setList(taskreportinfos);
        pageBean.setTotalPage((int) totalpage);
        pageBean.setCurrentPage(page);
        m.addAttribute("pageBean", pageBean);
        m.addAttribute("roleid", admininfo.getRoleid());
        m.addAttribute("sites", siteareainfos);
        m.addAttribute("siteid", siteid);
        m.addAttribute("areainfos", areainfos);
        m.addAttribute("areaid", areaid);
        m.addAttribute("equipmentinfos", equipmentinfos);
        m.addAttribute("equipid", equipid);
        m.addAttribute("operationstate", operationstate);
        m.addAttribute("worker",worker);
        m.addAttribute("time1", time1);
        m.addAttribute("time2", time2);



        /*ShowReportParam param = new ShowReportParam(page, tasktype, taskCcode, reportSstate, time1, time2, operationstate, siteid, searchtype);
        request.getSession().setAttribute("reportParam", param);*/
        return "showexceptionreport";
    }

    public PageBean getPageBean() {
        return pageBean;
    }
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }
}
