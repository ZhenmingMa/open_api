package com.cby.entity;

import javax.persistence.*;

/**
 * Created by Ma on 2017/7/18.
 */
@javax.persistence.Entity
public class Company {
    @Id
    @GeneratedValue
    private Integer id;
    private String companyCode;
    private String companyName;
    private Integer M_count;

    private Integer M_beijing_count;
    private Integer M_guangdong_count;
    private Integer M_fujian_count;
    private Integer M_jiangsu_count;
    private Integer M_zhejiang_count;
    private Integer M_liaoning_count;
    private Integer M_sichuang_count;
    private Integer M_chongqing_count;
    private Integer M_hubei_count;
    private Integer M_shanghai_count;
    private Integer M_tianjin_count;

    public Integer getM_tianjin_count() {
        return M_tianjin_count;
    }

    public void setM_tianjin_count(Integer m_tianjin_count) {
        M_tianjin_count = m_tianjin_count;
    }

    public Integer getM_count() {
        return M_count;
    }

    public void setM_count(Integer m_count) {
        M_count = m_count;
    }

    public Integer getM_beijing_count() {
        return M_beijing_count;
    }

    public void setM_beijing_count(Integer m_beijing_count) {
        M_beijing_count = m_beijing_count;
    }

    public Integer getM_guangdong_count() {
        return M_guangdong_count;
    }

    public void setM_guangdong_count(Integer m_guangdong_count) {
        M_guangdong_count = m_guangdong_count;
    }

    public Integer getM_fujian_count() {
        return M_fujian_count;
    }

    public void setM_fujian_count(Integer m_fujian_count) {
        M_fujian_count = m_fujian_count;
    }

    public Integer getM_jiangsu_count() {
        return M_jiangsu_count;
    }

    public void setM_jiangsu_count(Integer m_jiangsu_count) {
        M_jiangsu_count = m_jiangsu_count;
    }

    public Integer getM_zhejiang_count() {
        return M_zhejiang_count;
    }

    public void setM_zhejiang_count(Integer m_zhejiang_count) {
        M_zhejiang_count = m_zhejiang_count;
    }

    public Integer getM_liaoning_count() {
        return M_liaoning_count;
    }

    public void setM_liaoning_count(Integer m_liaoning_count) {
        M_liaoning_count = m_liaoning_count;
    }

    public Integer getM_sichuang_count() {
        return M_sichuang_count;
    }

    public void setM_sichuang_count(Integer m_sichuang_count) {
        M_sichuang_count = m_sichuang_count;
    }

    public Integer getM_chongqing_count() {
        return M_chongqing_count;
    }

    public void setM_chongqing_count(Integer m_chongqing_count) {
        M_chongqing_count = m_chongqing_count;
    }

    public Integer getM_hubei_count() {
        return M_hubei_count;
    }

    public void setM_hubei_count(Integer m_hubei_count) {
        M_hubei_count = m_hubei_count;
    }

    public Integer getM_shanghai_count() {
        return M_shanghai_count;
    }

    public void setM_shanghai_count(Integer m_shanghai_count) {
        M_shanghai_count = m_shanghai_count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Company() {

    }
}
