package com.zhongan.icare.message.push.mq.handler;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.health.common.utils.security.digest.MD5Utils;
import com.zhongan.icare.share.message.dto.PushRequestLogDTO;
import com.zhongan.icare.share.message.dto.PushTemplateDTO;
import com.zhongan.icare.share.message.enm.MessageEventType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 目前发送全部的
 */
@Slf4j
public class Message2AllProcesser extends MessageCommonProcesser<PushRequestLogDTO>
{

    @Override
    public String name()
    {
        return MessageEventType.MSG_2_ALL.getCode();
    }

    @Override
    protected String caclKey(PushRequestLogDTO pushRequestLogDTO, String... keys)
    {
        try
        {
            return MD5Utils.digestAsBase64(commonKey(keys) + commonKey(pushRequestLogDTO));
        } catch (Exception e)
        {
            log.error("", e);
            return null;
        }
    }

    @Override
    public void process(PushRequestLogDTO requestLogDTO)
    {
        // 首先做消息的解析
        PushTemplateDTO pushTemplateDTO = loadPushTemplateDTO(requestLogDTO);
        if (pushTemplateDTO == null)
            return;
        Map<String, Object> contextMap = requestLogDTO.getDataMap();
        // 查询所有的设备信息，然后批量发送
        pushMsg2AllAndSaveData(contextMap, pushTemplateDTO, requestLogDTO);
    }

    @Override
    public boolean match(String tag, String topic, String bizKey)
    {
        log.info("recieve kafka message tag : {}", tag);
        return MessageEventType.MSG_2_ALL.getCode().equals(tag);
    }

    @Override
    public void process(String tag, String bizKey, PushRequestLogDTO requestLogDTO)
    {
        // 首先检查是否是重复消费
        String key = caclKey(requestLogDTO, bizKey);
        if (isRepeatConsume(key))
        {
            log.warn("推送给所有设备重复消费，requestLogDTO:{},bizKey:{},key:{}", JSONObject.toJSONString(requestLogDTO), bizKey, key);
            return;
        }
        process(requestLogDTO);
    }
}
