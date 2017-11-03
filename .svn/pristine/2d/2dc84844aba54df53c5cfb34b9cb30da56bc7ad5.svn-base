package com.zhongan.icare.message.push.mq.handler;

import com.google.gson.Gson;
import com.zhongan.icare.common.mq.handler.MatchProcessor;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushTagDO;
import com.zhongan.icare.message.push.service.IMsgPushCustTagService;
import com.zhongan.icare.message.push.service.IMsgPushTagService;
import com.zhongan.icare.share.customer.dto.CustCustomerOrgDTO;
import com.zhongan.icare.share.customer.enm.CustEventType;
import com.zhongan.icare.share.customer.enm.CustTypeEnum;
import com.zhongan.icare.share.customer.service.ICustomerQueryService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
public class RegisterMessageProcesser implements MatchProcessor<CustCustomerOrgDTO> {
    @Resource
    private IMsgPushCustTagService msgPushCustTagService;
    @Resource
    private IMsgPushTagService     msgPushTagService;
    @Resource
    private ICustomerQueryService  customerQueryService;

    @Override
    public String name() {
        return CustEventType.CUSTOMER_ORG_CREATED.getCode();
    }

    @Override
    public void process(CustCustomerOrgDTO customer) {
        try {
            log.info("message push , add new tag by customer info : {}", new Gson().toJson(customer));
            if (customer == null || customer.getCustType() == null)
                return;
            if (customer.getCustType().getValue().intValue() == CustTypeEnum.COMPANY.getValue().intValue()) {
                MsgPushTagDO custMsgPushTagDO = new MsgPushTagDO();
                custMsgPushTagDO.setTagName(customer.getName());
                custMsgPushTagDO.setOrgId(customer.getCustId());
                msgPushTagService.addPushTag(custMsgPushTagDO);
            }

        } catch (Exception e) {
            log.error("", e);
        }

    }

    @Override
    public void process(String tag, String bizKey, CustCustomerOrgDTO customer) {
        process(customer);
    }

    @Override
    public boolean match(String tag, String topic, String bizKey) {
        log.info("recieve kafka message tag : {}", tag);
        return CustEventType.CUSTOMER_ORG_CREATED.getCode().equals(tag);
    }

}
