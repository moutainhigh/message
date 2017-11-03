package com.zhongan.icare.message.im.mq.handler;

import com.alibaba.fastjson.JSON;
import com.zhongan.icare.common.mq.handler.EqualMatchProcessor;
import com.zhongan.icare.message.im.bean.ImAccount;
import com.zhongan.icare.message.im.service.ImAccountService;
import com.zhongan.icare.message.im.service.ImOrgGroupService;
import com.zhongan.icare.share.customer.dto.CustCustomerIndividualDTO;
import com.zhongan.icare.share.customer.enm.CustEventType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * Created by za-raozhikun on 2017/8/22.
 */
@Slf4j
public class IndividualCreateProcessor extends EqualMatchProcessor<CustCustomerIndividualDTO> {

    @Resource
    private ImAccountService imAccountService;

    @Resource
    private ImOrgGroupService imOrgGroupService;

    @Override
    public String name() {
        return CustEventType.CUSTOMER_INDIVIDUAL_CREATED.getCode();
    }

    @Override
    public void process(CustCustomerIndividualDTO custCustomerIndividualDTO) {
        try {
            log.info("Im Customer Get Message : id={}, name={}", custCustomerIndividualDTO.getId(), custCustomerIndividualDTO.getName());
            String id = String.valueOf(custCustomerIndividualDTO.getCustId());
            String name = custCustomerIndividualDTO.getName();
            String icon = custCustomerIndividualDTO.getCustIcon();
            ImAccount imAccount = new ImAccount();
            imAccount.setId(id);
            imAccount.setNickName(name);
            imAccount.setFaceUrl(icon);
            imAccountService.addImAccount(imAccount);
            if (custCustomerIndividualDTO.getParentCustId() != null && custCustomerIndividualDTO.getParentCustId() != 0) {
                imOrgGroupService.addOrgImGroupMember(String.valueOf(custCustomerIndividualDTO.getParentCustId()), id, name);
            }
            log.info("Im Customer Create Message Processed : id={}", custCustomerIndividualDTO.getId());
        } catch (Exception e) {
            log.warn("Im Customer Create Message Processed Failed : cust={}, msg={}", JSON.toJSONString(custCustomerIndividualDTO), e.getMessage(), e);
            throw e;
        }
    }
}
