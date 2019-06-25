package com.niule.znxj.web.model;

public class Areainfo {
    private Long id;

    private String customid;

    private String longitude;

    private String latitude;

    private Long plant;

    private Long nfctag;

    private String desccontent;

    private Double radiusnumber;
    private Siteareainfo site;
    private Nfcinfo nfc;

    public Siteareainfo getSite() {
        return site;
    }

    public void setSite(Siteareainfo site) {
        this.site = site;
    }

    public void setNfc(Nfcinfo nfc) {
        this.nfc = nfc;
    }

    public Nfcinfo getNfc() {

        return nfc;
    }

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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public Long getPlant() {
        return plant;
    }

    public void setPlant(Long plant) {
        this.plant = plant;
    }

    public Long getNfctag() {
        return nfctag;
    }

    public void setNfctag(Long nfctag) {
        this.nfctag = nfctag;
    }

    public String getDesccontent() {
        return desccontent;
    }

    public void setDesccontent(String desccontent) {
        this.desccontent = desccontent == null ? null : desccontent.trim();
    }

    public Double getRadiusnumber() {
        return radiusnumber;
    }

    public void setRadiusnumber(Double radiusnumber) {
        this.radiusnumber = radiusnumber;
    }
}