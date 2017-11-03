package com.zhongan.icare.message.push.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;

import com.zhongan.icare.message.push.dao.dataobject.MsgPushTagDO;

@Service
@FeignClient(name = "icare-message")
public interface IMsgPushTagService {
    public boolean addPushTag(MsgPushTagDO custMsgPushTagDO);

    public boolean removePushTag(MsgPushTagDO custMsgPushTagDO);

    public List<MsgPushTagDO> queryPushTagsByOrgId(Long orgId);

    public void updatePushTageByOrgId(MsgPushTagDO msgPushTag);
}
