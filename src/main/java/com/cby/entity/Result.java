package com.cby.entity;


import javax.persistence.*;
import javax.persistence.Entity;

/**
 * Created by Ma on 2017/7/6.
 */
public class Result {

    private String status;
    private String message;
    private String policyNo;

    public Result() {
    }


    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }
}
