package com.zhongan.icare.message.star;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.constants.CommonErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * 语音REST API
 * 
 * @author lirong
 * @date 2016-6-24
 */
@RestController
@RequestMapping(path = "/v1/voice", consumes = { "application/json" })
@Slf4j
public class VoiceController {
    @Autowired
    private StarHttpTemplate starHttpTemplate;

    /**
     * 发送语音
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResult<Map<String, String>> sendVoice(@RequestBody String jsonBody) {
        log.info("收到发送语音请求: info={}", jsonBody);
        BaseResult<Map<String, String>> result = new BaseResult<>();

        // 校验参数
        if (StringUtils.isEmpty(jsonBody)) {
            log.info("发送语音失败, 请求内容不能为空");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("请求内容不能为空");
            return result;
        }
        JSONObject data;
        try {
            data = JSON.parseObject(jsonBody);
        } catch (Exception e) {
            log.info("发送语音失败, 请求内容不是合法的JSON数据");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("请求内容不是合法的JSON数据");
            return result;
        }
        String content = data.getString("content");
        if (StringUtils.isEmpty(content)) {
            log.info("发送语音失败, 语音内容为空");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("语音内容为空");
            return result;
        }
        String mobiles = data.getString("mobiles");
        if (StringUtils.isEmpty(mobiles)) {
            log.info("发送语音失败, 语音收信人不能为空");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("语音收信人不能为空");
            return result;
        }

        // 调用满天星发送语音
        try {
            Map<String, String> postData = new HashMap<String, String>();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mobiles", mobiles);
            map.put("templateNo", "YUYIN");// 必填
            map.put("userNo", "icare");
            Map<String, Object> params = Maps.newHashMap();
            params.put("content", content);//4-6位阿拉伯数字
            map.put("parameters", params);

            Object[] obj = new Object[] { map };
            String[] type = { "com.zhongan.pigeon.dto.SimpleSmsMessageDto" };
            postData.put("ArgsObjects", JSON.toJSONString(obj));
            postData.put("ArgsTypes", JSON.toJSONString(type));

            String response = starHttpTemplate.postForm4Hsf("sendSms", postData);
            JSONObject responseJson = JSON.parseObject(response);
            Boolean sendResult = responseJson.getBoolean("isSuccess");
            if (sendResult) {
                log.info("发送语音成功");
                result.setCodeSuccess();
                result.setMessage("发送语音成功");
            } else {
                log.info("发送语音失败, response={}", response);
                String errorMessage = responseJson.getString("errorMessage");
                result.setCode(CommonErrorCode.CMN_THIRD_ERROR.getCode());
                result.setMessage("发送语音失败，" + errorMessage);
            }
        } catch (Exception e) {
            log.info("发送语音失败", e);
            result.setCode(CommonErrorCode.CMN_BUZ_ERROR.getCode());
            result.setMessage("发送语音失败，" + e.getMessage());
        }

        return result;
    }
}
