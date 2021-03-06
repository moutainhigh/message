package com.zhongan.icare.message.push.enums;

public enum IsDeleteEnum {
    YES("Y", "删除"),
    NO("N", "不删除");
    private String value;
    private String des;

    private IsDeleteEnum(String value, String des) {
        this.value = value;
        this.des = des;
    }

    public String getValue() {
        return value;
    }

    public String getDes() {
        return des;
    }

}
