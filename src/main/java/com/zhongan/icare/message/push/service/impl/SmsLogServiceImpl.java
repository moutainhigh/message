package com.zhongan.icare.message.push.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.utils.BeanUtils;
import com.zhongan.health.common.utils.DateUtils;
import com.zhongan.icare.common.dao.mybatis.BatchSqlSessionTemplate;
import com.zhongan.icare.message.push.dao.SmsLogDAO;
import com.zhongan.icare.message.push.dao.dataobject.SmsLogDO;
import com.zhongan.icare.message.push.dao.dataobject.SmsLogPageDO;
import com.zhongan.icare.share.message.dto.SmsLogDTO;
import com.zhongan.icare.share.message.dto.SmsLogQueryDTO;
import com.zhongan.icare.share.message.service.ISmsLogService;

@Service
@RestController
class SmsLogServiceImpl implements ISmsLogService {
    @Resource
    SmsLogDAO                       dao;
    @Resource
    private BatchSqlSessionTemplate batchSqlSessionTemplate;

    public long insert(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null, "dto不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        Long id = dataobject.getId();
        if (id == null) {
            id = SequenceFactory.nextId(SmsLogDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insert(dataobject);
        return id;
    }

    public long insertSelective(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null, "dto不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        Long id = dataobject.getId();
        if (id == null) {
            id = SequenceFactory.nextId(SmsLogDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insertSelective(dataobject);
        return id;
    }

    @Override
    public Boolean insertBatch(@RequestBody List<SmsLogDTO> lists) {

        List<SmsLogDO> list = BeanUtils.simpleDOAndBOConvert(lists, SmsLogDO.class, null);
        if (CollectionUtils.isNotEmpty(list)) {
            for (SmsLogDO domain : list) {
                Long id = domain.getId();
                if (id == null) {
                    id = SequenceFactory.nextId(SmsLogDO.class);
                }
                domain.setId(id);
                domain.setIsDeleted(YesOrNo.NO.getValue());
                CommonFieldUtils.populate(domain, true);
            }
            int count = batchSqlSessionTemplate.insert("com.zhongan.icare.message.push.dao.SmsLogDAO.insert", list);
            if (count == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int updateIsDeletedById(@RequestParam String modifier, @RequestParam List<Long> ids) {
        Preconditions.checkArgument(modifier != null && ids != null && ids.size() > 0, "参数不能为空");

        Date now = DateUtils.currentDate();
        List<SmsLogDO> detailList = Lists.newArrayList();
        for (Long id : ids) {
            SmsLogDO detailDO = new SmsLogDO();
            detailDO.setId(id);
            detailDO.setIsDeleted(YesOrNo.YES.getValue());
            detailDO.setModifier(modifier);
            detailDO.setGmtModified(now);
            detailList.add(detailDO);
        }
        return batchSqlSessionTemplate.update("com.zhongan.icare.message.push.dao.SmsLogDAO.updateIsDeletedById",
                detailList);
    }

    public int updateByPrimaryKey(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKey(dataobject);
        return cnt;
    }

    public int deleteByPrimaryKey(@RequestParam("id") long id) {
        Preconditions.checkArgument(id > 0, "Id必须大于0");
        int cnt = dao.deleteByPrimaryKey(id);
        return cnt;
    }

    @Override
    public int deleteBytemplateNoOrBatchId(@RequestBody SmsLogQueryDTO dto) {
        Preconditions.checkArgument(
                dto != null && (StringUtils.isNotEmpty(dto.getTemplateNo()) || dto.getBatchId() != null),
                "模板号或批次号不能都为空.");
        dto.setIsDeleted(YesOrNo.YES);
        SmsLogPageDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogPageDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateIsDeletedBytemplateNoOrBatchId(dataobject);
        return cnt;
    }

    @Override
    public int updateSendTypeBytemplateNoOrBatchId(@RequestBody SmsLogQueryDTO dto) {
        Preconditions
                .checkArgument(dto != null && (StringUtils.isNotEmpty(dto.getTemplateNo()) || dto.getBatchId() != null)
                        && dto.getSendType() != null, "(模板号或批次号不能都为空)且发送类型不能为空.");
        SmsLogPageDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogPageDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateSendTypeBytemplateNoOrBatchId(dataobject);
        return cnt;
    }

    public int updateByPrimaryKeySelective(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        return cnt;
    }

    public int updateFailExclusiveSuccess(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        return cnt;
    }

    public List<SmsLogDTO> selectByCond(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        List<SmsLogDO> dataobjects = dao.selectByCond(dataobject);
        return BeanUtils.simpleDOAndBOConvert(dataobjects, SmsLogDTO.class, null);
    }

    @Override
    public List<SmsLogDTO> selectNeedSendSmsLog(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        List<SmsLogDO> dataobjects = dao.selectNeedSendSmsLog(dataobject);
        return BeanUtils.simpleDOAndBOConvert(dataobjects, SmsLogDTO.class, null);
    }

    @Override
    public com.zhongan.icare.share.common.dto.PageDTO<SmsLogDTO> selectByCondPage(@RequestBody com.zhongan.icare.share.common.dto.PageDTO<SmsLogQueryDTO> condition) {
        com.zhongan.icare.share.common.dto.PageDTO<SmsLogDTO> pages = new com.zhongan.icare.share.common.dto.PageDTO<SmsLogDTO>();
        SmsLogPageDO pageDO = BeanUtils.simpleDOAndBOConvert(condition.getParam(), SmsLogPageDO.class);
        pageDO.setStartRow(condition.getStartRow());
        pageDO.setPageSize(condition.getPageSize());
        pageDO.setIsDeleted(YesOrNo.NO.getValue());
        List<SmsLogDTO> list = new ArrayList<SmsLogDTO>();
        list = BeanUtils.simpleDOAndBOConvert(dao.selectByCondPage(pageDO), SmsLogDTO.class, null);
        pages.setStartRow(condition.getStartRow());
        pages.setTotalItem(countByCondPage(condition.getParam()));
        pages.setStartRow(condition.getStartRow());
        pages.setPageSize(condition.getPageSize());
        pages.setResultList(list);
        return pages;
    }

    @Override
    public int countByCondPage(@RequestBody SmsLogQueryDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        SmsLogPageDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogPageDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCondPage(dataobject);
        return cnt;
    }

    public int countByCond(@RequestBody SmsLogDTO dto) {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        SmsLogDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, SmsLogDO.class);
        dataobject.setIsDeleted(YesOrNo.NO.getValue());
        int cnt = dao.countByCond(dataobject);
        return cnt;
    }

    public SmsLogDTO selectByPrimaryKey(@RequestParam("id") long id) {
        Preconditions.checkArgument(id > 0, "id必须大于0");
        SmsLogDO dataobject = dao.selectByPrimaryKey(id);
        return BeanUtils.simpleDOAndBOConvert(dataobject, SmsLogDTO.class);
    }
}
