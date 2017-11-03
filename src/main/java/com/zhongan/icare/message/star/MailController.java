package com.zhongan.icare.message.star;

import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.constants.CommonErrorCode;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.icare.share.common.constants.ErrorCode;
import com.zhongan.icare.share.message.dto.star.MailDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 邮件REST API
 * 
 * @author lirong
 * @date 2016-6-24
 */
@RestController
@RequestMapping(path = "/v1", consumes = { "application/json" })
@Slf4j
public class MailController {
    @Autowired
    private StarHttpTemplate    starHttpTemplate;
    @Value("${mail.from}")
    private String              defaultMailFrom;
    @Value("${za.icare.oss.accessId}")
    private String              ossAccessId;
    @Value("${za.icare.oss.accessKey}")
    private String              ossAccessKey;
    @Value("${za.icare.oss.endpoint}")
    private String              ossEndpoint;
    @Value("${za.icare.oss.bucketName}")
    private String              ossBucketName;

    private Map<String, String> ossAccessData;

    /**
     * 发送邮件
     */
    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public BaseResult<Map<String, String>> sendMail(@RequestBody MailDTO mail) {
        BaseResult<Map<String, String>> result = new BaseResult<>();
        if (mail == null || StringUtils.isEmpty(mail.getToMails())
                || (StringUtils.isEmpty(mail.getBodyHtml()) && StringUtils.isEmpty(mail.getTemplateNo()))
                || StringUtils.isEmpty(mail.getTitle())) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.CMN_ILLEGAL_ARG, "收件人,邮件内容不能为空");
        } else {
            log.info("收到发送邮件请求: info={}", JSON.toJSONString(mail));

            if (StringUtils.isEmpty(mail.getFromMail())) {
                mail.setFromMail(defaultMailFrom);
            }
            // 调用满天星发送邮件

            Map<String, Object> mailMap = new LinkedHashMap<String, Object>();

            Map<String, String> params = new HashMap<String, String>();
            if (mail.getMessageParams() != null) {
                params.putAll(mail.getMessageParams());
            }
            mailMap.put("toMails", mail.getToMails());
            mailMap.put("title", mail.getTitle());
            if (StringUtils.isNotEmpty(mail.getTemplateNo())) {
                mailMap.put("templateNo", mail.getTemplateNo());
            } else {
                mailMap.put("bodyHtml", mail.getBodyHtml());
            }
            mailMap.put("fromMail", mail.getFromMail());
            if (mailMap.get("userName") == null) {
                mailMap.put("userName", "最福利");
            } else {
                mailMap.put("userName", mailMap.get("userName"));

            }

            String[] attachements = mail.getMailAttachments();
            if (attachements != null && attachements.length > 0) {
                params.putAll(ossAccessData);
                mailMap.put("attachment", attachements[0]);
            }
            mailMap.put("parameters", JSON.toJSONString(params));
            return retrySendMail(mailMap, 3);

        }

        return result;
    }

    private BaseResult<Map<String, String>> retrySendMail(@RequestBody Map<String, Object> mailMap, int i) {
        BaseResult<Map<String, String>> result = new BaseResult<>();
        // 调用满天星发送短信
        try {
            String response = starHttpTemplate.post4Hsf("sendEmail", new String[] { "java.lang.String" },
                    new String[] { JSONObject.toJSONString(mailMap) });
            JSONObject responseJson = JSON.parseObject(response);
            Boolean sendResult = responseJson.getBoolean("isSuccess");
            if (sendResult) {
                log.info("发送邮件成功");
                result.setCodeSuccess();
                result.setMessage("发送邮件成功");
            } else {
                log.info("发送邮件失败, response={},mailMap={}", response, mailMap);
                String errorMessage = responseJson.getString("errorMessage");
                result.setCode(CommonErrorCode.CMN_THIRD_ERROR.getCode());
                result.setMessage("发送邮件失败，" + errorMessage);
            }
        } catch (Exception e) {
            try {
                if (e.getCause() instanceof SocketException) {
                    log.error("发送邮件SocketException失败" + JSON.toJSONString(mailMap) + ",i=" + i, e);
                    if (i <= 0) {
                        result.setCode(CommonErrorCode.CMN_BUZ_ERROR.getCode());
                        result.setMessage("发送邮件失败，" + e.getMessage());
                        return result;
                    }
                    return retrySendMail(mailMap, --i);
                }
            } catch (Exception e1) {
                log.error("发送邮件失败soket" + JSON.toJSONString(mailMap), e);
            }
            log.info("发送邮件失败", e);
            result.setCode(CommonErrorCode.CMN_BUZ_ERROR.getCode());
            result.setMessage("发送邮件失败，" + e.getMessage());
        }
        return result;
    }

    @PostConstruct
    public void init() {
        ossAccessData = new LinkedHashMap<>();
        ossAccessData.put("accessId", ossAccessId);
        ossAccessData.put("accessKey", ossAccessKey);
        ossAccessData.put("endpoint", ossEndpoint);
        ossAccessData.put("bucketName", ossBucketName);
    }
}
