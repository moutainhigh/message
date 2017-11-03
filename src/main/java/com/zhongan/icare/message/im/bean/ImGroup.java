package com.zhongan.icare.message.im.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by za-raozhikun on 2017/10/20.
 */
@Setter
@Getter
public class ImGroup {

    @JSONField(name = "GroupId")
    private String id;

    @JSONField(name = "Owner_Account")
    private String ownerAccount;

    @JSONField(name = "Type")
    private String type = "Private";

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "MaxMemberCount")
    private int maxMemberCount = 10000;

    @JSONField(name = "FaceUrl")
    private String faceUrl;

}
