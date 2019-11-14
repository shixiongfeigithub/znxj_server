package com.niule.znxj.core.entity;

/**
 * Created by xuqb
 * Date: 2019-11-07 15:31
 */
public class Xjjl {

    private String flag; //巡检记录唯一标识
    private String checkorder; //班次
    private String checkperson; //巡检人
    private String checkfj; //附件地址，多个用逗号隔开
    private String checkpointname; //检查点
    private String checkresult; //检查结果（1有隐患/0无隐患）
    private String createtime; //检查时间（2019-11-04 16:24:56）
    private String note; //巡检备注

    public Xjjl() {
    }

    public Xjjl(String flag, String checkorder, String checkperson, String checkfj, String checkpointname, String checkresult, String createtime, String note) {
        this.flag = flag;
        this.checkorder = checkorder;
        this.checkperson = checkperson;
        this.checkfj = checkfj;
        this.checkpointname = checkpointname;
        this.checkresult = checkresult;
        this.createtime = createtime;
        this.note = note;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCheckorder() {
        return checkorder;
    }

    public void setCheckorder(String checkorder) {
        this.checkorder = checkorder;
    }

    public String getCheckperson() {
        return checkperson;
    }

    public void setCheckperson(String checkperson) {
        this.checkperson = checkperson;
    }

    public String getCheckfj() {
        return checkfj;
    }

    public void setCheckfj(String checkfj) {
        this.checkfj = checkfj;
    }

    public String getCheckpointname() {
        return checkpointname;
    }

    public void setCheckpointname(String checkpointname) {
        this.checkpointname = checkpointname;
    }

    public String getCheckresult() {
        return checkresult;
    }

    public void setCheckresult(String checkresult) {
        this.checkresult = checkresult;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String  createtime) {
        this.createtime = createtime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
