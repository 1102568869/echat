package tech.washmore.easychat.common.autocomplete;

/**
 * @author Washmore
 * @version V1.0
 * @summary TODO
 * @Copyright (c) 2018, Lianjia Group All Rights Reserved.
 * @since 2018/3/15
 */
public enum AutoCompleteTarget {
    Admin(80000L, "系统管理员"),
    CurrentLoginUser(-1L, ""),
    //
    ;
    private long userCode;
    private String userName;

    AutoCompleteTarget(long userCode, String userName) {
        this.userCode = userCode;
        this.userName = userName;
    }

    public long getUserCode() {
        return userCode;
    }

    public void setUserCode(long userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
