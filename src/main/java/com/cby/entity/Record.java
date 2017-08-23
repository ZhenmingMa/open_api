package com.cby.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ma on 2017/7/24.
 */
@javax.persistence.Entity
public class Record {
    @Id
    @GeneratedValue
    private Integer id;
    private String companyCode;
    private String identifer;
    private String type;
    private String status;
    private String message;
    private String freeInsureNo;
    private String phone;
    private String name;
    private String sex;
    private String birthday;
    private String province;
    private String city;
    private String address;

    private Date date;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFreeInsureNo() {
        return freeInsureNo;
    }

    public void setFreeInsureNo(String freeInsureNo) {
        this.freeInsureNo = freeInsureNo;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdentifer() {
        return identifer;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }

    public Record() {

    }
}
