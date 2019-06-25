package com.niule.znxj.web.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Taskreportinfo {
    private Long id;

    private Long taskid;
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date donetime;
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date uploadedtime;
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date uploadtime;

    private Long userid;

    private Long termimalid;

    private Integer tasktype;

    private Integer reportstate;

    private String worker;

    private Integer operationstate;

    private String taskcode;
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date starttime;
    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date endtime;

    private String logcat;

    private String content;

    private Userinfo user;

    private Terminalinfo ter;

    private Taskplaninfo task;

    private Taskstopinfo stop;

    private Tasktempinfo temp;

    public Taskstopinfo getStop() {
        return stop;
    }

    public void setStop(Taskstopinfo stop) {
        this.stop = stop;
    }

    public Tasktempinfo getTemp() {
        return temp;
    }

    public void setTemp(Tasktempinfo temp) {
        this.temp = temp;
    }

    public Taskplaninfo getTask() {
        return task;
    }

    public void setTask(Taskplaninfo task) {
        this.task = task;
    }

    public Userinfo getUser() {
        return user;
    }

    public void setUser(Userinfo user) {
        this.user = user;
    }

    public Terminalinfo getTer() {
        return ter;
    }

    public void setTer(Terminalinfo ter) {
        this.ter = ter;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }

    public Date getDonetime() {
        return donetime;
    }

    public void setDonetime(Date donetime) {
        this.donetime = donetime;
    }

    public Date getUploadedtime() {
        return uploadedtime;
    }

    public void setUploadedtime(Date uploadedtime) {
        this.uploadedtime = uploadedtime;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getTermimalid() {
        return termimalid;
    }

    public void setTermimalid(Long termimalid) {
        this.termimalid = termimalid;
    }

    public Integer getTasktype() {
        return tasktype;
    }

    public void setTasktype(Integer tasktype) {
        this.tasktype = tasktype;
    }

    public Integer getReportstate() {
        return reportstate;
    }

    public void setReportstate(Integer reportstate) {
        this.reportstate = reportstate;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker == null ? null : worker.trim();
    }

    public Integer getOperationstate() {
        return operationstate;
    }

    public void setOperationstate(Integer operationstate) {
        this.operationstate = operationstate;
    }

    public String getTaskcode() {
        return taskcode;
    }

    public void setTaskcode(String taskcode) {
        this.taskcode = taskcode == null ? null : taskcode.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getLogcat() {
        return logcat;
    }

    public void setLogcat(String logcat) {
        this.logcat = logcat == null ? null : logcat.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}