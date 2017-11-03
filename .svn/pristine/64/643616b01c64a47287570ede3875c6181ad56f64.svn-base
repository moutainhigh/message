package com.zhongan.icare.message.push.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.icare.message.push.dao.MsgPushDAO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushDO;
import com.zhongan.icare.message.push.enums.IsDeleteEnum;
import com.zhongan.icare.message.push.service.IMsgPushService;

@Service
@Slf4j
@RequestMapping(value = "/testPush")
public class MsgPushServiceImpl implements IMsgPushService {
    @Resource
    private MsgPushDAO msgPushDAO;

    @RequestMapping(value = "/addPush", method = RequestMethod.POST)
    @Override
    public void addMsgPush(@RequestBody MsgPushDO MsgPushDO) {
        try {
            Long id = SequenceFactory.nextId(MsgPushDO.getClass());
            MsgPushDO.setId(id);
            MsgPushDO.setCreator("system");
            MsgPushDO.setGmtCreated(new Date());
            MsgPushDO.setModifier("system");
            MsgPushDO.setGmtModified(new Date());
            MsgPushDO.setIsDeleted(IsDeleteEnum.NO.getValue());
            msgPushDAO.insert(MsgPushDO);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @RequestMapping(value = "/queryMsgs")
    @Override
    public List<MsgPushDO> queryMsgPush(@RequestParam long custId, @RequestParam int start, @RequestParam int end) {
        List<MsgPushDO> list = null;
        try {
            MsgPushDO _do = new MsgPushDO();
            _do.setCustId(custId);
            _do.setStart(start);
            _do.setEnd(end);
            _do.setIsDeleted(IsDeleteEnum.NO.getValue());
            list = msgPushDAO.selectByPage(_do);
        } catch (Exception e) {
            log.error("", e);
        }
        return list;
    }

}
