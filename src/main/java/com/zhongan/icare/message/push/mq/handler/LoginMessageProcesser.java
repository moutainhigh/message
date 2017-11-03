package com.zhongan.icare.message.push.mq.handler;

import com.zhongan.health.common.utils.StringUtils;
import com.zhongan.icare.common.mq.handler.MatchProcessor;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushRelDO;
import com.zhongan.icare.message.push.service.IMsgPushRelService;
import com.zhongan.icare.share.customer.dto.CustCustomerDTO;
import com.zhongan.icare.share.customer.dto.CustLoginAccountDTO;
import com.zhongan.icare.share.customer.dto.CustOptionDTO;
import com.zhongan.icare.share.customer.enm.CustEventType;
import com.zhongan.icare.share.customer.service.ICustomerQueryService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
public class LoginMessageProcesser implements MatchProcessor<CustLoginAccountDTO> {
    @Resource
    private IMsgPushRelService    msgPushRelService;
    @Resource
    private ICustomerQueryService customerQueryService;

    @Override
    public String name() {
        return CustEventType.CUSTOMER_LOGIN_SUCCESS.getCode();
    }

    @Override
    public void process(CustLoginAccountDTO account) {
        Long custId = account.getCustId();
        if (custId == null || custId.longValue() == 0l)
            return;
        CustOptionDTO option = new CustOptionDTO();
        option.setCustId(custId);
        CustCustomerDTO customer = customerQueryService.query(option);
        Long parentId = customer.getParentCustId();
        log.info("message push login cust info , [custId :{}, parentId : {}]", custId, parentId);
        if (parentId == null || parentId.longValue() == 0l)
            return;
        String channelId = account.getChannelId();
        Integer deviceType = account.getDeviceType();
        if (StringUtils.isNotBlank(channelId) && deviceType != null && deviceType != 0) {
            MsgPushRelDO relDO = new MsgPushRelDO();
            relDO.setChannelId(channelId);
            relDO.setCustId(account.getCustId());
            relDO.setDeviceType(deviceType);
            relDO.setOrgId(parentId);
            msgPushRelService.saveOrUpdateDevice(relDO);
        }
    }

    @Override
    public boolean match(String tag, String topic, String bizKey) {
        log.info("recieve kafka message tag : {}", tag);
        return CustEventType.CUSTOMER_LOGIN_SUCCESS.getCode().equals(tag);
    }

    @Override
    public void process(String tag, String bizKey, CustLoginAccountDTO account) {
        process(account);
    }

}
