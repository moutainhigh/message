package com.zhongan.icare.message.push.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;

import com.zhongan.icare.message.push.dao.dataobject.MsgPushRelDO;

@Service
@FeignClient(name = "icare-message")
public interface IMsgPushRelService {
    public void saveOrUpdateDevice(MsgPushRelDO custMsgPushRelDO);

    public MsgPushRelDO queryCustDeviceByCustId(Long custId);

    public List<MsgPushRelDO> queryCustDevices(MsgPushRelDO pushRelDO);
}
