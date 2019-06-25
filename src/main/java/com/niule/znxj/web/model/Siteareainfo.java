package com.niule.znxj.web.model;

public class Siteareainfo {
    private Long id;

    private String customid;

    private String desccontent;

    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomid() {
        return customid;
    }

    public void setCustomid(String customid) {
        this.customid = customid == null ? null : customid.trim();
    }

    public String getDesccontent() {
        return desccontent;
    }

    public void setDesccontent(String desccontent) {
        this.desccontent = desccontent == null ? null : desccontent.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}