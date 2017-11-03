package com.zhongan.icare.message.push.mq.handler;

import com.google.gson.Gson;
import com.zhongan.icare.common.cache.redis.client.RedisUtils;
import com.zhongan.icare.common.mq.handler.MatchProcessor;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushTagDO;
import com.zhongan.icare.message.push.service.IMsgPushTagService;
import com.zhongan.icare.share.customer.dto.CustCustomerDTO;
import com.zhongan.icare.share.customer.enm.CustEventType;
import com.zhongan.icare.share.customer.enm.CustTypeEnum;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class PushTagManagerProcessor implements MatchProcessor<CustCustomerDTO>
{

    @Resource
    private IMsgPushTagService msgPushTagService;
    private final static String CACHKEY = "PUSHTAG_";

    private final static int EXPIRE_TIME_IN_SECOND = 864000;

    @Override
    public void process(String tag, String bizKey, CustCustomerDTO cust)
    {
        process(cust);
    }

    @Override
    public String name()
    {
        return CustEventType.CUST_ICON_UPDATE.getCode();
    }

    @Override
    public void process(CustCustomerDTO customer)
    {
        log.info("reveive update push tag message : {}", new Gson().toJson(customer));
        try
        {
            if (customer.getCustType() == CustTypeEnum.COMPANY)
            {
                List<MsgPushTagDO> list = msgPushTagService.queryPushTagsByOrgId(customer.getId());
                MsgPushTagDO msgPushTagDO = new MsgPushTagDO();
                if (list != null && !list.isEmpty())
                {
                    msgPushTagDO = list.get(0);
                    msgPushTagDO.setTagName(customer.getName());
                    msgPushTagDO.setTagIcon(customer.getCustIcon());
                    msgPushTagService.updatePushTageByOrgId(msgPushTagDO);
                } else
                {
                    msgPushTagDO.setTagName(customer.getName());
                    msgPushTagDO.setTagIcon(customer.getCustIcon());
                    msgPushTagDO.setOrgId(customer.getId());
                    msgPushTagService.addPushTag(msgPushTagDO);
                }
                String redisKey = CACHKEY + customer.getId();
                RedisUtils.putExceptionOk(redisKey, msgPushTagDO, EXPIRE_TIME_IN_SECOND);
            }

        } catch (Exception e)
        {
            log.error("", e);
        }
    }

    @Override
    public boolean match(String tag, String topic, String bizKey)
    {
        return CustEventType.CUST_ICON_UPDATE.getCode().equals(tag);
    }

}
