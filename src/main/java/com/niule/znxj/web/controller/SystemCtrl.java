package com.niule.znxj.web.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.niule.znxj.web.model.Systemsettinginfo;
import com.niule.znxj.web.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by administor on 2017/5/16.
 */
@Controller
public class SystemCtrl {
    @Resource
    private SystemService systemService;

    @RequestMapping("showsystem")
    public String showsystem(Model m,int type){
        List<Systemsettinginfo> systems=systemService.selectByExample();
        for(Systemsettinginfo info : systems){
            m.addAttribute(info.getKeyname(),info.getValue());
        }
        String page="";
        if(type==1){
            page= "system";
        }
        if(type==2){
            page= "system2";
        }
//        if(type==3) {
//            page= "email";
//        }
        if(type==4) {
            page= "imgserver";
        }
        m.addAttribute("type",type);
        return page;
    }
    @RequestMapping("updsystem")
    @ResponseBody
    public int updsystem(String REMEMBER_NAME,String REMEMBER_PSW,String SHOW_COPYRIGHT,String COPYRIGHT_CONTENT,
                         String REPORTCACHE,String WIFIUPLOAD,String EMAIL,String EMAILNUM,String EMAILPWD,
                         String IMGSERVER,String STORAGEPATH,String SYSTEMNAME,String SYSTEMDESC,int type){
        String key1="REMEMBER_NAME";
        String key2="REMEMBER_PSW";
        String key3="SHOW_COPYRIGHT";
        String key4="COPYRIGHT_CONTENT";
        String key5="REPORTCACHE";
        String key6="WIFIUPLOAD";
        String key7="EMAIL";
        String key8="EMAILNUM";
        String key9="EMAILPWD";
        String key10="IMGSERVER";
        String key11="STORAGEPATH";
        String key12="SYSTEMNAME";
        String key13="SYSTEMDESC";
        int result=0;
        if(type==1){
            int updresult1=systemService.updSysByKey(key1,REMEMBER_NAME);
            int updresult2=systemService.updSysByKey(key2,REMEMBER_PSW);
            int updresult3=systemService.updSysByKey(key3,SHOW_COPYRIGHT);
            int updresult4=systemService.updSysByKey(key4,COPYRIGHT_CONTENT);
            int updresult5=systemService.updSysByKey(key5,REPORTCACHE);
            int updresult6=systemService.updSysByKey(key6,WIFIUPLOAD);
            if(updresult1>0&&updresult2>0&&updresult3>0&updresult4>0&&updresult5>0&&updresult6>0){
                result=1;
                return result;
            }
        }
        if(type==2){
            int updresult12=systemService.updSysByKey(key12,SYSTEMNAME);
            int updresult13=systemService.updSysByKey(key13,SYSTEMDESC);
            if(updresult12>0&&updresult13>0){
                result=1;
                return result;
            }
        }
        if(type==3){
            int updresult7=systemService.updSysByKey(key7,EMAIL);
            int updresult8=systemService.updSysByKey(key8,EMAILNUM);
            int updresult9=systemService.updSysByKey(key9,EMAILPWD);
            if(updresult7>0&&updresult8>0&&updresult9>0){
                result=1;
                return result;
            }
        }
        if(type==4){
            int updresult10=systemService.updSysByKey(key10,IMGSERVER);
            int updresult11=systemService.updSysByKey(key11,STORAGEPATH);
            if(updresult10>0&&updresult11>0){
                result=1;
                return result;
            }
        }
        return result;
    }
}
