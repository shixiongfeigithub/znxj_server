package com.niule.znxj.web.model;

import java.util.Date;

public class Taskuploadconfig {
    private Long id;

    private Long siteid;

    private Long taskid;

    private Long contactid;

    private Integer uploadstate;

    private Date createtime;

    private Date updatetime;

    private String sitename;

    private String taskname;

    private String contactname;

    private String exceptiontype;

    private String exceptionlever;

    public String getExceptiontype() {
        return exceptiontype;
    }

    public void setExceptiontype(String exceptiontype) {
        this.exceptiontype = exceptiontype;
    }

    public String getExceptionlever() {
        return exceptionlever;
    }

    public void setExceptionlever(String exceptionlever) {
        this.exceptionlever = exceptionlever;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public Long getContactid() {
        return contactid;
    }

    public void setContactid(Long contactid) {
        this.contactid = contactid;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSiteid() {
        return siteid;
    }

    public void setSiteid(Long siteid) {
        this.siteid = siteid;
    }

    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }

    public Integer getUploadstate() {
        return uploadstate;
    }

    public void setUploadstate(Integer uploadstate) {
        this.uploadstate = uploadstate;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}