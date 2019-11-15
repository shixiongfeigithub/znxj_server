package com.niule.znxj.web.model.response;

/**
 * Created by xuqb
 * Date: 2019-11-14 13:59
 */
public class UploadFileResponse {
    private int error;
    private String fileName;
    private String url;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
