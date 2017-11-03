package com.zhongan.icare.message.im.mq.handler;

import com.alibaba.fastjson.JSON;
import com.zhongan.icare.common.mq.handler.EqualMatchProcessor;
import com.zhongan.icare.message.im.bean.ImAccount;
import com.zhongan.icare.message.im.service.ImAccountService;
import com.zhongan.icare.message.im.service.ImOrgGroupService;
import com.zhongan.icare.share.customer.dto.CustCustomerOrgDTO;
import com.zhongan.icare.share.customer.enm.CustEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Created by za-raozhikun on 2017/8/22.
 */
@Slf4j
public class OrgCreateProcessor extends EqualMatchProcessor<CustCustomerOrgDTO> {

    @Resource
    private ImAccountService imAccountService;

    @Resource
    private ImOrgGroupService imOrgGroupService;

    @Override
    public String name() {
        return CustEventType.CUSTOMER_ORG_CREATED.getCode();
    }

    @Override
    public void process(CustCustomerOrgDTO custCustomerOrgDTO) {
        try {
            log.info("Im Customer Org Get Message : id={}, name={}", custCustomerOrgDTO.getCustId(), custCustomerOrgDTO.getName());
            String id = String.valueOf(custCustomerOrgDTO.getCustId());
            String name = custCustomerOrgDTO.getName();
            String icon = custCustomerOrgDTO.getCustIcon();
            ImAccount imAccount = new ImAccount();
            imAccount.setId(id);
            imAccount.setNickName(name);
            imAccount.setFaceUrl(icon);
            imAccountService.addImAccount(imAccount);
            imOrgGroupService.createOrgImGroup(id, StringUtils.hasText(custCustomerOrgDTO.getNickName()) ? custCustomerOrgDTO.getNickName() : custCustomerOrgDTO.getName(), icon);
            log.info("Im Org Create Message Processed : id={}", custCustomerOrgDTO.getCustId());
        } catch (Exception e) {
            log.warn("Im Org Create Message Processed Failed : org={}, msg={}", JSON.toJSONString(custCustomerOrgDTO), e.getMessage(), e);
            throw e;
        }
    }
}
