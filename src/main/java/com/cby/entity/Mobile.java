package com.cby.entity;

import javax.persistence.*;

/**
 * Created by Ma on 2017/7/17.
 */
@javax.persistence.Entity
public class Mobile {
    @Id
    private Integer id;
    private Integer phone;
    private String province;
    private String city;

    public Mobile() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

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

    public Mobile(Integer id, Integer phone, String province, String city) {
        this.id = id;
        this.phone = phone;
        this.province = province;
        this.city = city;
    }
}

