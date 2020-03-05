package com.bdqn.order.pojo;

import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ToString
public class UserInfo implements Serializable {
    private Integer userId;

    private String userName;

    private String userPwd;

    private String imgCode;

    private Integer pwErrCount;

    private String status;

    private BigDecimal userBalance;

    private Date createTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd == null ? null : userPwd.trim();
    }

    public Integer getPwErrCount() {
        return pwErrCount;
    }

    public void setPwErrCount(Integer pwErrCount) {
        this.pwErrCount = pwErrCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public BigDecimal getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getImgCode() {
        return imgCode;
    }

    public void setImgCode(String imgCode) {
        this.imgCode = imgCode;
    }
}