package com.zhongan.icare.message.push.enums;

public enum PushDefaultValueEnum {
    ANDROID_NOTIFICATION_BUILDER_ID_0("0", "android客户端默认的通知样式"),
    IOS_SOUND_DEFAULT("default", "ios默认的系统音");
    private String      value;
    private String      des;

    private PushDefaultValueEnum(String value, String des) {
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
