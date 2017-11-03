package com.zhongan.icare.message.push.enums;

public enum BaiduMessageTypeEnum {
    INFORM(1, "通知"),
    TOUCHUAN(0, "透传");
    private int    code;
    private String des;

    private BaiduMessageTypeEnum(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public int getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

}
