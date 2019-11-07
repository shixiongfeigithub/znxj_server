package com.niule.znxj.core.entity;

/**
 * Created by xuqb
 * Date: 2019-11-07 15:48
 */
public class Yhjl {

    private String checkresult_flag; //巡检记录唯一标识，与巡检记录关联的字段
    private String checkpointname; //检查点名称
    private String checkcontent; //检查内容
    private String dangerstatus; //隐患状态(初始  0未整改   1整改完成   2整改未完成   3已完成 )
    private String findperson; //隐患发现人
    private String createtime; //隐患发现时间
    private String handletime; //计划整改时间
    private String dangerdesc; //隐患备注
    private String dangerfj; //隐患附件,多文件用逗号隔开
    private String handlepersons; //指定隐患整改人
    private String reformperson; //整改人
    private String reformtime; //整改备注
    private String reformfj; //整改附件
    private String reformdesc; //整改时间

    public Yhjl() {
    }

    public Yhjl(String checkresult_flag, String checkpointname, String checkcontent, String dangerstatus, String findperson, String createtime, String handletime, String dangerdesc, String dangerfj, String handlepersons, String reformperson, String reformtime, String reformfj, String reformdesc) {
        this.checkresult_flag = checkresult_flag;
        this.checkpointname = checkpointname;
        this.checkcontent = checkcontent;
        this.dangerstatus = dangerstatus;
        this.findperson = findperson;
        this.createtime = createtime;
        this.handletime = handletime;
        this.dangerdesc = dangerdesc;
        this.dangerfj = dangerfj;
        this.handlepersons = handlepersons;
        this.reformperson = reformperson;
        this.reformtime = reformtime;
        this.reformfj = reformfj;
        this.reformdesc = reformdesc;
    }

    public String getCheckresult_flag() {
        return checkresult_flag;
    }

    public void setCheckresult_flag(String checkresult_flag) {
        this.checkresult_flag = checkresult_flag;
    }

    public String getCheckpointname() {
        return checkpointname;
    }

    public void setCheckpointname(String checkpointname) {
        this.checkpointname = checkpointname;
    }

    public String getCheckcontent() {
        return checkcontent;
    }

    public void setCheckcontent(String checkcontent) {
        this.checkcontent = checkcontent;
    }

    public String getDangerstatus() {
        return dangerstatus;
    }

    public void setDangerstatus(String dangerstatus) {
        this.dangerstatus = dangerstatus;
    }

    public String getFindperson() {
        return findperson;
    }

    public void setFindperson(String findperson) {
        this.findperson = findperson;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getHandletime() {
        return handletime;
    }

    public void setHandletime(String handletime) {
        this.handletime = handletime;
    }

    public String getDangerdesc() {
        return dangerdesc;
    }

    public void setDangerdesc(String dangerdesc) {
        this.dangerdesc = dangerdesc;
    }

    public String getDangerfj() {
        return dangerfj;
    }

    public void setDangerfj(String dangerfj) {
        this.dangerfj = dangerfj;
    }

    public String getHandlepersons() {
        return handlepersons;
    }

    public void setHandlepersons(String handlepersons) {
        this.handlepersons = handlepersons;
    }

    public String getReformperson() {
        return reformperson;
    }

    public void setReformperson(String reformperson) {
        this.reformperson = reformperson;
    }

    public String getReformtime() {
        return reformtime;
    }

    public void setReformtime(String reformtime) {
        this.reformtime = reformtime;
    }

    public String getReformfj() {
        return reformfj;
    }

    public void setReformfj(String reformfj) {
        this.reformfj = reformfj;
    }

    public String getReformdesc() {
        return reformdesc;
    }

    public void setReformdesc(String reformdesc) {
        this.reformdesc = reformdesc;
    }
}
