package com.cby.entity;

import javax.persistence.*;

/**
 * Created by Ma on 2017/7/18.
 */
@javax.persistence.Entity
public class IdCard {
    @Id
    private Integer id;
    private String content;

    public IdCard() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
