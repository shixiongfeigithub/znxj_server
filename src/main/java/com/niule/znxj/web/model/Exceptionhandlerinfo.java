package com.niule.znxj.web.model;

import java.util.Date;

public class Exceptionhandlerinfo {
    private Long id;

    private Long reportid;

    private Long reportcontentid;

    private Long checkuserid;

    private String descontent;

    private String attachment;

    private Long operatorid;

    private String operatorname;

    private Date reporttime;

    private Date confirmtime;

    private Date appointedtime;

    private Date exceptionclosetime;

    private Integer exceptionstate;

    private String exceptiontype;

    private String exceptionlever;

    private Integer frequency;

    private Integer uploadstate;

    private Date uploadtime;

    private String equipname;
    private String checkname;
    private String reportimg;
    private String worker;
    private String username;
    private Date donetime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEquipname() {
        return equipname;
    }

    public void setEquipname(String equipname) {
        this.equipname = equipname;
    }

    public String getCheckname() {
        return checkname;
    }

    public void setCheckname(String checkname) {
        this.checkname = checkname;
    }

    public String getReportimg() {
        return reportimg;
    }

    public void setReportimg(String reportimg) {
        this.reportimg = reportimg;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public Date getDonetime() {
        return donetime;
    }

    public void setDonetime(Date donetime) {
        this.donetime = donetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportid() {
        return reportid;
    }

    public void setReportid(Long reportid) {
        this.reportid = reportid;
    }

    public Long getReportcontentid() {
        return reportcontentid;
    }

    public void setReportcontentid(Long reportcontentid) {
        this.reportcontentid = reportcontentid;
    }

    public Long getCheckuserid() {
        return checkuserid;
    }

    public void setCheckuserid(Long checkuserid) {
        this.checkuserid = checkuserid;
    }

    public String getDescontent() {
        return descontent;
    }

    public void setDescontent(String descontent) {
        this.descontent = descontent == null ? null : descontent.trim();
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment == null ? null : attachment.trim();
    }

    public Long getOperatorid() {
        return operatorid;
    }

    public void setOperatorid(Long operatorid) {
        this.operatorid = operatorid;
    }

    public String getOperatorname() {
        return operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname == null ? null : operatorname.trim();
    }

    public Date getReporttime() {
        return reporttime;
    }

    public void setReporttime(Date reporttime) {
        this.reporttime = reporttime;
    }

    public Date getConfirmtime() {
        return confirmtime;
    }

    public void setConfirmtime(Date confirmtime) {
        this.confirmtime = confirmtime;
    }

    public Date getAppointedtime() {
        return appointedtime;
    }

    public void setAppointedtime(Date appointedtime) {
        this.appointedtime = appointedtime;
    }

    public Date getExceptionclosetime() {
        return exceptionclosetime;
    }

    public void setExceptionclosetime(Date exceptionclosetime) {
        this.exceptionclosetime = exceptionclosetime;
    }

    public Integer getExceptionstate() {
        return exceptionstate;
    }

    public void setExceptionstate(Integer exceptionstate) {
        this.exceptionstate = exceptionstate;
    }

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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getUploadstate() {
        return uploadstate;
    }

    public void setUploadstate(Integer uploadstate) {
        this.uploadstate = uploadstate;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }
}