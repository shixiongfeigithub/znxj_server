package com.niule.znxj.web.task;

import com.niule.znxj.web.service.CommonService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by MrD on 2017/3/27.
 */
@Component("taskJob")
public class TestTask {
    @Resource
    private CommonService commonService;

    /**
     * 凌晨0点 触发生成当天任务
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dojob() {
        commonService.generate();
    }

    //发送日报(每日凌晨0点)
    @Scheduled(cron = "0 0 0 * * ?")
    public void senddayemail() {
        commonService.senddayreport();
    }

    //发送周报（每周一凌晨0点）
    @Scheduled(cron = "0 0 0 ? * MON")
    public void sendweekemail() {
        commonService.sendweekemail();
    }

    //发送月报（每月一号凌晨0点）
    @Scheduled(cron = "0 0 0 ? * ?")
    public void sendmonthemail() {
        commonService.sendmonthemail();
    }

    //自动复核（每分钟执行一次）
    @Scheduled(cron = "0 */1 * * * ?")
    public void automaticExamine() {
        commonService.automaticExamine();
    }

    //上传巡检记录（一小时执行一次）
    @Scheduled(cron = "0 */30 * * * ?")
    public void uploadTaskReportInfo() {
        commonService.uploadTaskReportInfo();
    }

    //上传巡检异常记录（每个小时的第15分执行）
    @Scheduled(cron = "0 15,45 * * * ?")
    public void uploadExceptionReport() {
        commonService.uploadExceptionReport();
    }

}
