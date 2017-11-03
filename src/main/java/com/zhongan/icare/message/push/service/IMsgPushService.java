package com.zhongan.icare.message.push.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;

import com.zhongan.icare.message.push.dao.dataobject.MsgPushDO;

@Service
@FeignClient(name = "icare-message")
public interface IMsgPushService {
    public void addMsgPush(MsgPushDO custMsgPushDO);

    public List<MsgPushDO> queryMsgPush(long orgId, int start, int end);

}
