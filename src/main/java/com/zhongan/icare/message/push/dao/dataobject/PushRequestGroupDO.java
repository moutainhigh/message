package com.zhongan.icare.message.push.dao.dataobject;

import com.zhongan.health.common.share.bean.BaseDataObject;

/**
 * 组装消息和分组信息的对象
 */
public class PushRequestGroupDO extends BaseDataObject {
    private static final long serialVersionUID = 1481255861525L;

    /**
     * 请求消息的对象
     */
    private PushRequestLogDO    pushRequestLogDO;

    /**
     * 请求消息的分组信息
     */
    private PushGroupDO         pushGroupDO;

    public PushRequestLogDO getPushRequestLogDO() {
        return pushRequestLogDO;
    }

    public void setPushRequestLogDO(PushRequestLogDO pushRequestLogDO) {
        this.pushRequestLogDO = pushRequestLogDO;
    }

    public PushGroupDO getPushGroupDO() {
        return pushGroupDO;
    }

    public void setPushGroupDO(PushGroupDO pushGroupDO) {
        this.pushGroupDO = pushGroupDO;
    }
}