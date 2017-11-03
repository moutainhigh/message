package com.zhongan.icare.message.star;

import java.net.SocketException;
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
import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.constants.CommonErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信REST API
 * 
 * @author lirong
 * @date 2016-6-24
 */
@RestController
@RequestMapping(path = "/v1", consumes = { "application/json" })
@Slf4j
public class SmsController {
    @Autowired
    private StarHttpTemplate starHttpTemplate;

    //    /**
    //     * 发送短信
    //     */
    //    @SuppressWarnings("rawtypes")
    //    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    //    public BaseResult<Map<String, String>> sendSms(@RequestBody String jsonBody) {
    //        log.info("收到发送短信请求: info={}", jsonBody);
    //        BaseResult<Map<String, String>> result = new BaseResult<>();
    //
    //        // 校验参数
    //        if (StringUtils.isEmpty(jsonBody)) {
    //            log.info("发送短信失败, 请求内容不能为空");
    //            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
    //            result.setMessage("请求内容不能为空");
    //            return result;
    //        }
    //        JSONObject data;
    //        try {
    //            data = JSON.parseObject(jsonBody);
    //        } catch (Exception e) {
    //            log.info("发送短信失败, 请求内容不是合法的JSON数据");
    //            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
    //            result.setMessage("请求内容不是合法的JSON数据");
    //            return result;
    //        }
    //        String mobiles = data.getString("mobiles");
    //        String templateNo = data.getString("templateNo");
    //        if (StringUtils.isEmpty(templateNo)) {
    //            log.info("发送短信失败, 短信模板不能为空");
    //            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
    //            result.setMessage("短信模板不能为空");
    //            return result;
    //        }
    //        if (StringUtils.isEmpty(mobiles)) {
    //            log.info("发送短信失败, 短信收信人不能为空");
    //            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
    //            result.setMessage("短信收信人不能为空");
    //            return result;
    //        }
    //
    //        // 调用满天星发送短信
    //        try {
    //            Map<String, String> postData = new HashMap<String, String>();
    //
    //            Map<String, Object> map = new HashMap<String, Object>();
    //            map.put("mobiles", mobiles);
    //            map.put("userNo", "icare");
    //            map.put("templateNo", templateNo);
    //            Map params = data.getJSONObject("parameters").toJavaObject(Map.class);
    //            map.put("parameters", params);
    //
    //            Object[] obj = new Object[] { map };
    //            String[] type = { "com.zhongan.pigeon.dto.SimpleSmsMessageDto" };
    //            postData.put("ArgsObjects", JSON.toJSONString(obj));
    //            postData.put("ArgsTypes", JSON.toJSONString(type));
    //
    //            String response = starHttpTemplate.postForm4Hsf("sendSms", postData);
    //            JSONObject responseJson = JSON.parseObject(response);
    //            Boolean sendResult = responseJson.getBoolean("isSuccess");
    //            if (sendResult) {
    //                log.info("发送短信成功");
    //                result.setCodeSuccess();
    //                result.setMessage("发送短信成功");
    //            } else {
    //                log.info("发送短信失败, response={}", response);
    //                String errorMessage = responseJson.getString("errorMessage");
    //                result.setCode(CommonErrorCode.CMN_THIRD_ERROR.getCode());
    //                result.setMessage("发送短信失败，" + errorMessage);
    //            }
    //        } catch (Exception e) {
    //            log.info("发送短信失败", e);
    //            result.setCode(CommonErrorCode.CMN_BUZ_ERROR.getCode());
    //            result.setMessage("发送短信失败，" + e.getMessage());
    //        }
    //
    //        return result;
    //    }

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public BaseResult<Map<String, String>> sendSms(@RequestBody String jsonBody) {
        log.info("收到发送短信请求: info={}", jsonBody);
        BaseResult<Map<String, String>> result = new BaseResult<>();

        // 校验参数
        if (StringUtils.isEmpty(jsonBody)) {
            log.info("发送短信失败, 请求内容不能为空");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("请求内容不能为空");
            return result;
        }
        JSONObject data;
        try {
            data = JSON.parseObject(jsonBody);
        } catch (Exception e) {
            log.info("发送短信失败, 请求内容不是合法的JSON数据");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("请求内容不是合法的JSON数据");
            return result;
        }
        String mobiles = data.getString("mobiles");
        String templateNo = data.getString("templateNo");
        if (StringUtils.isEmpty(templateNo)) {
            log.info("发送短信失败, 短信模板不能为空");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("短信模板不能为空");
            return result;
        }
        if (StringUtils.isEmpty(mobiles)) {
            log.info("发送短信失败, 短信收信人不能为空");
            result.setCode(CommonErrorCode.CMN_ILLEGAL_ARG.getCode());
            result.setMessage("短信收信人不能为空");
            return result;
        }
        Map<String, String> postData = new HashMap<String, String>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mobiles", mobiles);
        map.put("userNo", "icare");
        map.put("templateNo", templateNo);
        Map params = data.getJSONObject("parameters").toJavaObject(Map.class);
        map.put("parameters", params);

        Object[] obj = new Object[] { map };
        String[] type = { "com.zhongan.pigeon.dto.SimpleSmsMessageDto" };
        postData.put("ArgsObjects", JSON.toJSONString(obj));
        postData.put("ArgsTypes", JSON.toJSONString(type));
        return retrySendSms(postData, 3);
    }

    private BaseResult<Map<String, String>> retrySendSms(@RequestBody Map<String, String> postData, int i) {
        BaseResult<Map<String, String>> result = new BaseResult<>();
        // 调用满天星发送短信
        try {
            String response = starHttpTemplate.postForm4Hsf("sendSms", postData);
            JSONObject responseJson = JSON.parseObject(response);
            Boolean sendResult = responseJson.getBoolean("isSuccess");
            if (sendResult) {
                log.info("发送短信成功");
                result.setCodeSuccess();
                result.setMessage("发送短信成功");
            } else {
                log.error("发送短信失败, response={},postData{}", response, JSON.toJSONString(postData));
                String errorMessage = responseJson.getString("errorMessage");
                result.setCode(CommonErrorCode.CMN_THIRD_ERROR.getCode());
                result.setMessage("发送短信失败，" + errorMessage);
            }
        } catch (Exception e) {
            try {
                if (e.getCause() instanceof SocketException) {
                    log.error("发送短信SocketException失败" + JSON.toJSONString(postData) + ",i=" + i, e);
                    if (i <= 0) {
                        result.setCode(CommonErrorCode.CMN_BUZ_ERROR.getCode());
                        result.setMessage("发送短信失败，" + e.getMessage());
                        return result;
                    }
                    return retrySendSms(postData, --i);
                }
            } catch (Exception e1) {
                log.error("发送短信失败soket" + JSON.toJSONString(postData), e);
            }
            log.error("发送短信失败" + JSON.toJSONString(postData), e);
            result.setCode(CommonErrorCode.CMN_BUZ_ERROR.getCode());
            result.setMessage("发送短信失败，" + e.getMessage());
        }
        return result;
    }
}
