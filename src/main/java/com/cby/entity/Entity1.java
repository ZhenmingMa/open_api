package com.cby.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ma on 2017/8/16.
 */
@javax.persistence.Entity
public class Entity1 {
    @Id
    private String id;
    private String name;   //姓名
    private String idCard; //身份证号
    private String phone;  //电话
    private String email;  //邮箱
    private String identifer; //乙方标识符
    private String companyCode;
    private String type;     //投保类型
    private String sex;     //性别
    private String birthday; //生日
    private String province; //省份
    private String city;    //市
    private String address; //地址
    private boolean isSuccess; //是否成功投保
    private String policyNo; //保单号
    private String policyFrom; //保险来源
    private Date date;   //日期


    public Entity1() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentifer() {
        return identifer;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
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

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getPolicyFrom() {
        return policyFrom;
    }

    public void setPolicyFrom(String policyFrom) {
        this.policyFrom = policyFrom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Entity1( String name, String idCard, String phone, String email, String identifer, String companyCode, String type, String sex, String birthday, String province, String city, String address) {
        this.name = name;
        this.idCard = idCard;
        this.phone = phone;
        this.email = email;
        this.identifer = identifer;
        this.companyCode = companyCode;
        this.type = type;
        this.sex = sex;
        this.birthday = birthday;
        this.province = province;
        this.city = city;
        this.address = address;
    }
}
