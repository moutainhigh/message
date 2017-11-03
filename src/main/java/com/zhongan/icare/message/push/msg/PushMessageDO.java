package com.zhongan.icare.message.push.msg;

import java.util.Map;

public class PushMessageDO {
    private String title;
    private String description;
    private String notification_builder_id;
    private Map    custom_content;
    private Map    aps;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotification_builder_id() {
        return notification_builder_id;
    }

    public void setNotification_builder_id(String notification_builder_id) {
        this.notification_builder_id = notification_builder_id;
    }

    public Map getCustom_content() {
        return custom_content;
    }

    public void setCustom_content(Map custom_content) {
        this.custom_content = custom_content;
    }

    public Map getAps() {
        return aps;
    }

    public void setAps(Map aps) {
        this.aps = aps;
    }

}
