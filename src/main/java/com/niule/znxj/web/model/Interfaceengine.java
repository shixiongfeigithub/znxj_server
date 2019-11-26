package com.niule.znxj.web.model;

import java.util.Date;

public class Interfaceengine {
    private Long id;

    private Long siteid;


    private String sitename;

    private String ip;

    private String port;

    private String enginetype;

    private Date createtime;

    private Integer state;

    private String email;

    private Integer sendemailstate;

    private Date updatetime;

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSendemailstate() {
        return sendemailstate;
    }

    public void setSendemailstate(Integer sendemailstate) {
        this.sendemailstate = sendemailstate;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public String getEnginetype() {
        return enginetype;
    }

    public void setEnginetype(String enginetype) {
        this.enginetype = enginetype == null ? null : enginetype.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatettime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}