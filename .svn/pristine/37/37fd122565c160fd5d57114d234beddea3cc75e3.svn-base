package com.zhongan.icare.message.push.mq.handler;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.utils.StringUtils;
import com.zhongan.health.common.utils.security.digest.MD5Utils;
import com.zhongan.icare.share.message.dto.PushRequestDataDTO;
import com.zhongan.icare.share.message.dto.PushRequestLogDTO;
import com.zhongan.icare.share.message.dto.PushTemplateDTO;
import com.zhongan.icare.share.message.enm.MessageEventType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class Message2SingleProcesser extends MessageCommonProcesser<Long>
{

    @Override
    public String name()
    {
        return MessageEventType.MSG_2_SINGLE.getCode();
    }

    /**
     * 计算
     *
     * @param id
     * @return
     */
    @Override
    protected String caclKey(Long id, String... keys)
    {
        try
        {
            return MD5Utils.digestAsBase64(commonKey(keys) + id.toString());
        } catch (Exception e)
        {
            log.error("", e);
            return null;
        }
    }

    @Override
    public void process(Long id)
    {
        // 查询消息
        PushRequestDataDTO pushRequestDataDTO= queryRequestData(id);
        if(pushRequestDataDTO == null) return;
        // 判断一下是否已经消费
        if(YesOrNo.YES.getCode().equals(pushRequestDataDTO.getIsDeleted().getCode())) {
            log.info("<<<<<<已经消费过Message2SingleProcesser，id={}",id);
            return;
        }
        log.info(">>>>>>开始消费Message2SingleProcesser.process,id={}",id);
        PushRequestLogDTO requestLogDTO = JSONObject.parseObject(pushRequestDataDTO.getData(),PushRequestLogDTO.class);
        if(!deleteRequestData(id))return;
        // 首先做消息的解析
        PushTemplateDTO pushTemplateDTO = loadPushTemplateDTO(requestLogDTO);
        if (pushTemplateDTO == null)
            return;
        if (!StringUtils.isBlank(requestLogDTO.getGroupCode()))
        {
            pushTemplateDTO.setGroupCode(requestLogDTO.getGroupCode());
        }
        Map<String, Object> contextMap = requestLogDTO.getDataMap();
        // 组装contextMap和初始化PushRequestLogDTO对象
        initLogDTO(requestLogDTO, pushTemplateDTO, contextMap);
        // 保存并且发送推送信息
        insertAndSendMessage(requestLogDTO, contextMap);
    }

    @Override
    public boolean match(String tag, String topic, String bizKey)
    {
        log.info("recieve kafka message tag : {}", tag);
        return MessageEventType.MSG_2_SINGLE.getCode().equals(tag);
    }

    @Override
    public void process(String tag, String bizKey, Long id)
    {
        // 首先检查是否是重复消费
        String key = caclKey(id, bizKey);
        if (isRepeatConsume(key))
        {
            log.warn("单个推送的重复消费，id:{},bizKey:{},key:{}", id, bizKey, key);
            return;
        }
        process(id);
    }

}
