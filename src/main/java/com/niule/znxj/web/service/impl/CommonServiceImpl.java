package com.niule.znxj.web.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niule.znxj.core.common.Resources;
import com.niule.znxj.core.entity.JSONResult;
import com.niule.znxj.core.entity.Result;
import com.niule.znxj.core.entity.Xjjl;
import com.niule.znxj.core.entity.Yhjl;
import com.niule.znxj.core.util.*;
import com.niule.znxj.core.util.json.JsonUtil;
import com.niule.znxj.web.dao.*;
import com.niule.znxj.web.model.*;
import com.niule.znxj.web.model.response.RestfulResponse;
import com.niule.znxj.web.model.response.TaskSimpleReport;
import com.niule.znxj.web.model.response.UploadFileResponse;
import com.niule.znxj.web.model.taskcontent.TaskArea;
import com.niule.znxj.web.model.taskcontent.TaskContent;
import com.niule.znxj.web.model.taskcontent.TaskEquipment;
import com.niule.znxj.web.model.taskresponse.TaskTempRes;
import com.niule.znxj.web.service.CommonService;
import com.niule.znxj.web.service.InterfaceService;
import com.niule.znxj.web.service.OperateLogService;
import com.niule.znxj.web.service.SendEmailService;
import jdk.nashorn.internal.ir.annotations.Reference;
import net.sf.json.JSONObject;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by MrD on 2017/3/22.
 */
@Service(value = "commonService")
public class CommonServiceImpl implements CommonService {

    private String ip = Resources.ApplicationResources.getString("ip");

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
    @Resource
    private ReportcontentMapper reportcontentMapper;
    @Resource
    private ContactinfoMapper contactinfoMapper;
    @Resource
    private  ExceptionhandlerinfoMapper exceptionhandlerinfoMapper;
    @Resource
    private UploadtaskinfoMapper uploadtaskinfoMapper;
    @Resource
    private AdmininfoMapper admininfoMapper;
    @Resource
    private  TaskuploadconfigMapper taskuploadconfigMapper;
    @Resource
    private OperateLogService operateLogService;
    @Resource
    private  InterfaceengineMapper interfaceengineMapper;

    @Override
    public Userinfo userLogin(String username, String password) {
        return userinfoMapper.authUser(username, password);
    }

