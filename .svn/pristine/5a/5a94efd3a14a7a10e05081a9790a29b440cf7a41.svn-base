package com.zhongan.icare.message.push.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.icare.message.push.dao.MsgPushDetailDAO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushDetailDO;
import com.zhongan.icare.message.push.enums.IsDeleteEnum;
import com.zhongan.icare.message.push.enums.MsgStatusEnum;
import com.zhongan.icare.message.push.service.IMsgPushDetailService;

@Service
@Slf4j
@RestController
@RequestMapping(value = "/testMsg")
public class MsgPushDetailServiceImpl implements IMsgPushDetailService {
    @Resource
    private MsgPushDetailDAO custMsgPushDetailDAO;

    /**
     * 保存消息
     */
    @RequestMapping(value = "/addMsg", method = { RequestMethod.GET, RequestMethod.POST })
    @Override
    public void addMsgPushDetail(@RequestBody MsgPushDetailDO custMsgPushDetailDO) {
        try {
            createMsgDetailDO(custMsgPushDetailDO);
            custMsgPushDetailDAO.insert(custMsgPushDetailDO);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    /**
     * 批量保存数据
     */
    @RequestMapping(value = "/batchAddMsg", method = { RequestMethod.GET, RequestMethod.POST })
    @Override
    public void addMsgPushDetails(List<MsgPushDetailDO> list) {
        if (list != null) {
            List<MsgPushDetailDO> temp = new ArrayList<MsgPushDetailDO>();
            for (int i = 0; i < list.size(); i++) {
                MsgPushDetailDO _do = list.get(i);
                createMsgDetailDO(_do);
                temp.add(_do);
                if (i >= 100 || i == list.size() - 1) {
                    custMsgPushDetailDAO.insertMsgDetailBatch(temp);
                    temp.clear();
                }
            }
        }

    }

    private void createMsgDetailDO(MsgPushDetailDO custMsgPushDetailDO) {
        Long id = SequenceFactory.nextId(custMsgPushDetailDO.getClass());
        custMsgPushDetailDO.setId(id);
        custMsgPushDetailDO.setIsDeleted(IsDeleteEnum.NO.getValue());
        custMsgPushDetailDO.setCreator("system");
        custMsgPushDetailDO.setModifier("system");
        custMsgPushDetailDO.setGmtCreated(new Date());
        custMsgPushDetailDO.setGmtModified(new Date());
    }

    /**
     * 查询消息详情
     */
    @RequestMapping(value = "/queryMsgById")
    @Override
    public MsgPushDetailDO queryMsgPushDetail(@RequestParam Long id) {
        try {
            return custMsgPushDetailDAO.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 查询指定账户下的未读消息
     */
    @RequestMapping(value = "/countUnreadMsg")
    @Override
    public int countUnreadMsg(@RequestParam Long custId) {
        try {
            MsgPushDetailDO _do = new MsgPushDetailDO();
            _do.setCustId(custId);
            _do.setMessageStatus(MsgStatusEnum.UNREAD.getCode());
            _do.setIsDeleted(IsDeleteEnum.NO.getValue());
            return custMsgPushDetailDAO.countUnreadMsgs(_do);
        } catch (Exception e) {
            log.error("", e);
        }
        return 0;
    }

    /**
     * 分页查询账户下的消息
     */
    @RequestMapping(value = "/queryByPages", method = { RequestMethod.POST })
    @Override
    public List<MsgPushDetailDO> queryMsgs(@RequestBody MsgPushDetailDO msgPushDetailDO) {
        try {
            msgPushDetailDO.setIsDeleted(IsDeleteEnum.NO.getValue());
            return custMsgPushDetailDAO.selectByPage(msgPushDetailDO);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 更新消息
     */
    @RequestMapping(value = "/updateMsg", method = { RequestMethod.POST })
    @Override
    public void updateMsg(@RequestBody MsgPushDetailDO custMsgPushDetailDO) {
        try {
            custMsgPushDetailDO.setGmtModified(new Date());
            custMsgPushDetailDAO.updateByPrimaryKeySelective(custMsgPushDetailDO);
        } catch (Exception e) {
            log.error("", e);
        }

    }

}
