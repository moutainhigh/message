package com.zhongan.icare.message.push.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;

import com.zhongan.icare.message.push.dao.dataobject.MsgPushDetailDO;

@Service
@FeignClient(name = "icare-message")
public interface IMsgPushDetailService {
    public void addMsgPushDetail(MsgPushDetailDO custMsgPushDetailDO);

    public void addMsgPushDetails(List<MsgPushDetailDO> list);

    public MsgPushDetailDO queryMsgPushDetail(Long id);

    public int countUnreadMsg(Long custId);

    public List<MsgPushDetailDO> queryMsgs(MsgPushDetailDO custMsgPushDetailDO);

    public void updateMsg(MsgPushDetailDO custMsgPushDetailDO);

}