    @Override
    public Result getLoginConfig() {
        SystemsettinginfoExample systemsettinginfoExample = new SystemsettinginfoExample();
        systemsettinginfoExample.createCriteria().andKeynameIn(Arrays.asList("REMEMBER_NAME", "REMEMBER_PSW", "SHOW_COPYRIGHT", "COPYRIGHT_CONTENT", "REPORTCACHE", "WIFIUPLOAD", "SYSTEMVERSION", "APPVERSION"));
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
        example.createCriteria().andTaskcode2EqualTo(taskCode);
        List<Taskreportinfo> taskreportinfos = taskreportinfoMapper.selectByExample(example);
        List<Reportcontent> reportcontents = ReportContentUtils.getReportConentList(reportcontentMapper, taskreportinfos.get(0).getId());

        ArrayList list = new ArrayList();
        for (int i = 0; i < reportcontents.size(); i++) {
            String ss = reportcontents.get(i).getReportstate();
            if (ss.equals("1")) {
                Reportcontent aa = reportcontents.get(i);
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
        taskreportinfoExample.createCriteria().andTaskcode2EqualTo(taskCode);
        List<Taskreportinfo> taskreportinfos = taskreportinfoMapper.selectByExample(taskreportinfoExample);
        if (taskreportinfos != null) {
            Taskreportinfo taskreportinfo = taskreportinfos.get(0);
            if (taskreportinfo == null) {
                simpleReport.setUploadState(0);
            } else {
                List<Reportcontent> reportcontents = ReportContentUtils.getReportConentList(reportcontentMapper, taskreportinfo.getId());
                //异常数
                for (Reportcontent reportContent : reportcontents) {
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
        }
        return new JSONResult<>("没查到数据");
    }

    /**
     * 获取任务列表
     *
     * @param classId
     * @param type
     * @return
     */
    @Override
    public Result getTasks(Long userId, Long classId, Integer type, Integer state, Integer page, Integer size) {
        TasktempinfoExample example = new TasktempinfoExample();
        TasktempinfoExample.Criteria criteria = example.createCriteria();
        if (state == 1) {
            criteria.andUseridEqualTo(userId);
        }
        if (state == 0 || state == 1) {
            example.setOrderByClause("t1.executetime asc");
        } else {
            criteria.andExecutetimeGreaterThan(DateUtils.getWholePointDate(7));
            example.setOrderByClause("t1.executetime desc");
        }
        criteria.andStateEqualTo(state);
        criteria.andTypeEqualTo(type);
        criteria.andClassIdEqualto(classId);
        List<TaskTempRes> list2 = null;
        if (state == 0 || state == 1) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("state", state);
            map.put("type", type);
            map.put("classId", classId);
            list2 = taskplaninfoMapper.getPartTask(map);

        }
        PageHelper.startPage(page, size);
        List<TaskTempRes> list1 = taskplaninfoMapper.getTakTempsExecutingByExample(example);
        if (list2 != null && list2.size() > 0) {
            //2018年7月23日11:28:24  去掉迭代  因为迭代会卡
            List<TaskTempRes> list3 = new ArrayList<>();
            for (TaskTempRes taskTempRes : list1) {
                if (taskTempRes.getOperationstate() == 5) {
                    list3.add(taskTempRes);
                }
            }

            if (list3.size() > 0)
                list1.removeAll(list3);
            list1.addAll(list2);
        }
        if (state == 0 || state == 1) {
            Collections.sort(list1, new Comparator<TaskTempRes>() {
                @Override
                public int compare(TaskTempRes o1, TaskTempRes o2) {
                    //按照executetime进行升序排列
                    if (o1.getExecutetime().after(o2.getExecutetime())) {
                        return 1;
                    }
                    if (o1.getExecutetime().equals(o2.getExecutetime())) {
                        return 0;
                    }
                    return -1;
                }
            });
        }

        //获取任务--未按时间生成的 待执行
        if (state == 0) {
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("type", type);
            map1.put("classId", classId);
            List<TaskTempRes> taskplaninfoList = taskplaninfoMapper.selectTaskTempRes(map1);
            list1.addAll(0, taskplaninfoList);
        }
        return new JSONResult<>(new PageInfo<>(list1));
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
    public synchronized Result setTaskState(Long userId, Long tempId, Integer state, Integer operationstate, Integer stopstate) {
        Tasktempinfo tasktempinfo = tasktempinfoMapper.selectByPrimaryKey(tempId);
        if (tasktempinfo == null) {
            return new JSONResult<>("子任务不存在");
        }
        if (tasktempinfo.getOperationstate() != 0 && tasktempinfo.getOperationstate() != 5) {
            return new JSONResult<>("操作失败，任务已上传，不能重复设置任务状态");
        }
        tasktempinfo.setState(state);
        tasktempinfo.setUserid(userId);
        tasktempinfo.setOperationstate(operationstate);
        tasktempinfo.setStopstate(stopstate);
        tasktempinfo.setUpdatetime(new Date());
        tasktempinfoMapper.updateByPrimaryKeySelective(tasktempinfo);
        if (operationstate == 5) { // 部分执行任务返回
            TaskreportinfoExample example = new TaskreportinfoExample();
            TaskreportinfoExample.Criteria criteria = example.createCriteria();
            criteria.andTaskcodeEqualTo(tasktempinfo.getTaskcode());
            Taskreportinfo taskreportinfo = null;
            List<TaskreportinfoWithBLOBs> list = taskreportinfoMapper.selectByExampleWithBLOBs(example);
            if (list != null && list.size() > 0) { //继续执行分段任务taskreportinfo.getContent()
                taskreportinfo = list.get(0);
                if (taskreportinfo.getContent() != null && !taskreportinfo.getContent().isEmpty()) {
                    Object o = JSONUtils.parse(taskreportinfo.getContent());
                    JSONObject jsonObject = JSONObject.fromObject(o);
                    jsonObject.put("logcat", taskreportinfo.getLogcat());
                    return new JSONResult<>(state == 1 ? jsonObject : null);
                } else { //分段任务上传没有报告
                    Object o = JSONUtils.parse(taskplaninfoMapper.selectByPrimaryKey(tasktempinfo.getTaskid()).getTaskcontent());
                    JSONObject jsonObject = JSONObject.fromObject(o);
                    jsonObject.put("logcat", taskreportinfo.getLogcat());
                    return new JSONResult<>(state == 1 ? jsonObject : null);
                }
            } else { //分段任务上传没有报告
                Object o = JSONUtils.parse(taskplaninfoMapper.selectByPrimaryKey(tasktempinfo.getTaskid()).getTaskcontent());
                return new JSONResult<>(state == 1 ? o : null);
            }
        } else {//app 37及以下版本才有在
            return new JSONResult<>(state == 1 ? (JSONUtils.parse(taskplaninfoMapper.selectByPrimaryKey(tasktempinfo.getTaskid()).getTaskcontent())) : null);
        }
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
    public synchronized Result taskStop(Long userId, Long tempId, String reason, String content, String classname, String
            directorname) {
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
     * 生成任务附表(taskteminfo)
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
                ++i;
                TasktempinfoExample tasktempinfoExample = new TasktempinfoExample();
                tasktempinfoExample.createCriteria().andTaskcodeEqualTo(code);
                List<Tasktempinfo> tasktempinfoList = tasktempinfoMapper.selectByExample(tasktempinfoExample);
                if (tasktempinfoList == null || tasktempinfoList.size() == 0) {
                    temp = new Tasktempinfo(info.getId(), DateUtils.setTime(StringUtils.getHours(str), StringUtils.getMinutes(str)), 0, code, info.getType(), new Date());
                    tasktempinfoMapper.insertSelective(temp);
                    System.out.println("生成任务附表:" + code);
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
            ++i;
            temp = new Tasktempinfo(info.getId(), DateUtils.setTime(StringUtils.getHours(str), StringUtils.getMinutes(str)), 0, code, info.getType(), new Date());
            tasktempinfoMapper.insertSelective(temp);
        }
    }

    /**
     * 上传任务报告
     *
     * @return
     */
    @Override
    public Result uploadReport(final Taskreportinfo taskreportinfo) throws Exception {
        System.out.print("taskreportinfo:" + taskreportinfo.toString());
        if (taskreportinfo.getEndtime() == null)
            taskreportinfo.setDonetime(new Date());
        else
            taskreportinfo.setDonetime(taskreportinfo.getEndtime());
        if (taskreportinfo.getStarttime() == null)
            taskreportinfo.setStarttime(new Date());
        else
            taskreportinfo.setStarttime(taskreportinfo.getStarttime());
        taskreportinfo.setUploadedtime(new Date());
        taskreportinfo.setUploadtime(new Date());
        taskreportinfo.setExamstate(0);
        taskreportinfo.setExamtime(null);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //删除已有的相同 taskckcode 报告
        TaskreportinfoExample example = new TaskreportinfoExample();
        example.createCriteria().andTaskcodeEqualTo(taskreportinfo.getTaskcode());
        taskreportinfoMapper.deleteByExample(example);
        //不存在子任务 上传失败
        TasktempinfoExample example1 = new TasktempinfoExample();
        example1.createCriteria().andTaskcodeEqualTo(taskreportinfo.getTaskcode());
        if (tasktempinfoMapper.countByExample(example1) == 0)
            return new JSONResult<>("操作失败，子任务不存在");

        Result result;
        if (taskreportinfo.getOperationstate() == 5) { //任务部分上传不插入报告内容
            result = taskreportinfoMapper.insert(taskreportinfo) == 1 ? new JSONResult<>() : new JSONResult<>("操作失败");
            return result;
        }
        final String content1 = taskreportinfo.getContent();
        taskreportinfo.setContent(null);   //任务上传完成后，去掉这个值，节省空间
        result = taskreportinfoMapper.insert(taskreportinfo) == 1 ? new JSONResult<>() : new JSONResult<>("操作失败");

        List<Taskreportinfo> taskreportinfos = null;
        try {
            taskreportinfos = getTaskCode2(taskreportinfo.getTaskcode());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ThreadPoolExecutorFactory.getInstance().run(new Runnable() {
            @Override
            public void run() {
                Boolean hasException = false;
                String content = "{" + "\"res\":" + content1 + "}";
                if (content != null) {
                    //插入报告内容单项 到 reportcontent
                    TaskReportRes res = JsonUtil.toObject(content, TaskReportRes.class);
                    for (TaskReportContent item : res.getRes()) {

                        if (!hasException && "1".equals(item.getReportstate())) {
                            hasException = true;
                        }
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
                            if (normalmin == null || normalmin == 0) {
                                a = "-";
                            } else
                                a = normalmin + "";
                            if (normalmax == null || normalmax == 0) {
                                e = "-";
                            } else
                                e = normalmax + "";
                            if (lowerwarning == null || lowerwarning == 0) {
                                c = "-";
                            } else
                                c = lowerwarning + "";
                            if (upperwarning == null || upperwarning == 0) {
                                d = "-";
                            } else
                                d = upperwarning + "";
                        } else if (checkitype.equals("状态项")) {
                            a = "-";
                            e = "-";
                            c = "-";
                            d = "-";
                        } else if (checkitype.equals("枚举项")) {
                            a = "-";
                            e = "-";
                            c = "-";
                            d = "-";
                        }

                        Reportcontent reportcontent = new Reportcontent(
                                item.getAreaname(),
                                item.getEquipname(),
                                item.getCheckname(),
                                item.getChecktype(),
                                item.getNumvalue(),
                                item.getRecordname(),
                                item.getUnitname(),
                                item.getReportstate(),
                                item.getErrcontent(),
                                item.getAreaskip(),
                                item.getEquipmentskip(),
                                item.getAreaskipdesc(),
                                item.getEquipmentskipdesc(),
                                sdf.format(item.getOperationtime()),
                                taskreportinfo.getId(),
                                c,
                                e,
                                a,
                                d,
                                JsonUtil.toJSON(item.getVideo()),
                                JsonUtil.toJSON(item.getAudio()),
                                JsonUtil.toJSON(item.getImg()),
                                item.getEnumitem(),
                                item.getEquipinouttime(),
                                item.getAreainouttime(),
                                item.getUserid()
                        );
                        reportcontent.setReportid(taskreportinfo.getId());
                        reportcontentMapper.insert(reportcontent);
                    }
                }
                //存在异常发送邮件
                if (hasException) {
                    sendExceptionEmail(taskreportinfo.getId(), taskreportinfo.getTaskid());
                }
            }
        });

        if (taskreportinfos != null && taskreportinfos.size() > 0) {
            TaskreportinfoWithBLOBs taskreportinfoWithBLOBs = new TaskreportinfoWithBLOBs();
            taskreportinfoWithBLOBs.setId(taskreportinfos.get(0).getId());
            taskreportinfoWithBLOBs.setExamstate(2);
            taskreportinfoWithBLOBs.setExamtime(new Date());
            int updCheckTime = taskreportinfoMapper.updateByPrimaryKeySelective(taskreportinfoWithBLOBs);
            if (updCheckTime > 0) {
                ReportcontentExample example2 = new ReportcontentExample();
                example2.createCriteria().andReportidEqualTo(taskreportinfos.get(0).getId());
                List<Reportcontent> reportcontents = reportcontentMapper.selectByExample(example2);
                for (Reportcontent reportcontent : reportcontents) {
                    reportcontent.setCheckvalue(reportcontent.getNumvalue());
                    reportcontentMapper.updateByPrimaryKeySelective(reportcontent);
                }
            }
        }
        return result;
    }

    /**
     * 发送异常邮件
     */
    public void sendExceptionEmail(Long reportId, Long taskId) {
        try {
            //获取发件人邮箱和授权码
            List<Sendemail> sendemails = getSendExceptionList(taskId);
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
                String content = "http://" + ip + "/exceptionReport?reportId=" + reportId;
                String res = HttpRequestUtil.get(content, null, 3000, 60000, "utf-8");
                EmailUtils.sendEmails(send.getEmail(), send.getPwd(), send.getSmtpAddress(), send.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "智能巡检异常报告", res);
            }
        } catch (Exception ex) {
            System.err.println("邮件发送失败的原因是：" + ex.getMessage());
            System.err.println("具体的错误原因");
            ex.printStackTrace(System.err);
        }
    }

    public List<Taskreportinfo> getTaskCode(String taskCode) throws Exception {
        String code = taskCode.substring(0, taskCode.lastIndexOf("-"));
        //得到当前子任务是第几次执行
        Integer i = Integer.valueOf(taskCode.substring(taskCode.lastIndexOf("-") + 1));
        String oldTaskCode = "";
        if (i != 1) {
            oldTaskCode = code + "-" + (i - 1);
        } else {
            //如果当前子任务是第一次执行，获取到昨天当前任务最后一次执行的任务报告
            int index = taskCode.indexOf("-");
            //根据第一个点的位置 获得第二个点的位置
            index = taskCode.indexOf("-", index + 1);
            int taskNameIndex = taskCode.indexOf("-");
            String taskName = taskCode.substring(0, taskNameIndex);//截取任务号
            String time = taskCode.substring(index + 1, taskCode.lastIndexOf("-"));//截取时间
            //时间减取一天
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date date = sdf.parse(time);
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            now.set(Calendar.DATE, now.get(Calendar.DATE) - 1);
            String time2 = sdf.format(now.getTime());
            //查询昨天的所有报告
            TaskreportinfoExample example = new TaskreportinfoExample();
            example.createCriteria().andTaskcode2Like(taskName + "%" + time2 + "%");
            List<Taskreportinfo> taskreportinfos = taskreportinfoMapper.selectByExample(example);
            Integer k = null;
            String taskversion = "";
            if (taskreportinfos.size() > 0) {
                String newTaskCode = taskreportinfos.get(0).getTaskcode();
                int aa = newTaskCode.indexOf("-");
                int bb = newTaskCode.indexOf("-", aa + 1);
                taskversion = newTaskCode.substring(aa + 1, bb);//昨天的任务版本
                List<Integer> counts = new ArrayList<>();
                for (Taskreportinfo taskreportinfo : taskreportinfos) {
                    String taskcode2 = taskreportinfo.getTaskcode();
                    Integer j = Integer.valueOf(taskcode2.substring(taskcode2.lastIndexOf("-") + 1));
                    counts.add(j);
                }
                k = Collections.max(counts);
            }
            if (k == null)
                oldTaskCode = null;
            else
                oldTaskCode = taskName + "-" + taskversion + "-" + time2 + "-" + k;//昨天最后一个子任务
        }
        List<Taskreportinfo> taskreportinfos = new ArrayList<>();
        if (oldTaskCode != null) {
            TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
            taskreportinfoExample.createCriteria().andTaskcode2EqualTo(oldTaskCode).andExamstateEqualTo(0);
            taskreportinfos = taskreportinfoMapper.selectByExample(taskreportinfoExample);
        }
        return taskreportinfos;
    }

    public List<Taskreportinfo> getTaskCode2(String taskCode) throws Exception {

        //如果当前子任务是第一次执行，获取到昨天当前任务最后一次执行的任务报告
        int index = taskCode.indexOf("-");
        //根据第一个点的位置 获得第二个点的位置
        index = taskCode.indexOf("-", index + 1);
        int taskNameIndex = taskCode.indexOf("-");
        String taskName = taskCode.substring(0, taskNameIndex);//截取任务号
        String time = taskCode.substring(index + 1, taskCode.lastIndexOf("-"));//截取时间
        String endTime = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);

        //时间减取一天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(endTime);

        //如何添加数据时，没有选择开始时间和结束时间，则在创建时间上加上十年
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -2);
        date = calendar.getTime();
        String startTime = sdf.format(date);

        //查询当天和昨天和前天的所有报告
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskName", "%" + taskName + "%");
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        List<Taskreportinfo> taskreportinfos = taskreportinfoMapper.selectByExample2(map);

        if (taskreportinfos != null && taskreportinfos.size() > 0) {
            Iterator<Taskreportinfo> iterator = taskreportinfos.iterator();
            while (iterator.hasNext()) {
                Taskreportinfo taskreportinfo = iterator.next();
                if (taskreportinfo.getExamstate() != 0 || taskCode.equals(taskreportinfo.getTaskcode())) {
                    iterator.remove();
                }
            }
        }

        return taskreportinfos;
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
                Taskplaninfo taskplaninfo = taskplaninfoMapper.selectByPrimaryKey(send.getTaskid());
                if (taskplaninfo != null) {
                    String content = "http://" + ip + "/dadiyreport?date1=" + date1 + "&taskidstr=" + send.getTaskid();
                    String res = HttpRequestUtil.get(content, null, 3000, 60000, "utf-8");
                    EmailUtils.sendEmails(send.getEmail(), send.getPwd(), send.getSmtpAddress(), send.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "智能巡检日报", res);
                }
            }
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
                Taskplaninfo taskplaninfo = taskplaninfoMapper.selectByPrimaryKey(send.getTaskid());
                if (taskplaninfo != null) {
                    String content = "http://" + ip + "/weekreport?date1=" + date1 + "&date2=" + date2 + "&taskidstr=" + send.getTaskid() + "&type=0";
                    String res = HttpRequestUtil.get(content, null, 3000, 60000, "utf-8");
                    EmailUtils.sendEmails(send.getEmail(), send.getPwd(), send.getSmtpAddress(), send.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "智能巡检周报", res);
                }
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
                Taskplaninfo taskplaninfo = taskplaninfoMapper.selectByPrimaryKey(send.getTaskid());
                if (taskplaninfo != null) {
                    String content = "http://" + ip + "/weekreport?date1=" + date1 + "&date2=" + date2 + "&taskidstr=" + send.getTaskid() + "&type=1";
                    String res = HttpRequestUtil.get(content, null, 3000, 60000, "utf-8");
                    EmailUtils.sendEmails(send.getEmail(), send.getPwd(), send.getSmtpAddress(), send.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "智能巡检月报", res);
                }
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

    public List<Sendemail> querySendEmailByTypeAndTask(int type,Long taskid) {
        SendemailExample sendemailExample = new SendemailExample();
        sendemailExample.createCriteria().andTypeEqualTo(type).andTaskidEqualTo(taskid);
        List<Sendemail> sendemails = sendEmailService.selectByExample(sendemailExample);
        return sendemails;
    }

    public List<Sendemail> querySendEmailByType(int type) {
        SendemailExample sendemailExample = new SendemailExample();
        sendemailExample.createCriteria().andTypeEqualTo(type);
        List<Sendemail> sendemails = sendEmailService.selectByExample(sendemailExample);
        return sendemails;
     }

    /**
     * 获取需要发送异常的邮箱
     *
     * @return
     */
    public List<Sendemail> getSendExceptionList(Long taskId) {
        SendemailExample sendemailExample = new SendemailExample();
        sendemailExample.createCriteria().andTypeEqualTo(3).andTaskidEqualTo(taskId);
        List<Sendemail> sendemails = sendEmailService.selectByExample(sendemailExample);
        return sendemails;
    }

    @Override
    public void automaticExamine() {
        TaskreportinfoExample taskreportinfoExample = new TaskreportinfoExample();
        taskreportinfoExample.createCriteria().andExamstateEqualTo(0);
        List<Taskreportinfo> taskreportinfos = taskreportinfoMapper.selectByExample(taskreportinfoExample);
        Date now = new Date();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        if (taskreportinfos.size() > 0) {
            for (Taskreportinfo taskreportinfo : taskreportinfos) {
                long diff = now.getTime() - taskreportinfo.getDonetime().getTime();
                // 计算差多少天
                long day = diff / nd;
                // 计算差多少小时
                long hour = diff % nd / nh;
                long hours = day * 24 + hour;
                if (hours > 36) {
                    TaskreportinfoWithBLOBs taskreportinfoWithBLOBs = new TaskreportinfoWithBLOBs();
                    taskreportinfoWithBLOBs.setId(taskreportinfo.getId());
                    taskreportinfoWithBLOBs.setExamstate(2);
                    taskreportinfoWithBLOBs.setExamtime(new Date());
                    int updCheckTime = taskreportinfoMapper.updateByPrimaryKeySelective(taskreportinfoWithBLOBs);
                    ReportcontentExample example = new ReportcontentExample();
                    example.createCriteria().andReportidEqualTo(taskreportinfo.getId());
                    List<Reportcontent> reportcontents = reportcontentMapper.selectByExample(example);
                    for (Reportcontent reportcontent : reportcontents) {
                        reportcontent.setCheckvalue(reportcontent.getNumvalue());
                        Reportcontent reportcontent1 = updateErrContent(reportcontent.getId());
                        reportcontent.setErrcontent(reportcontent1.getErrcontent());
                        reportcontentMapper.updateByPrimaryKeySelective(reportcontent);
                    }
                }
            }
        }
    }

    @Override
    public Reportcontent updateErrContent(Integer reportcontentid){
        Reportcontent reportcontent = reportcontentMapper.selectByPrimaryKey(reportcontentid);
        Taskreportinfo taskreportinfo = taskreportinfoMapper.selectByPrimaryKey(reportcontent.getReportid());
        try{
            List<Reportcontent> reportcontentList = null;
            //得到前一次报告
            List<Taskreportinfo> taskreportinfos = this.getTaskCode(taskreportinfo.getTaskcode());
            if (taskreportinfos.size() > 0) {
                ReportcontentExample reportcontentExample = new ReportcontentExample();
                reportcontentExample.createCriteria().andReportidEqualTo(taskreportinfos.get(0).getId());
                reportcontentList = reportcontentMapper.selectByExample(reportcontentExample);
            }
            //得到波动值
            SystemsettinginfoExample systemsettinginfoExample=new SystemsettinginfoExample();
            List<Systemsettinginfo> systems = systemsettinginfoMapper.selectByExample(systemsettinginfoExample);
            Double fluctudte = 0.0;
            for (Systemsettinginfo info : systems) {
                if (info.getKeyname().equals("FLUCTUATE"))
                    fluctudte = Double.valueOf(info.getValue());
            }
            Reportcontent reportcontent1 = null;
            if(reportcontentList!=null && reportcontentList.size()>0){
                for(Reportcontent reportcontent2 : reportcontentList){
                    if(reportcontent2.getAreaname().equals(reportcontent.getAreaname()) && reportcontent2.getEquipname().equals(reportcontent.getEquipname())
                            && reportcontent2.getCheckname().equals(reportcontent.getCheckname())){
                        reportcontent1 = reportcontent2;
                    }
                }
            }
            String bodongzhi = "";
            double maxfluctudte = 1 + fluctudte;
            double minfluctudte = 1 - fluctudte;
            if (reportcontent.getCheckvalue() != null && reportcontent1 != null && reportcontent1.getCheckvalue() != null) {
                if (reportcontent.getCheckvalue() != null && !reportcontent.getCheckvalue().isEmpty() && reportcontent1.getCheckvalue() != null && !reportcontent1.getCheckvalue().isEmpty()) {
                    if (Double.parseDouble(reportcontent.getCheckvalue()) / Double.parseDouble(reportcontent1.getCheckvalue()) > maxfluctudte) {
                        bodongzhi = "<span style='color:blue;'>" + "&nbsp;&nbsp;&nbsp;" + "↑↑↑" + "</span>";
                    }
                    if (Double.parseDouble(reportcontent.getCheckvalue()) / Double.parseDouble(reportcontent1.getCheckvalue()) < minfluctudte) {
                        bodongzhi = "<span style='color:blue;'>" + "&nbsp;&nbsp;&nbsp;" + "↓↓↓" + "</span>";
                    }
                }

            } else if (!reportcontent.getNumvalue().isEmpty() && reportcontent1 != null && reportcontent1.getCheckvalue() != null && !reportcontent1.getCheckvalue().isEmpty()) {
                if (Double.parseDouble(reportcontent.getNumvalue()) / Double.parseDouble(reportcontent1.getCheckvalue()) > maxfluctudte) {
                    bodongzhi = "<span style='color:blue;'>" + "&nbsp;&nbsp;&nbsp;" + "↑↑↑" + "</span>";
                }
                if (Double.parseDouble(reportcontent.getNumvalue()) / Double.parseDouble(reportcontent1.getCheckvalue()) < minfluctudte) {
                    bodongzhi = "<span style='color:blue;'>" + "&nbsp;&nbsp;&nbsp;" + "↓↓↓" + "</span>";
                }
            }
            if (reportcontent.getCheckvalue() != null && !reportcontent.getCheckvalue().isEmpty()) {
                //大于低值 小于高值
                if (!reportcontent.getNormalmin().equals("-") && !reportcontent.getNormalmax().equals("-") &&
                        Double.parseDouble(reportcontent.getNormalmin()) <= Double.parseDouble(reportcontent.getCheckvalue()) &&
                        Double.parseDouble(reportcontent.getCheckvalue()) <= Double.parseDouble(reportcontent.getNormalmax())) {
                    reportcontent.setErrcontent("");
                }
                //大于最低值  小于低值
                else if (!reportcontent.getNormalmin().equals("-") && !reportcontent.getLowerwarning().equals("-")
                        && Double.parseDouble(reportcontent.getCheckvalue()) < Double.parseDouble(reportcontent.getNormalmin())
                        && Double.parseDouble(reportcontent.getCheckvalue()) > Double.parseDouble(reportcontent.getLowerwarning())) {
                    reportcontent.setErrcontent(reportcontent.getCheckvalue()+"<span style='color:red;'>" + "↓" + bodongzhi + "</span>");
                }//大于高值  小于最高值值
                else if (!reportcontent.getNormalmax().equals("-") && !reportcontent.getUpperwarning().equals("-")
                        && Double.parseDouble(reportcontent.getCheckvalue()) > Double.parseDouble(reportcontent.getNormalmax())
                        && Double.parseDouble(reportcontent.getCheckvalue()) < Double.parseDouble(reportcontent.getUpperwarning())) {
                    reportcontent.setErrcontent(reportcontent.getCheckvalue()+"<span style='color:red;'>" + "↑" + bodongzhi + "</span>");
                } //大于最高上限值
                else if (!reportcontent.getUpperwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getCheckvalue()) > Double.parseDouble(reportcontent.getUpperwarning())) {
                    reportcontent.setErrcontent(reportcontent.getCheckvalue()+"<span style='color:red;'>" + "↑↑" + bodongzhi + "</span>");
                }//小于最低上限值
                else if (!reportcontent.getLowerwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getCheckvalue()) < Double.parseDouble(reportcontent.getLowerwarning())) {
                    reportcontent.setErrcontent(reportcontent.getCheckvalue()+"<span style='color:red;'>" + "↓↓" + bodongzhi + "</span>");
                } else {
                    reportcontent.setErrcontent("");
                }
            } else if (!reportcontent.getNumvalue().equals("")) {
                //大于低值 小于高值
                if (!reportcontent.getNormalmin().equals("-") && !reportcontent.getNormalmax().equals("-") &&
                        Double.parseDouble(reportcontent.getNormalmin()) <= Double.parseDouble(reportcontent.getNumvalue()) &&
                        Double.parseDouble(reportcontent.getNumvalue()) <= Double.parseDouble(reportcontent.getNormalmax())) {
                    reportcontent.setErrcontent("");
                }//大于最低值  小于低值
                else if (!reportcontent.getNormalmin().equals("-") && !reportcontent.getLowerwarning().equals("-")
                        && Double.parseDouble(reportcontent.getNumvalue()) < Double.parseDouble(reportcontent.getNormalmin())
                        && Double.parseDouble(reportcontent.getNumvalue()) > Double.parseDouble(reportcontent.getLowerwarning())) {
                    reportcontent.setErrcontent(reportcontent.getNumvalue()+"<span style='color:red;'>" + "↓" + bodongzhi + "</span>");
                }
                //大于高值  小于最高值值
                else if (!reportcontent.getNormalmax().equals("-") && !reportcontent.getUpperwarning().equals("-")
                        && Double.parseDouble(reportcontent.getNumvalue()) > Double.parseDouble(reportcontent.getNormalmax())
                        && Double.parseDouble(reportcontent.getNumvalue()) < Double.parseDouble(reportcontent.getUpperwarning())) {
                    reportcontent.setErrcontent(reportcontent.getNumvalue()+"<span style='color:red;'>" + "↑" + bodongzhi + "</span>");
                }  //大于最高上限值
                else if (!reportcontent.getUpperwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getNumvalue()) > Double.parseDouble(reportcontent.getUpperwarning())) {
                    reportcontent.setErrcontent(reportcontent.getNumvalue()+"<span style='color:red;'>" + "↑↑" + bodongzhi + "</span>");
                }
                //小于最低上限值
                else if (!reportcontent.getLowerwarning().equals("-") &&
                        Double.parseDouble(reportcontent.getNumvalue()) < Double.parseDouble(reportcontent.getLowerwarning())) {
                    reportcontent.setErrcontent(reportcontent.getNumvalue()+"<span style='color:red;'>" + "↓↓" + bodongzhi + "</span>");
                } else {
                    reportcontent.setErrcontent("");
                }
            } else {
                reportcontent.setErrcontent("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  reportcontent;
    }

    @Override
    public void sendReportEmail(Long userid,Long reportcontentid) {
        try {
            //获取发件人邮箱和授权码
            List<Sendemail> sendemails = querySendEmailByType(0); //按照日报的获取
            List<String> emails = new ArrayList<>();
            if(sendemails!=null && sendemails.size()>0){
                Sendemail sendemail = sendemails.get(0);
                Admininfo user = admininfoMapper.selectByPrimaryKey(userid);
                emails.add(user.getEmail());
                String content = "http://" + ip + "/exceptionReportContent?id=" + reportcontentid;
                String res = HttpRequestUtil.get(content, null, 3000, 60000, "utf-8");
                EmailUtils.sendEmails(sendemail.getEmail(), sendemail.getPwd(), sendemail.getSmtpAddress(), sendemail.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "智能巡检异常报告", res);
            }

        } catch (Exception ex) {
            System.err.println("邮件发送失败的原因是：" + ex.getMessage());
            System.err.println("具体的错误原因");
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public void sendDangerEmail(Long userid,Long reportid) {
        try {

            Quickreport quickreport = quickreportMapper.selectByPrimaryKey(reportid);
            //获取发件人邮箱和授权码
            List<Sendemail> sendemails = querySendEmailByType(0); //按照日报的获取
            List<String> emails = new ArrayList<>();
            if(sendemails!=null && sendemails.size()>0){
                Sendemail sendemail = sendemails.get(0);
                Admininfo user = admininfoMapper.selectByPrimaryKey(userid);
                emails.add(user.getEmail());
                String content = "隐患任务报告为："+quickreport.getReportcode()+" 的任务已分配给你，请及时处理！";
                EmailUtils.sendEmails(sendemail.getEmail(), sendemail.getPwd(), sendemail.getSmtpAddress(), sendemail.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), "隐患巡检报告", content);
            }

        } catch (Exception ex) {
            System.err.println("邮件发送失败的原因是：" + ex.getMessage());
            System.err.println("具体的错误原因");
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public Result doTask(Long userId, Long taskId) {

        TasktempinfoExample example = new TasktempinfoExample();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        example.createCriteria().andTaskidNoAliasEqualTo(taskId).andExecutetimeDoTaskEqualTo(sdf.format(now));
        int code = tasktempinfoMapper.countByExample(example) + 1;
        Taskplaninfo taskplaninfo = taskplaninfoMapper.selectByPrimaryKey(taskId);
        SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd");
        String codeStr = taskplaninfo.getIdentifyingid() + "-" + taskplaninfo.getTaskversion() + "-" + dtf.format(new Date()) + "-" + code;
        Tasktempinfo temp = new Tasktempinfo(taskplaninfo.getId(), userId, new Date(), 1, codeStr, taskplaninfo.getType(), new Date());
        tasktempinfoMapper.insertSelective(temp);
        TasktempinfoExample example1 = new TasktempinfoExample();
        TasktempinfoExample.Criteria criteria = example1.createCriteria();
        criteria.andUseridEqualTo(userId);
        example1.setOrderByClause("t1.executetime asc");
        criteria.andStateEqualTo(1);
        criteria.andTypeEqualTo(taskplaninfo.getType());
        criteria.andClassIdEqualto(taskplaninfo.getClassid());
        List<TaskTempRes> list1 = taskplaninfoMapper.getTakTempsExecutingByExample(example1);
        if (list1 != null && list1.size() > 0) {
            return new JSONResult<>(list1.get(list1.size() - 1));
        } else {
            return new JSONResult<>("没有子任务");
        }
    }

    @Override
    public void uploadTaskReportInfo() {
        //1、查询需要发送的任务报告
        UploadtaskinfoExample example = new UploadtaskinfoExample();
        List<Integer> states = new ArrayList<>();
        states.add(0); //未上报
        states.add(2);//上报失败
        example.createCriteria().andStateIn(states).andEnginetypeEqualTo("1");//苏州公司接口
        List<Uploadtaskinfo> uploadtaskinfoList = uploadtaskinfoMapper.selectByExample(example);
        String uploadMethod = "system/hotsync/xjjl";
        //获取发件人邮箱和授权码
        List<Sendemail> sendemails = querySendEmailByType(0); //按照日报的获取
        if(uploadtaskinfoList!=null && uploadtaskinfoList.size()>0){
            for(Uploadtaskinfo uploadtaskinfo : uploadtaskinfoList) {
                InterfaceengineExample interfaceengineExample = new InterfaceengineExample();
                interfaceengineExample.createCriteria().andSiteidEqualTo(uploadtaskinfo.getSiteid())
                        .andEnginetypeEqualTo(uploadtaskinfo.getEnginetype());
                List<Interfaceengine> interfaceengineList = interfaceengineMapper.selectByExample(interfaceengineExample);
                Interfaceengine interfaceengine = interfaceengineList.get(0);
                Taskreportinfo taskreportinfo = taskreportinfoMapper.selectTaskReportInfo(uploadtaskinfo.getReportid());
                if(taskreportinfo!=null){
                    List<Xjjl> xjjlList = new ArrayList<Xjjl>();
                    Xjjl xjjl = new Xjjl();
                    xjjl.setFlag(taskreportinfo.getId() + "");
                    xjjl.setCheckpointname(taskreportinfo.getTaskdesc());
                    xjjl.setCheckorder(taskreportinfo.getTaskcode());
                    xjjl.setCreatetime(DateUtils.parseDateToStr(taskreportinfo.getDonetime(), "yyyy-MM-dd hh:mm:ss"));
                    xjjl.setCheckperson(taskreportinfo.getWorker());
                    xjjl.setCheckresult(taskreportinfo.getReportstate()==null?"0":taskreportinfo.getReportstate()+"");
                    xjjlList.add(xjjl);
                    try {
                        String url = uploadtaskinfo.getAddress() + uploadMethod;
                        String resultStr = HttpClientUtils.httpPost(url, JsonUtil.toJSON(xjjlList));
                        System.out.println("result ====" + resultStr);
                        RestfulResponse response = JsonUtil.toObject(resultStr, RestfulResponse.class);
                        uploadtaskinfo.setUploadtime(new Date());
                        if (response.getStatus()) {
                            uploadtaskinfo.setState(1);
                            uploadtaskinfoMapper.updateByPrimaryKey(uploadtaskinfo);
                            //更新接口异常上传状态为1（异常需要发邮件）
                            if(interfaceengine.getSendemailstate()==0){
                                interfaceengine.setSendemailstate(1);
                                interfaceengineMapper.updateByPrimaryKey(interfaceengine);
                            }
                            operateLogService.insertSelective("autouser", "巡检记录上传成功，任务编号：" + taskreportinfo.getTaskcode());
                        } else {
                            uploadtaskinfo.setState(2);
                            uploadtaskinfoMapper.updateByPrimaryKey(uploadtaskinfo);
                            operateLogService.insertSelective("autouser", "巡检记录上传失败，任务编号：" + taskreportinfo.getTaskcode());
                            //发送邮件
                            String content = "巡检记录上传失败，任务编号：" + taskreportinfo.getTaskcode();
                            sengUplaodExceptionEmail(sendemails,uploadtaskinfo.getEmail(),content,"巡检记录上传");
                        }
                    }catch(Exception e){
                        if(interfaceengine.getSendemailstate()==1){ //需要发送邮件
                            String title = "服务器接口失败";
                            String content = "调用接口异常，接口地址:"+uploadtaskinfo.getAddress()+uploadMethod;
                            sengUplaodExceptionEmail(sendemails,interfaceengine.getEmail(),content,title);
                            interfaceengine.setSendemailstate(0);
                            interfaceengineMapper.updateByPrimaryKey(interfaceengine);
                        }
                        operateLogService.insertSelective("autouser", "调用巡检记录上传接口：" + uploadtaskinfo.getAddress()+uploadMethod+"失败！");
                        System.out.println("---------调用巡检记录上传接口失败："+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void sengUplaodExceptionEmail( List<Sendemail> sendemails,String email,String content,String title){
        List<String> emails = new ArrayList<>();
        if (sendemails != null && sendemails.size() > 0) {
            Sendemail sendemail = sendemails.get(0);
            emails.add(email);
            EmailUtils.sendEmails(sendemail.getEmail(), sendemail.getPwd(), sendemail.getSmtpAddress(), sendemail.getSmtpPort(), (String[]) emails.toArray(new String[emails.size()]), title, content);
        }
    }

    @Override
    public void uploadExceptionReport() {
        //获取发件人邮箱和授权码
        List<Sendemail> sendemails = querySendEmailByType(0); //按照日报的获取
        //1、查询已经上传成功的巡检任务报告
        UploadtaskinfoExample example = new UploadtaskinfoExample();
        example.createCriteria().andUploadtimeBetween(DateUtils.getWholePointDate(7),new Date()).andStateEqualTo(1).andEnginetypeEqualTo("1");//苏州公司接口
        List<Uploadtaskinfo> uploadtaskinfoList = uploadtaskinfoMapper.selectByExample(example);
        String uploadMethod = "system/hotsync/yhjl";
        for(Uploadtaskinfo uploadtaskinfo : uploadtaskinfoList) {
            TaskuploadconfigExample taskuploadconfigExample = new TaskuploadconfigExample();
            taskuploadconfigExample.createCriteria().andTaskidEqualTo(uploadtaskinfo.getTaskid());
            List<Taskuploadconfig> taskuploadconfigList = taskuploadconfigMapper.selectByExample(taskuploadconfigExample);

            InterfaceengineExample interfaceengineExample = new InterfaceengineExample();
            interfaceengineExample.createCriteria().andSiteidEqualTo(uploadtaskinfo.getSiteid())
                    .andEnginetypeEqualTo(uploadtaskinfo.getEnginetype());
            List<Interfaceengine> interfaceengineList = interfaceengineMapper.selectByExample(interfaceengineExample);
            Interfaceengine interfaceengine = interfaceengineList.get(0);
            Taskreportinfo taskreportinfo = taskreportinfoMapper.selectByPrimaryKey(uploadtaskinfo.getReportid());
            if(taskreportinfo!=null && taskuploadconfigList!=null && taskuploadconfigList.size()>0){
                String[] exceptiontypes = taskuploadconfigList.get(0).getExceptiontype().split(",");
                String[] exceptionlevers = taskuploadconfigList.get(0).getExceptionlever().split(",");
                HashMap<String,Object> paramMap = new HashMap<>();
                List<Integer> states = new ArrayList<>();
                states.add(0); //未上报
                states.add(2);//上报失败
                paramMap.put("states",states);
                paramMap.put("reportid",taskreportinfo.getId());
                paramMap.put("exceptionleverList",exceptionlevers);
                paramMap.put("exceptiontypeList",exceptiontypes);
                List<Exceptionhandlerinfo> exceptionhandlerinfoList = exceptionhandlerinfoMapper.selectByParam(paramMap);
                List<Long> reportcontentidList = new ArrayList<Long>();
                List<Yhjl> yhjlList = new ArrayList<>();
                try {
                    for(Exceptionhandlerinfo info: exceptionhandlerinfoList){
                        Yhjl yhjl = new Yhjl();
                        yhjl.setCheckresult_flag(info.getReportid()+"");
                        yhjl.setCheckpointname(info.getEquipname());
                        yhjl.setCheckcontent(info.getCheckname());
                        yhjl.setDangerstatus(info.getExceptionstate()+"");
                        yhjl.setFindperson(info.getWorker());
                        yhjl.setCreatetime(info.getDonetime()!=null?DateUtils.parseDateToStr(info.getDonetime(),
                                "yyyy-MM-dd hh:mm:ss"):DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd hh:mm:ss"));
                        yhjl.setHandletime(info.getAppointedtime()!=null?DateUtils.parseDateToStr(info.getAppointedtime(),
                                "yyyy-MM-dd hh:mm:ss"):DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd hh:mm:ss"));
                        yhjl.setHandlepersons(info.getUsername());
                        yhjl.setDangerdesc("");//隐患备注
                        yhjl.setDangerfj(getImgUrl(uploadtaskinfo.getAddress(),info.getReportimg())); //隐患附件
                        yhjl.setReformperson(info.getOperatorname()!=null?info.getOperatorname():"未指定");
                        yhjl.setReformdesc(""); //整改备注
                        yhjl.setReformfj(getImgUrl(uploadtaskinfo.getAddress(),info.getAttachment())); //整改附件
                        yhjl.setReformtime(info.getExceptionclosetime()!=null?DateUtils.parseDateToStr(info.getExceptionclosetime(),
                                "yyyy-MM-dd hh:mm:ss"):DateUtils.parseDateToStr(new Date(),"yyyy-MM-dd hh:mm:ss"));
                        yhjlList.add(yhjl);
                        reportcontentidList.add(info.getReportcontentid());
                    }
                    if (yhjlList.size()>0) {
                        String resultStr = HttpClientUtils.httpPost(uploadtaskinfo.getAddress() + uploadMethod, JsonUtil.toJSON(yhjlList));
                        System.out.println("result ====" + resultStr);
                        RestfulResponse response = JsonUtil.toObject(resultStr, RestfulResponse.class);
                        HashMap<String,Object> param = new HashMap<String,Object>();
                        param.put("reportid",uploadtaskinfo.getReportid());
                        param.put("reportcontentids", reportcontentidList);
                        if (response.getStatus()) {
                            param.put("uploadstate",1);
                            exceptionhandlerinfoMapper.updateByReportId(param);
                            //更新接口异常上传状态为1（异常需要发邮件）
                            if(interfaceengine.getSendemailstate()==0){
                                interfaceengine.setSendemailstate(1);
                                interfaceengineMapper.updateByPrimaryKey(interfaceengine);
                            }
                            operateLogService.insertSelective("autouser","异常巡检记录上传成功，任务编号："+taskreportinfo.getTaskcode());
                        } else {
                            param.put("uploadstate",2);
                            exceptionhandlerinfoMapper.updateByReportId(param);
                            operateLogService.insertSelective("autouser","异常巡检记录上传失败，任务编号："+taskreportinfo.getTaskcode());
                            //发送邮件
                            String content = "异常巡检记录上传失败，请查看！";
                            sengUplaodExceptionEmail(sendemails,uploadtaskinfo.getEmail(),content,"巡检异常记录上传");
                        }
                    }
                }catch(Exception e){
                    if(interfaceengine.getSendemailstate()==1){ //需要发送邮件
                        String title = "服务器接口失败";
                        String content = "调用接口异常，接口地址:"+uploadtaskinfo.getAddress()+uploadMethod;
                        sengUplaodExceptionEmail(sendemails,interfaceengine.getEmail(),content,title);
                        interfaceengine.setSendemailstate(0);
                        interfaceengineMapper.updateByPrimaryKey(interfaceengine);
                    }
                    operateLogService.insertSelective("autouser", "调用异常巡检记录上传接口：" + uploadtaskinfo.getAddress()+uploadMethod+"失败！");
                    System.out.println("---------调用异常巡检记录上传接口失败："+e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public String getImgUrl(String urlAddress,String imgStr){
        String imgurl = "";
        if (imgStr==null || "[]".equals(imgStr) || "[\"\"]".equals(imgStr) || "null".equals(imgStr) || "".equals(imgStr)){
            return imgurl;
        }
        String uploadFileMethod = "appupload/uploadfile";
        try {
            List<String> imgList = JsonUtil.toObject(imgStr,List.class);
            for (String str: imgList){
                String filePath = Resources.ApplicationResources.getString("newfile.path")+str;
                String url = urlAddress + uploadFileMethod;
                String resultStr = HttpClientUtils.uploadFile(url,filePath);
                UploadFileResponse response = JsonUtil.toObject(resultStr, UploadFileResponse.class);
                if (response.getError()==0) {
                    imgurl+=response.getUrl()+",";
                } else {
                    System.out.println("===========img:"+str+" upload error!");
                }
            }
            imgurl = imgurl.substring(0,imgurl.length()-1);

        }catch (Exception e){
            operateLogService.insertSelective("autouser", "调用文件上传接口：" +urlAddress + uploadFileMethod+"失败！");
            System.out.println("---------调用文件上传接口失败："+e.getMessage());
            e.printStackTrace();
            return "";
        }
        return imgurl;
    }
}
