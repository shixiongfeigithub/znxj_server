package com.niule.znxj.web.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.niule.znxj.core.entity.JSONResult;
import com.niule.znxj.core.entity.Result;
import com.niule.znxj.core.util.DateUtils;
import com.niule.znxj.core.util.EmailUtils;
import com.niule.znxj.core.util.StringUtils;
import com.niule.znxj.core.util.json.JsonUtil;

import com.niule.znxj.web.dao.*;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.model.response.TaskSimpleReport;
import com.niule.znxj.web.model.taskcontent.TaskArea;
import com.niule.znxj.web.model.taskcontent.TaskContent;
import com.niule.znxj.web.model.taskcontent.TaskEquipment;
import com.niule.znxj.web.model.taskresponse.AreaRes;
import com.niule.znxj.web.service.CommonService;
import com.niule.znxj.web.service.SendEmailService;
import com.niule.znxj.web.task.PopupAuthenticator;
import com.sun.mail.util.MailSSLSocketFactory;
import com.sun.scenario.effect.impl.sw.sse.SSESepiaTonePeer;
import net.sf.json.JSONArray;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by MrD on 2017/3/22.
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private UserinfoMapper userinfoMapper;
    @Resource
    private SystemsettinginfoMapper systemsettinginfoMapper;
    @Resource
    private TaskplaninfoMapper taskplaninfoMapper;
    @Resource
    private TasktempinfoMapper tasktempinfoMapper;
    @Resource
    private QuickreportMapper quickreportMapper;
    @Resource
    private TaskreportinfoMapper taskreportinfoMapper;
    @Resource
    private WarningtasktypeMapper warningtasktypeMapper;
    @Resource
    private TaskstopinfoMapper taskstopinfoMapper;
    @Resource
    private AppversionMapper appversionMapper;
    @Resource
    private KnowledgetypeMapper knowledgetypeMapper;
    @Resource
    private KnowledgeMapper knowledgeMapper;
    @Resource
    private SendEmailService sendEmailService;
    private String ip = "218.78.221.179:8080";

    @Override
    public Userinfo userLogin(String username, String password) {
        return userinfoMapper.authUser(username, password);
    }

    @Resource
    private ReportcontentMapper reportcontentMapper;
    @Resource
    private ContactinfoMapper contactinfoMapper;

    @Override
    public Result getLoginConfig() {
        SystemsettinginfoExample systemsettinginfoExample = new SystemsettinginfoExample();
        systemsettinginfoExample.createCriteria().andKeynameIn(Arrays.asList("REMEMBER_NAME", "REMEMBER_PSW", "SHOW_COPYRIGHT", "COPYRIGHT_CONTENT", "REPORTCACHE", "WIFIUPLOAD"));
        return new JSONResult<>(systemsettinginfoMapper.selectByExample(systemsettinginfoExample));
    }

    @Override
    public Result getStopReason(Long tasktempid) {
        TaskstopinfoExample taskstopinfoExample = new TaskstopinfoExample();
        taskstopinfoExample.createCriteria().andTasktempidEqualTo(tasktempid);
        return new JSONResult<>(taskstopinfoMapper.selectByExample(taskstopinfoExample));
    }

    @Override
    public Result getExceptionReport(String taskCode) {
        TaskreportinfoExample example = new TaskreportinfoExample();
        example.createCriteria().andTaskcodeEqualTo(taskCode);

        String content = "{" + "\"res\":" + taskreportinfoMapper.selectByExample(example).get(0).getContent() + "}";
        TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);

        ArrayList list = new ArrayList();

        for (int i = 0; i < res.getRes().size(); i++) {
            String ss = res.getRes().get(i).getReportstate();
            if (ss.equals("1")) {
                TaskReportContent aa = res.getRes().get(i);
                list.add(aa);
            }
        }
        return new JSONResult<>(list);
    }

    @Override
    public Result getLastestAppVersion() {
        return new JSONResult<>(appversionMapper.getLastestAppVersion());
    }

    @Override
    public Result getTaskSimpleReport(String taskCode, Long taskid) {
        int areaCnt = 0, equipmentCnt = 0, itemCnt = 0, errorCnt = 0;
        TaskSimpleReport simpleReport = new TaskSimpleReport();
        //获取任务
        /*TaskplaninfoExample example=new TaskplaninfoExample();
        example.createCriteria().andIdEqualTo(taskid);*/
        Taskplaninfo taskplaninfo = taskplaninfoMapper.selectByPrimaryKey(taskid);
        TaskContent taskcontent = JsonUtil.toObject(taskplaninfo.getTaskcontent(), TaskContent.class);
        //赋值任务名
        simpleReport.setTaskname(taskplaninfo.getCustomid());
        //区域数
        areaCnt = taskcontent.getAreas().size();
        for (TaskArea area : taskcontent.getAreas()) {
            //设备数
            equipmentCnt += area.getEquipments().size();
            //巡检项数
            for (TaskEquipment equipment : area.getEquipments()) {
                itemCnt += equipment.getCheckItems().size();
            }
        }
        //设置区域数 设备数 项目数
        simpleReport.setAreaCnt(areaCnt);
        simpleReport.setEquipmentCnt(equipmentCnt);
        simpleReport.setItemCnt(itemCnt);

        //获取任务报告
        TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
        taskreportinfoExample.createCriteria().andTaskcodeEqualTo(taskCode);
        Taskreportinfo taskreportinfo = taskreportinfoMapper.selectByExample(taskreportinfoExample).get(0);
        if (taskreportinfo == null) {
            simpleReport.setUploadState(0);
        }
        String content = "{" + "\"res\":" + taskreportinfo.getContent() + "}";
        TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
        //异常数
        for (TaskReportContent reportContent : res.getRes()) {
            if ("1".equals(reportContent.getReportstate())) {
                errorCnt++;
            }
        }
        simpleReport.setErrorCnt(errorCnt);
        simpleReport.setTaskcode(taskCode);
        simpleReport.setStarttime(taskreportinfo.getStarttime());
        simpleReport.setEndtime(taskreportinfo.getEndtime());
        simpleReport.setExecuteState(taskreportinfo.getReportstate());
        simpleReport.setUploadState(1);
        return new JSONResult<>(simpleReport);
    }

    /**
     * 获取任务列表
     *
     * @param classId
     * @param type
     * @return
     */
    @Override
    public Result getTasks(Long userId, Long classId, Integer type, Integer state) {
        if (state == 0) {
            return new JSONResult<>(taskplaninfoMapper.getTakTemps(classId, type, state));
        } else
            return new JSONResult<>(taskplaninfoMapper.getTakTempsExecuting(classId, type, state, userId));
    }

    /**
     * 设置任务状态
     *
     * @param
     * @param state          执行状态 0 未执行 1 进行中 2 已完成
     * @param operationstate 0 未执行 1 漏检 2跳检 3正常 4超时
     * @param stopstate      0 无 1 主动终止 2 被动终止
     */
    @Override
    public Result setTaskState(Long userId, Long tempId, Integer state, Integer operationstate, Integer stopstate) {
        Tasktempinfo tasktempinfo = tasktempinfoMapper.selectByPrimaryKey(tempId);
        if (operationstate != 0) {
            return new JSONResult<>("操作失败，任务已上传，不能重复设置任务状态");
        }
        tasktempinfo.setState(state);
        tasktempinfo.setUserid(userId);
        tasktempinfo.setOperationstate(operationstate);
        tasktempinfo.setStopstate(stopstate);
        tasktempinfo.setUpdatetime(new Date());
        tasktempinfoMapper.updateByPrimaryKeySelective(tasktempinfo);
        return new JSONResult<>(state == 1 ? (JSONUtils.parse(taskplaninfoMapper.selectByPrimaryKey(tasktempinfo.getTaskid()).getTaskcontent())) : null);
    }

    /**
     * 任务终止
     *
     * @param userId
     * @param tempId
     * @param reason
     * @param content
     * @return
     */
    @Override
    public Result taskStop(Long userId, Long tempId, String reason, String content, String classname, String directorname) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Tasktempinfo tasktempinfo = tasktempinfoMapper.selectByPrimaryKey(tempId);
        if (tasktempinfo.getState() == 3)
            return new JSONResult<>("操作失败，请勿重复终止任务");
        tasktempinfo.setState(3);
        tasktempinfo.setUserid(userId);
        tasktempinfo.setUpdatetime(new Date());
        tasktempinfoMapper.updateByPrimaryKeySelective(tasktempinfo);
        Taskstopinfo info = new Taskstopinfo();
        info.setTasktempid(tempId);
        info.setReason(reason);
        info.setContent(content);
        info.setStoptime(new Date());
        info.setClassname(classname);
        info.setDirectorname(directorname);
        taskstopinfoMapper.insert(info);
        return new JSONResult<>();
    }


    /**
     * 生成任务附表
     *
     * @return
     */
    @Override
    public void generate() {
        TaskplaninfoExample example = new TaskplaninfoExample();
        example.createCriteria().andStateEqualTo(1);
        Tasktempinfo temp;
        for (Taskplaninfo info : taskplaninfoMapper.selectByExample(example)) {
            if (!info.getWeeklytime().contains(getWeekStr()))
                continue;
            List<String> timeList = (List<String>) JSONUtils.parse(info.getImplementtime());
            int i = 1;
            for (String str : timeList) {
                SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd");
                String code = info.getIdentifyingid() + "-" + info.getTaskversion() + "-" + dtf.format(new Date()) + "-" + i;
                //设置 当天执行时间 时 分
                //当前时间小于待执行时间  生成对应参数
                if (DateUtils.getCurrentHour() < StringUtils.getHours(str)) {
                    ++i;
                    temp = new Tasktempinfo(info.getId(), DateUtils.setTime(StringUtils.getHours(str), StringUtils.getMinutes(str)), 0, code, info.getType(), new Date());
                    tasktempinfoMapper.insertSelective(temp);
                } else if (DateUtils.getCurrentHour() == StringUtils.getHours(str) && DateUtils.getCurrentMinute() < StringUtils.getMinutes(str)) {
                    ++i;
                    temp = new Tasktempinfo(info.getId(), DateUtils.setTime(StringUtils.getHours(str), StringUtils.getMinutes(str)), 0, code, info.getType(), new Date());
                    tasktempinfoMapper.insertSelective(temp);
                }
            }
        }
    }

    /**
     * 查看任务的执行状态
     *
     * @param taskcode
     * @return
     */
    @Override
    public Result getTaskTempState(String taskcode) {
        TasktempinfoExample tasktempinfoExample = new TasktempinfoExample();
        tasktempinfoExample.createCriteria().andTaskcodeEqualTo(taskcode);
        return new JSONResult<>(tasktempinfoMapper.selectByExample(tasktempinfoExample));
    }

    /**
     * 生成任务附表
     *
     * @return
     */
    @Override
    public void generateByTask(Taskplaninfo info) {
        if (!info.getWeeklytime().contains(getWeekStr()))
            return;
        Tasktempinfo temp;
        List<String> timeList = (List<String>) JSONUtils.parse(info.getImplementtime());
        int i = 1;
        for (String str : timeList) {
            SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd");
            String code = info.getIdentifyingid() + "-" + info.getTaskversion() + "-" + dtf.format(new Date()) + "-" + i;
            //设置 当天执行时间 时 分
            //当前时间小于待执行时间  生成对应参数
            if (DateUtils.getCurrentHour() < StringUtils.getHours(str)) {
                ++i;
                temp = new Tasktempinfo(info.getId(), DateUtils.setTime(StringUtils.getHours(str), StringUtils.getMinutes(str)), 0, code, info.getType(), new Date());
                tasktempinfoMapper.insertSelective(temp);
            } else if (DateUtils.getCurrentHour() == StringUtils.getHours(str) && DateUtils.getCurrentMinute() < StringUtils.getMinutes(str)) {
                ++i;
                temp = new Tasktempinfo(info.getId(), DateUtils.setTime(StringUtils.getHours(str), StringUtils.getMinutes(str)), 0, code, info.getType(), new Date());
                tasktempinfoMapper.insertSelective(temp);
            }
        }
    }

    @Override
    public Result uploadReport(Taskreportinfo taskreportinfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TaskreportinfoExample example = new TaskreportinfoExample();
        example.createCriteria().andTaskcodeEqualTo(taskreportinfo.getTaskcode());
        taskreportinfoMapper.deleteByExample(example);
        TasktempinfoExample example1 = new TasktempinfoExample();
        example1.createCriteria().andTaskcodeEqualTo(taskreportinfo.getTaskcode());
        List<Tasktempinfo> taskplaninfoList = tasktempinfoMapper.selectByExample(example1);
        if (taskplaninfoList.size() == 0)
            return new JSONResult<>("操作失败，子任务不存在");
        Tasktempinfo tasktempinfo = tasktempinfoMapper.selectByExample(example1).get(0);
        if (tasktempinfo.getOperationstate() != 0) {
            return new JSONResult<>("操作失败，任务已上传，不能重复设置任务状态");
        }
        taskreportinfo.getTaskcode();
        Result result = taskreportinfoMapper.insert(taskreportinfo) == 1 ? new JSONResult<>() : new JSONResult<>("操作失败");
        String content = "{" + "\"res\":" + taskreportinfo.getContent() + "}";
        if (content != null) {
            TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
            for (TaskReportContent item : res.getRes()) {
                String checkitype = item.getChecktype();
                Float normalmin = item.getNormalmin();
                Float normalmax = item.getNormalmax();
                Float lowerwarning = item.getLowerwarning();
                Float upperwarning = item.getUpperwarning();
                String a = "";
                String e = "";
                String c = "";
                String d = "";
                if (checkitype.equals("记录项")) {
                    if (normalmin == 0 || normalmin == null) {
                        a = "-";
                    } else
                        a = normalmin + "";
                    if (normalmax == 0 || normalmax == null) {
                        e = "-";
                    } else
                        e = normalmax + "";
                    if (lowerwarning == 0 || lowerwarning == null) {
                        c = "-";
                    } else
                        c = lowerwarning + "";
                    if (upperwarning == 0 || upperwarning == null) {
                        d = "-";
                    } else
                        d = upperwarning + "";
                }
                if (checkitype.equals("状态项")) {
                    a = "-";
                    e = "-";
                    c = "-";
                    d = "-";
                }
                if (checkitype.equals("")) {
                    a = "-";
                    e = "-";
                    c = "-";
                    d = "-";
                }
                Reportcontent reportcontent = new Reportcontent(item.getAreaname(), item.getEquipname(), item.getCheckname(), item.getChecktype(), item.getNumvalue(),
                        item.getRecordname(), item.getUnitname(), item.getReportstate(), item.getErrcontent(), item.getAreaskip(), item.getEquipmentskip(),
                        item.getAreaskipdesc(), item.getEquipmentskipdesc(), sdf.format(item.getOperationtime()),
                        taskreportinfo.getId(), c, e, a, d, JsonUtil.toJSON(item.getVideo()), JsonUtil.toJSON(item.getAudio()), JsonUtil.toJSON(item.getImg()));
                reportcontent.setReportid(taskreportinfo.getId());
                reportcontentMapper.insert(reportcontent);
            }
        }
        return result;
    }

    @Override
    public Result uploadQuickReport(Quickreport quickreport) {
        return quickreportMapper.insertSelective(quickreport) == 1 ? new JSONResult<>() : new JSONResult<>("上传失败");
    }

    @Override
    public Result getWarningTypeOrLevel(int type) {
        WarningtasktypeExample example = new WarningtasktypeExample();
        example.createCriteria().andTypeEqualTo(type);
        return new JSONResult<>(warningtasktypeMapper.selectByExample(example));
    }

    @Override
    public List<Warningtasktype> getWarningTypeOrLevels(int type) {
        WarningtasktypeExample example = new WarningtasktypeExample();
        example.createCriteria().andTypeEqualTo(type);
        return warningtasktypeMapper.selectByExample(example);
    }

    public String getWeekStr() {
        String weekStr;
        Calendar c = Calendar.getInstance();
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                weekStr = "周一";
                break;
            case Calendar.TUESDAY:
                weekStr = "周二";
                break;
            case Calendar.WEDNESDAY:
                weekStr = "周三";
                break;
            case Calendar.THURSDAY:
                weekStr = "周四";
                break;
            case Calendar.FRIDAY:
                weekStr = "周五";
                break;
            case Calendar.SATURDAY:
                weekStr = "周六";
                break;
            case Calendar.SUNDAY:
                weekStr = "周日";
                break;
            default:
                weekStr = "周一";
                break;
        }
        return weekStr;
    }

    /**
     * 获取知识库类别
     *
     * @return
     */
    @Override
    public List<Knowledgetype> getKnowledgeType() {
        KnowledgetypeExample example = new KnowledgetypeExample();
        return knowledgetypeMapper.selectByExample(example);
    }

    /**
     * 根据类别获取相关的知识
     *
     * @param typeid 类别编号
     * @return
     */
    @Override
    public List<Knowledge> getKnowledge(int typeid) {
        return knowledgeMapper.getKnowledgeByTypeid(typeid);
    }

    @Override
    public List<Knowledge> getKnowledgeByParam(String str) {
        return knowledgeMapper.getKnowledgeByParam("%" + str + "%");
    }

    @Override
    public List<Tasktempinfo> selectByExample(Long taskid) {
        TasktempinfoExample example = new TasktempinfoExample();
        example.createCriteria().andTaskidEqualTo(taskid);
        return tasktempinfoMapper.selectByExample(example);
    }

    //    @Override
