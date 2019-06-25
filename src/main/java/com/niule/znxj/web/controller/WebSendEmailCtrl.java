package com.niule.znxj.web.controller;

import com.github.pagehelper.PageInfo;
import com.niule.znxj.web.model.Contactinfo;
import com.niule.znxj.web.model.Sendemail;
import com.niule.znxj.web.model.Taskplaninfo;
import com.niule.znxj.web.model.TaskplaninfoExample;
import com.niule.znxj.web.service.ContactinfoService;
import com.niule.znxj.web.service.SendEmailService;
import com.niule.znxj.web.service.TaskPlanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by administor on 2017/6/21.
 */
@Controller
public class WebSendEmailCtrl {
    @Resource
    private SendEmailService sendEmailService;
    @Resource
    private ContactinfoService contactinfoService;
    @Resource
    private TaskPlanService taskPlanService;

    @RequestMapping("showsendemaillist")
    public String showsendemaillist(int page,Model m){
        if(page<=0){page=1;}
        int size=15;
        PageInfo pageInfo=new PageInfo<>(sendEmailService.showSendEmail(page,size));
        m.addAttribute("pageInfo",pageInfo);
        return "showsendemail";
    }
    @RequestMapping("addsendemail")
    public String addsendemail(String selectedpersion,Sendemail sendemail){
        sendemail.setContactid(selectedpersion);
        int result= sendEmailService.addSendEmail(sendemail);
        if(result>0){
            return "redirect:showsendemaillist?page=1";
        }else
            return"redirect:sendpersion";

    }
    @RequestMapping("querysendbyid")
    public String querysendbyid(Long id,Model m){
        Sendemail sendemail=sendEmailService.showSendById(id);
        List<Contactinfo> contactinfos=contactinfoService.sendAllPersion();
        TaskplaninfoExample taskplaninfoExample=new TaskplaninfoExample();
        taskplaninfoExample.createCriteria().andStateNotEqualTo(-1);
        List<Taskplaninfo> taskplaninfos=taskPlanService.selectByExample(taskplaninfoExample);

        if(sendemail!=null){
            m.addAttribute("editsendemail",sendemail);
            m.addAttribute("taskplaninfos",taskplaninfos);
            m.addAttribute("contactinfos",contactinfos);
            return "updsendemail";
        }else
            return "redirect:showsendemaillist?page=1";
    }
    @RequestMapping("updsendemail")
    public String updsendemail(String selectedpersion,Sendemail sendemail){
        sendemail.setContactid(selectedpersion);
        int result=sendEmailService.updSendEmail(sendemail);
        if(result>0){
            return "redirect:showsendemaillist?page=1";
        }else
            return"redirect:querysendbyid?id="+sendemail.getId();
    }
    @RequestMapping("delsendemail")
    @ResponseBody
    public int delsendemail(Long id){
        return sendEmailService.delSendEmail(id);
    }
}
