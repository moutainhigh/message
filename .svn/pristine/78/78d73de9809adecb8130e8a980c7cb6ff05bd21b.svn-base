package com.zhongan.icare.message.im.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.icare.message.im.service.ImAdminService;
import com.zhongan.icare.message.im.service.ImService;
import com.zhongan.icare.message.im.service.ImSigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by za-raozhikun on 2017/8/14.
 */
@Slf4j
@Service
public class ImAdminServiceImpl implements ImAdminService {

    @Resource
    private ImSigService imSigService;

    @Resource
    private ImService imService;

    @Value("${za.icare.message.im.sdk.admin}")
    private String adminId;

    @Value("${za.icare.message.im.sdk.appId}")
    private long skdAppId;

    private volatile String sig;

    private String getAdminSig() {
        if (StringUtils.isEmpty(sig)) {
            sig = imSigService.generateImSig(adminId);
        }
        return sig;
    }

    @Override
    public JSONObject invoke(String path, Object requestJson) {
        return imService.invoke(path, getAdminSig(), adminId, skdAppId, requestJson);
    }

    @Scheduled(cron = "${icare.im.admin.sig.fresh.cron:0 0 0 * * ?}")
    public void freshAdminSig() {
        sig = imSigService.generateImSig(adminId);
        log.info("Im Admin Sig Refreshed");
    }

}