//    public void senddayreport(){
//        try{
//            List<Contactinfo> contactinfolist=queryContactEmail("0");
//            //获取发送人邮箱
//            SystemsettinginfoExample system1=new SystemsettinginfoExample();
//            system1.createCriteria().andKeynameEqualTo("EMAILNUM");
////            String userName=systemsettinginfoMapper.selectByExample(system1).get(0).getValue();
//            String userName="1021490876@qq.com";
//            //获取发送人邮箱授权码
//            SystemsettinginfoExample system2=new SystemsettinginfoExample();
//            system2.createCriteria().andKeynameEqualTo("EMAILPWD");
////            String password=systemsettinginfoMapper.selectByExample(system2).get(0).getValue();
//
//
//            String password="yyeuyscxttxybcbi";
//            String smtp_server="smtp.qq.com";
//            String from_mail_address=userName;//发件人邮箱
//            String to_mail_address="474711345@qq.com";//发送对象
////            for(Contactinfo item:contactinfolist){
////                to_mail_address=item.getEmail();
////            }
//            Authenticator auth=new PopupAuthenticator(userName,password);
//            Properties mailProps=new Properties();
//            mailProps.put("mail.smtp.host", smtp_server);
//            mailProps.put("mail.smtp.auth", "true");
//            mailProps.put("username", userName);
//            mailProps.put("password", password);
//
//            MailSSLSocketFactory sf = new MailSSLSocketFactory();
//            sf.setTrustAllHosts(true);
//            mailProps.put("mail.smtp.ssl.enable", "true");
//            mailProps.put("mail.smtp.ssl.socketFactory", sf);
//
//            Session mailSession=Session.getDefaultInstance(mailProps, auth);
//            mailSession.setDebug(true);
//            MimeMessage message=new MimeMessage(mailSession);
//            message.setFrom(new InternetAddress(from_mail_address));
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to_mail_address));
//            message.setSubject("dayreport");
//
//            MimeMultipart multi=new MimeMultipart();
//            BodyPart textBodyPart=new MimeBodyPart();
//            Date date=new Date();
//            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//            String date1=sdf.format(date);
//            textBodyPart.setText("http://"+ip+"/dadiyreport?date1="+date1);
//            multi.addBodyPart(textBodyPart);
//            message.setContent(multi);
//            message.saveChanges();
//            Transport.send(message);
//
//        }catch(Exception ex){
//            System.err.println("邮件发送失败的原因是："+ex.getMessage());
//            System.err.println("具体的错误原因");
//            ex.printStackTrace(System.err);
//        }
//    }
    @Override
    public void senddayreport() {
        try {
            //获取发件人邮箱和授权码
            List<Sendemail> sendemails = querySendEmailByType(0);
            for (Sendemail send : sendemails) {
                //获取要发送的邮箱列表
                String[] contactidsstr = send.getContactid().split(",");
                List<Long> contactids = new ArrayList<>();
                for (String contactid : contactidsstr) {
                    contactids.add(Long.parseLong(contactid));
                }
                List<Contactinfo> contactinfolist = queryContactEmail(contactids);
                List<String> emails = new ArrayList<>();
                for (Contactinfo item : contactinfolist) {
                    emails.add(item.getEmail());
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date time1 = DateUtils.getWholePointDate(1);
                String date1 = sdf.format(time1);
                String content = "http://" + ip + "/dadiyreport?date1=" + date1 + "&taskidstr=" + send.getTaskid();
                EmailUtils.sendEmails(send.getEmail(), send.getPwd(), send.getSmtpAddress(), send.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "dayreport", content);
            }
//            List<String> contidstr=new ArrayList<>();
//
//            String taskids="";
//            //获取发送人邮箱授权码
//            for(int i=0;i<sendemails.size();i++){
//                contidstr.add(sendemails.get(i).getContactid());
//                taskids+=sendemails.get(i).getTaskid()+",";
//            }
//            for(int j=0;j<contidstr.size();j++){
//                String [] stringArr=contidstr.get(j).split(",");
//                for(int k=0;k<stringArr.length;k++){
//                    String aa=stringArr[k];
//                    contids.add(Long.valueOf(stringArr[k]));
//                }
//            }
//            List<Contactinfo> contactinfolist=queryContactEmail(contids);
//            String userName="";
//            String password="";
//            for(int i=0;i<sendemails.size();i++){
//                userName=sendemails.get(i).getEmail();
//                password=sendemails.get(i).getPwd();
////                userName="1021490876@qq.com";
////                password="yyeuyscxttxybcbi";
//                String smtp_server="smtp.qq.com";
//                String from_mail_address=userName;//发件人邮箱
//                List<String> to_mail_address=new ArrayList<>();//发送对象
////                for(Contactinfo item:contactinfolist){
////                    to_mail_address.add(item.getEmail());
////                }
//                Authenticator auth=new PopupAuthenticator(userName,password);
//                Properties mailProps=new Properties();
//                mailProps.put("mail.smtp.host", smtp_server);
//                mailProps.put("mail.smtp.auth", "true");
//                mailProps.put("username", userName);
//                mailProps.put("password", password);
//
//                MailSSLSocketFactory sf = new MailSSLSocketFactory();
//                sf.setTrustAllHosts(true);
//                mailProps.put("mail.smtp.ssl.enable", "true");
//                mailProps.put("mail.smtp.ssl.socketFactory", sf);
//
//                Session mailSession=Session.getDefaultInstance(mailProps, auth);
//                mailSession.setDebug(true);
//                MimeMessage message=new MimeMessage(mailSession);
//                message.setFrom(new InternetAddress(from_mail_address));
//                for(int j=0;j<to_mail_address.size();j++){
//                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(to_mail_address.get(j)));
////                    message.setRecipient(Message.RecipientType.TO, new InternetAddress("474711345@qq.com"));
//                }
//                message.setSubject("dayreport");
//                MimeMultipart multi=new MimeMultipart();
//                BodyPart textBodyPart=new MimeBodyPart();
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//                Date time1=DateUtils.getWholePointDate(1);
//                String date1=sdf.format(time1);
//                textBodyPart.setText("http://"+ip+"/dadiyreport?date1="+date1+"&taskidstr="+ taskids);
//                multi.addBodyPart(textBodyPart);
//                message.setContent(multi);
//                message.saveChanges();
//                Transport.send(message);
//            }
        } catch (Exception ex) {
            System.err.println("邮件发送失败的原因是：" + ex.getMessage());
            System.err.println("具体的错误原因");
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public void sendweekemail() {
        try {
            List<Sendemail> sendemails = querySendEmailByType(1);
            for (Sendemail send : sendemails) {
                //获取要发送的邮箱列表
                String[] contactidsstr = send.getContactid().split(",");
                List<Long> contactids = new ArrayList<>();
                for (String contactid : contactidsstr) {
                    contactids.add(Long.parseLong(contactid));
                }
                List<Contactinfo> contactinfolist = queryContactEmail(contactids);
                List<String> emails = new ArrayList<>();
                for (Contactinfo item : contactinfolist) {
                    emails.add(item.getEmail());
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date time1 = DateUtils.getWholePointDate(8);
                String date1 = sdf.format(time1);
                Date time2 = DateUtils.getWholePointDate(1);
                String date2 = sdf.format(time2);
                String content = "http://" + ip + "/weekreport?date1=" + date1 + "&date2=" + date2 + "&taskidstr=" + send.getTaskid() + "&type=0";
                EmailUtils.sendEmails(send.getEmail(), send.getPwd(), send.getSmtpAddress(), send.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "weekreport", content);
            }
        } catch (Exception ex) {
            System.err.println("邮件发送失败的原因是：" + ex.getMessage());
            System.err.println("具体的错误原因");
            ex.printStackTrace(System.err);
        }
    }

    public void sendmonthemail() {
        try {
            List<Sendemail> sendemails = querySendEmailByType(2);
            for (Sendemail send : sendemails) {
                //获取要发送的邮箱列表
                String[] contactidsstr = send.getContactid().split(",");
                List<Long> contactids = new ArrayList<>();
                for (String contactid : contactidsstr) {
                    contactids.add(Long.parseLong(contactid));
                }
                List<Contactinfo> contactinfolist = queryContactEmail(contactids);
                List<String> emails = new ArrayList<>();
                for (Contactinfo item : contactinfolist) {
                    emails.add(item.getEmail());
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date time1 = DateUtils.getWholePointDate(31);
                String date1 = sdf.format(time1);
                Date time2 = DateUtils.getWholePointDate(1);
                String date2 = sdf.format(time2);
                String content = "http://" + ip + "/weekreport?date1=" + date1 + "&date2=" + date2 + "&taskidstr=" + send.getTaskid() + "&type=1";
                EmailUtils.sendEmails(send.getEmail(), send.getPwd(), send.getSmtpAddress(), send.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "monthreport", content);
            }
        } catch (Exception ex) {
            System.err.println("邮件发送失败的原因是：" + ex.getMessage());
            System.err.println("具体的错误原因");
            ex.printStackTrace(System.err);
        }
    }

    public List<Contactinfo> queryContactEmail(List<Long> conids) {
        ContactinfoExample example = new ContactinfoExample();
        example.createCriteria().andIdIn(conids);
        return contactinfoMapper.selectByExample(example);
    }

    public List<Sendemail> querySendEmailByType(int type) {
        SendemailExample sendemailExample = new SendemailExample();
        sendemailExample.createCriteria().andTypeEqualTo(type);
        List<Sendemail> sendemails = sendEmailService.selectByExample(sendemailExample);
        return sendemails;
    }
}
