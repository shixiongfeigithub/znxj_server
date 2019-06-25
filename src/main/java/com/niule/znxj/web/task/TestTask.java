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
    //发送日报
    @Scheduled(cron = "0 0 0 * * ?" )
//    @Scheduled(cron = "0 0/1 13 * * ?" )
    public void senddayemail() {
        commonService.senddayreport();
    }
    //发送周报
    @Scheduled(cron = "0 0 0 ? * MON" )
//      @Scheduled(cron = "0 0/1 13 * * ?" )
    public void sendweekemail() {
        commonService.sendweekemail();
    }
    //发送月报
    @Scheduled(cron = "0 0 0 1 * ?")
//    @Scheduled(cron = "0 0/1 13 * * ?" )
    public void sendmonthemail() {
        commonService.sendmonthemail();
    }
}
