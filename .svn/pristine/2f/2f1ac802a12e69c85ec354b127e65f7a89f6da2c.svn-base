package com.zhongan.icare.message.push.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.utils.BeanUtils;
import com.zhongan.icare.common.cache.redis.client.RedisUtils;
import com.zhongan.icare.common.dao.mybatis.BatchSqlSessionTemplate;
import com.zhongan.icare.message.push.dao.MailLogDAO;
import com.zhongan.icare.message.push.dao.dataobject.MailLogDO;
import com.zhongan.icare.message.push.dao.dataobject.MailLogPageDO;
import com.zhongan.icare.share.message.dto.MailLogDTO;
import com.zhongan.icare.share.message.dto.MailLogQueryDTO;
import com.zhongan.icare.share.message.service.IMailLogService;

@Service
@RestController
class MailLogServiceImpl implements IMailLogService {
    @Resource
    MailLogDAO                      dao;
    @Resource
    private BatchSqlSessionTemplate batchSqlSessionTemplate;

    String cacheKey(MailLogDO dataobject) {
        return "MailLog." + dataobject.getId();
    }

    String cacheKey(long id) {
        return "MailLog." + id;
    }

    public long create(@RequestBody MailLogDTO dto) {
        Preconditions.checkArgument(dto != null, "dto不能为空.");
        MailLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogDO.class);
        Long id = dataobject.getId();
        if (id == null) {
            id = SequenceFactory.nextId(MailLogDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insert(dataobject);
        return id;
    }

    @Override
    public Boolean createBatch(@RequestBody List<MailLogDTO> lists) {

        List<MailLogDO> list = BeanUtils.simpleDOAndBOConvert(lists, MailLogDO.class, null);
        if (CollectionUtils.isNotEmpty(list)) {
            for (MailLogDO domain : list) {
                Long id = domain.getId();
                if (id == null) {
                    id = SequenceFactory.nextId(MailLogDO.class);
                }
                domain.setId(id);
                domain.setIsDeleted(YesOrNo.NO.getValue());
                CommonFieldUtils.populate(domain, true);
            }
            int count = batchSqlSessionTemplate.insert("com.zhongan.icare.message.push.dao.MailLogDAO.insert", list);
            if (count == 0) {
                return false;
            }
        }
        return true;
    }

    public int delete(@RequestParam("id") long id) {
        Preconditions.checkArgument(id > 0, "Id必须大于0");
        int cnt = dao.deleteByPrimaryKey(id);
        if (cnt > 0) {
            RedisUtils.removeExceptionOk(cacheKey(id));
        }
        return cnt;
    }

    @Override
    public int deleteBytemplateNoOrBatchId(@RequestBody MailLogQueryDTO dto) {
        Preconditions.checkArgument(
                dto != null && (StringUtils.isNotEmpty(dto.getTemplateNo()) || dto.getBatchId() != null),
                "模板号或批次号不能都为空.");
        dto.setIsDeleted(YesOrNo.YES);
        MailLogPageDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogPageDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateIsDeletedBytemplateNoOrBatchId(dataobject);
        return cnt;
    }

    @Override
    public int updateSendTypeBytemplateNoOrBatchId(@RequestBody MailLogQueryDTO dto) {
        Preconditions
                .checkArgument(dto != null && (StringUtils.isNotEmpty(dto.getTemplateNo()) || dto.getBatchId() != null)
                        && dto.getSendType() != null, "(模板号或批次号不能都为空)且发送类型不能为空.");
        MailLogPageDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogPageDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateSendTypeBytemplateNoOrBatchId(dataobject);
        return cnt;
    }

    public int update(@RequestBody MailLogDTO dto) {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        MailLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        if (cnt > 0) {
            RedisUtils.removeExceptionOk(cacheKey(dataobject));
        }
        return cnt;
    }

    @Override
    public com.zhongan.icare.share.common.dto.PageDTO<MailLogDTO> selectByCondPage(@RequestBody com.zhongan.icare.share.common.dto.PageDTO<MailLogQueryDTO> condition) {
        com.zhongan.icare.share.common.dto.PageDTO<MailLogDTO> pages = new com.zhongan.icare.share.common.dto.PageDTO<MailLogDTO>();
        MailLogPageDO pageDO = BeanUtils.simpleDOAndBOConvert(condition.getParam(), MailLogPageDO.class);
        pageDO.setStartRow(condition.getStartRow());
        pageDO.setPageSize(condition.getPageSize());
        pageDO.setIsDeleted(YesOrNo.NO.getValue());
        List<MailLogDTO> list = new ArrayList<MailLogDTO>();
        list = BeanUtils.simpleDOAndBOConvert(dao.selectByCondPage(pageDO), MailLogDTO.class, null);
        pages.setStartRow(condition.getStartRow());
        pages.setTotalItem(countByCondPage(condition.getParam()));
        pages.setStartRow(condition.getStartRow());
        pages.setPageSize(condition.getPageSize());
        pages.setResultList(list);
        return pages;
    }

    public List<MailLogDTO> list(@RequestBody MailLogDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        MailLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        List<MailLogDO> dataobjects = dao.selectByCond(dataobject);
        return BeanUtils.simpleDOAndBOConvert(dataobjects, MailLogDTO.class, null);
    }

    @Override
    public int count(@RequestBody MailLogDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        MailLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCond(dataobject);
        return cnt;
    }

    public int countByCondPage(@RequestBody MailLogQueryDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        MailLogPageDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogPageDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCondPage(dataobject);
        return cnt;
    }

    public MailLogDTO get(@RequestParam("id") long id) {
        Preconditions.checkArgument(id > 0, "id必须大于0");
        String cacheKey = cacheKey(id);
        MailLogDO dataobject = RedisUtils.getObjectExceptionNull(cacheKey, MailLogDO.class);
        if (dataobject == null) {
            dataobject = dao.selectByPrimaryKey(id);
            if (dataobject != null) {
                {
                    RedisUtils.putExceptionOk(cacheKey, dataobject, 86400);
                }
            }
        }
        return BeanUtils.simpleDOAndBOConvert(dataobject, MailLogDTO.class);
    }

    @Override
    public List<MailLogDTO> selectNeedSendMailLog(@RequestBody MailLogDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        MailLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, MailLogDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        List<MailLogDO> dataobjects = dao.selectNeedSendMailLog(dataobject);
        return BeanUtils.simpleDOAndBOConvert(dataobjects, MailLogDTO.class, null);
    }
}
