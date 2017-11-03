package com.zhongan.icare.message.im.web;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.icare.message.im.service.IImLogService;
import com.zhongan.icare.message.im.service.ImAccountService;
import com.zhongan.icare.message.im.web.dto.ImLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by za-raozhikun on 2017/8/14.
 */
@Slf4j
@RestController
@RequestMapping(path = "/v1/im/callback")
public class ImCallbackController {
    @Resource
    private IImLogService imLogService;
    @Resource
    private ImAccountService imAccountService;

    @RequestMapping
    public String callback(@RequestParam(name = "SdkAppid", required = false, defaultValue = "") String sdkAppid,
                           @RequestParam(name = "CallbackCommand", required = false, defaultValue = "") String callbackCommand,
                           @RequestParam(name = "contenttype", required = false, defaultValue = "") String contentType,
                           @RequestParam(name = "ClientIP", required = false, defaultValue = "") String clientIP,
                           @RequestParam(name = "OptPlatform", required = false, defaultValue = "") String optPlatform,
                           @RequestBody(required = false) String requestBody) {
        log.info("Im Callback : sdkAppId={}, CallbackCommand={}, contenttype={}, ClientIP={}, OptPlatform={}, requestBody={}", sdkAppid, callbackCommand, contentType, clientIP, optPlatform, requestBody);

        try {
            JSONObject jsonObject = JSONObject.parseObject(requestBody);
            ImLogDTO imLogDTO = new ImLogDTO();
            String custId = jsonObject.getString("From_Account");
            if (StringUtils.isEmpty(custId)) {
                custId = jsonObject.getString("Operator_Account");
            }
            if (StringUtils.isEmpty(custId)) {
                custId = jsonObject.getString("Owner_Account");
            }
            if (StringUtils.isEmpty(custId)){
                JSONObject info = jsonObject.getJSONObject("Info");
                if (info != null) {
                    custId = info.getString("To_Account");
                }
            }
            try {
                custId = imAccountService.decryptAccountId(custId);
            } catch (Exception e) {
            }
            imLogDTO.setCustId(custId);
            imLogDTO.setCommand(callbackCommand);
            imLogDTO.setClientIp(clientIP);
            imLogDTO.setOptPlatform(optPlatform);
            imLogDTO.setContent(requestBody);
            imLogService.create(imLogDTO);
        } catch (Exception e) {
            log.error("IM callback info fail to db:{}", e.getMessage(), e);
        }

        return "{\n" +
                "    \"ActionStatus\": \"OK\", \n" +
                "    \"ErrorInfo\": \"\", \n" +
                "    \"ErrorCode\": 0\n" +
                "}";
    }

}

