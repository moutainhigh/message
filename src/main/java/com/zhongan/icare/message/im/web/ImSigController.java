package com.zhongan.icare.message.im.web;

import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.health.common.utils.security.crypt.impl.SymmetricCipher;
import com.zhongan.icare.common.client.ClientInfo;
import com.zhongan.icare.message.im.service.ImSigService;
import com.zhongan.icare.share.common.constants.ErrorCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by za-raozhikun on 2017/8/14.
 */
@Slf4j
@RestController
@RequestMapping(path = "/v1/im/sig")
public class ImSigController {

    @Resource
    private ImSigService imSigService;

    @Resource
    private SymmetricCipher symmetricCipher;

    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.POST}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResult<String> generateImSig(@RequestBody GenerateImSigRequest generateImSigRequest) {
        ClientInfo clientInfo = ClientInfo.client();
        if (clientInfo == null || clientInfo.getCustId() == 0) {
            throw ExceptionUtils.throwException(ErrorCode.CUST_LOGIN_ACCT_EXPIRED);
        } else if (!symmetricCipher.encrypt(String.valueOf(clientInfo.getCustId())).equals(generateImSigRequest.getCustId())) {
            throw ExceptionUtils.throwException(ErrorCode.CMN_ILLEGAL_OPERATION, "登录用户校验失败");
        }
        BaseResult<String> result = ExceptionUtils.success(imSigService.generateImSig(generateImSigRequest.getCustId()));
        log.info("Generate ImSig: custId={}, sig={}", generateImSigRequest.getCustId(), result.getResult());
        return result;
    }

    @RequestMapping(params = "check=0", method = {RequestMethod.PUT, RequestMethod.POST}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResult<String> generateImSigNotCheck(@RequestBody GenerateImSigRequest generateImSigRequest) {
        BaseResult<String> result = ExceptionUtils.success(imSigService.generateImSig(generateImSigRequest.getCustId()));
        log.info("Generate ImSig: custId={}, sig={}", generateImSigRequest.getCustId(), result.getResult());
        return result;
    }

    @RequestMapping(path = "check", method = {RequestMethod.PUT, RequestMethod.POST}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResult<Boolean> checkImSig(@RequestBody CheckImSigRequest checkImSigRequest) {
        BaseResult<Boolean> result = ExceptionUtils.success(imSigService.checkImSig(checkImSigRequest.getCustId(), checkImSigRequest.getSig()));
        log.info("Check ImSig: custId={}, result={}, sig={}", checkImSigRequest.getCustId(), result.getResult(), checkImSigRequest.getSig());
        return result;
    }

    @ExceptionHandler
    public BaseResult<String> catchExceptions(Exception e) {
        log.warn("Im Sig Filed : msg={}", e.getMessage(), e);
        return ExceptionUtils.fail(ErrorCode.SYS_RUNTIME_ERROR);
    }

    @Data
    private static class GenerateImSigRequest {
        private String custId;
    }

    @Data
    private static class CheckImSigRequest {
        private String custId;
        private String sig;
    }
}


