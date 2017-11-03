package com.zhongan.icare.message.im.service.impl;

import com.zhongan.health.common.utils.exception.BusinessException;
import com.zhongan.icare.message.im.service.ImSigService;
import com.zhongan.icare.message.im.util.TlsSigaturer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.zip.DataFormatException;

import static com.zhongan.icare.message.im.util.TlsSigaturer.CheckTLSSignatureEx;
import static com.zhongan.icare.message.im.util.TlsSigaturer.GenTLSSignatureEx;

/**
 * Created by za-raozhikun on 2017/8/14.
 */
@Slf4j
@Service
public class ImSigServiceImpl implements ImSigService {

    @Value("${za.icare.message.im.sdk.key.public}")
    private String publicKey;

    @Value("${za.icare.message.im.sdk.key.private}")
    private String privateKey;

    @Value("${za.icare.message.im.sdk.appId}")
    private long skdAppId;

    @Override
    public String generateImSig(String custId) {
        try {
            TlsSigaturer.GenTLSSignatureResult result = GenTLSSignatureEx(skdAppId, custId, privateKey);
            return result.urlSig;
        } catch (Exception e) {
            log.warn("Generate ImSig Failed : custId={}, msg={}", custId, e.getMessage(), e);
            throw new BusinessException("Generate ImSig Failed", e);
        }
    }

    @Override
    public boolean checkImSig(String custId, String sig) {
        TlsSigaturer.CheckTLSSignatureResult checkResult;
        try {
            checkResult = CheckTLSSignatureEx(sig, skdAppId, custId, publicKey);
        } catch (DataFormatException e) {
            log.warn("Check ImSig Failed : custId={}, sig={}, msg={}", custId, sig, e.getMessage(), e);
            throw new BusinessException("Check ImSig Failed", e);
        }
        return checkResult.verifyResult;
    }
}
