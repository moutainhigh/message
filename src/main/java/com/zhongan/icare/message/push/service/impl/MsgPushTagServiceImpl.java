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
import org.springframework.web.bind.annotation.RestController;

import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.icare.message.push.dao.MsgPushTagDAO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushTagDO;
import com.zhongan.icare.message.push.enums.IsDeleteEnum;
import com.zhongan.icare.message.push.service.IBaiduPushService;
import com.zhongan.icare.message.push.service.IMsgPushTagService;

@Service
@Slf4j
@RestController
@RequestMapping(value = "test/pushTag")
public class MsgPushTagServiceImpl implements IMsgPushTagService {
    @Resource
    private MsgPushTagDAO     custMsgPushTagDAO;
    @Resource
    private IBaiduPushService baiduPushService;

    /**
     * 向机构中添加标签
     */
    @RequestMapping(value = "/addTag", method = RequestMethod.POST)
    @Override
    public boolean addPushTag(@RequestBody final MsgPushTagDO custMsgPushTagDO) {
        try {
            Long id = SequenceFactory.nextId(custMsgPushTagDO.getClass());
            custMsgPushTagDO.setId(id);
            custMsgPushTagDO.setCreator("system");
            custMsgPushTagDO.setGmtCreated(new Date());
            custMsgPushTagDO.setGmtModified(new Date());
            custMsgPushTagDO.setModifier("system");
            custMsgPushTagDO.setIsDeleted(IsDeleteEnum.NO.getValue());
            custMsgPushTagDAO.insert(custMsgPushTagDO);
            baiduPushService.createBaiduTag(custMsgPushTagDO.getTagName());
            //            if (res == 1) {
            //                log.warn("create biadu push tag failed by tagName : {}", custMsgPushTagDO.getTagName());
            //
            //                return false;
            //            }
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }

    /**
     * 删除指定标签
     */
    @RequestMapping(value = "/removeTag", method = RequestMethod.POST)
    @Override
    public boolean removePushTag(@RequestBody MsgPushTagDO custMsgPushTagDO) {
        try {
            baiduPushService.deleteBaiduTag(custMsgPushTagDO.getTagName());

            custMsgPushTagDO.setIsDeleted(IsDeleteEnum.NO.getValue());
            List<MsgPushTagDO> list = custMsgPushTagDAO.selectByCond(custMsgPushTagDO);
            if (list != null && !list.isEmpty()) {
                MsgPushTagDO _do = list.get(0);
                _do.setIsDeleted(IsDeleteEnum.YES.getValue());
                custMsgPushTagDAO.updateByPrimaryKey(_do);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 查询指定机构下的推送标签信息
     */
    @RequestMapping(value = "/queryTags", method = RequestMethod.GET)
    @Override
    public List<MsgPushTagDO> queryPushTagsByOrgId(@RequestParam Long orgId) {
        MsgPushTagDO custMsgPushTagDO = new MsgPushTagDO();
        custMsgPushTagDO.setOrgId(orgId);
        custMsgPushTagDO.setIsDeleted(IsDeleteEnum.NO.getValue());
        return custMsgPushTagDAO.selectByCond(custMsgPushTagDO);

    }

    @Override
    public void updatePushTageByOrgId(MsgPushTagDO msgPushTag) {
        MsgPushTagDO _do = new MsgPushTagDO();
        _do.setOrgId(msgPushTag.getOrgId());
        _do.setIsDeleted(IsDeleteEnum.NO.getValue());
        List<MsgPushTagDO> list = custMsgPushTagDAO.selectByCond(msgPushTag);
        if (list != null) {
            for (MsgPushTagDO tag : list) {
                msgPushTag.setId(tag.getId());
                msgPushTag.setGmtModified(new Date());
                custMsgPushTagDAO.updateByPrimaryKeySelective(msgPushTag);
            }
        }
    }

}
