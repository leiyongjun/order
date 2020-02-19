package com.bdqn.order.pojo;

import java.util.Date;

public class OperLog {
    private Integer id;

    private String oprType;

    private String oprName;

    private String oprDesc;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOprType() {
        return oprType;
    }

    public void setOprType(String oprType) {
        this.oprType = oprType == null ? null : oprType.trim();
    }

    public String getOprName() {
        return oprName;
    }

    public void setOprName(String oprName) {
        this.oprName = oprName == null ? null : oprName.trim();
    }

    public String getOprDesc() {
        return oprDesc;
    }

    public void setOprDesc(String oprDesc) {
        this.oprDesc = oprDesc == null ? null : oprDesc.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}