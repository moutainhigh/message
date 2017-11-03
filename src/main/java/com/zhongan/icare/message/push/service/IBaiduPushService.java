package com.zhongan.icare.message.push.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;

import com.zhongan.icare.message.push.dao.dataobject.MsgPushDO;

@Service
@FeignClient(name = "icare-message")
public interface IBaiduPushService {
    public boolean pushMsgToSingleDevice(String channelId, int deviceType, MsgPushDO msgPush);

    //    public void pushAllAndroidMsg(CustMsgPushDO msgPush);
    //
    //    public void pushAllIosMsg();

    public void pushMsgByTag(MsgPushDO msgPush, String tagName);

    public void pushMsgToAll(MsgPushDO msgPush);

    public int createBaiduTag(String tagName);

    public int deleteBaiduTag(String tagName);

    public void addDeviceToTag(String[] channelIds, String tagName, int deviceType);

    public void deleteDeviceFromTag(String[] channelId, String tagName, int deviceType);

}
