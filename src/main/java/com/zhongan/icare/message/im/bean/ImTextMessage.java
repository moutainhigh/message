package com.zhongan.icare.message.im.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by za-raozhikun on 2017/10/25.
 */
@Setter
@Getter
public class ImTextMessage {

    public ImTextMessage() {
    }

    public ImTextMessage(String message) {
        this.msgType = "TIMTextElem";
        ImMsgContent imMsgContent = new ImMsgContent();
        imMsgContent.setText(message);
        this.msgContent = imMsgContent;
    }

    @JSONField(name = "MsgType")
    private String msgType;

    @JSONField(name = "MsgContent")
    private ImMsgContent msgContent;

}
