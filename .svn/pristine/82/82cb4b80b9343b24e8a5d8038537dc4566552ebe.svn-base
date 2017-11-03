package com.zhongan.icare.message.push.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;

import com.zhongan.icare.message.push.dao.dataobject.MsgPushTagDO;

@Service
@FeignClient(name = "icare-message")
public interface IMsgPushCustTagService {
    public List<MsgPushTagDO> queryCustTag(Long custId);

    public void removeCustTagById(Long custId, Long tagId);

    public void addCustTag(Long custId, Long orgId);

}
