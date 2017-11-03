package com.zhongan.icare.message.im.mq.handler;

import com.alibaba.fastjson.JSON;
import com.zhongan.icare.common.mq.handler.EqualMatchProcessor;
import com.zhongan.icare.message.im.bean.ImAccount;
import com.zhongan.icare.message.im.service.ImAccountService;
import com.zhongan.icare.share.customer.dto.CustCustomerOrgDTO;
import com.zhongan.icare.share.customer.enm.CustEventType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * Created by za-raozhikun on 2017/8/22.
 */
@Slf4j
public class OrgUpdateProcessor extends EqualMatchProcessor<CustCustomerOrgDTO> {

    @Resource
    private ImAccountService imAccountService;

    @Override
    public String name() {
        return CustEventType.CUSTOMER_ORG_UPDATE.getCode();
    }

    @Override
    public void process(CustCustomerOrgDTO custCustomerOrgDTO) {
        try {
            log.info("Im Org Get Message : id={}, name={}", custCustomerOrgDTO.getId(), custCustomerOrgDTO.getName());
            long id = custCustomerOrgDTO.getCustId();
            String name = custCustomerOrgDTO.getName();
            String icon = custCustomerOrgDTO.getCustIcon();
            ImAccount imAccount = new ImAccount();
            imAccount.setId(String.valueOf(id));
            imAccount.setNickName(name);
            imAccount.setFaceUrl(icon);
            imAccountService.addImAccount(imAccount);
            log.info("Im Org Update Message Processed : id={}", custCustomerOrgDTO.getId());
        } catch (Exception e) {
            log.warn("Im Org Update Message Processed Failed : org={}, msg={}", JSON.toJSONString(custCustomerOrgDTO), e.getMessage(), e);
            throw e;
        }
    }
}