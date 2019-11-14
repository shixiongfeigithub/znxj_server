package com.niule.znxj.web.model.response;

/**
 * Created by xuqb
 * Date: 2019-11-14 13:59
 */
public class RestfulResponse {
    private Boolean status;
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
