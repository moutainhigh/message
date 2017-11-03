package com.zhongan.icare.message.push.dao.dataobject;

import com.zhongan.health.common.share.bean.BaseDataObject;

@lombok.Getter
@lombok.Setter
public class ImLogDO extends BaseDataObject {
    private static final long serialVersionUID = 1508747643107L;

    private String custId;

    private String command;

    private String clientIp;

    private String optPlatform;

    private String content;
}