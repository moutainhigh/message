package com.zhongan.icare.message.im.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by za-raozhikun on 2017/8/14.
 */
@Data
public class ImAccount {

    @JSONField(name = "Identifier")
    private String id;

    @JSONField(name = "Nick")
    private String nickName;

    @JSONField(name = "FaceUrl")
    private String faceUrl;

    @JSONField(name = "Type")
    private int accountType = 0;

    @JSONField(serialize = false)
    private boolean idEncrypted = false;

}
