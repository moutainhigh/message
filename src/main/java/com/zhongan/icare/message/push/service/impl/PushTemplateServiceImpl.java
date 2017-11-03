package com.zhongan.icare.message.push.service.impl;

import com.google.common.base.Preconditions;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.utils.BeanUtils;
import com.zhongan.icare.common.cache.redis.client.RedisUtils;
import com.zhongan.icare.message.push.constants.ConstantsDataKey;
import com.zhongan.icare.message.push.dao.PushTemplateDAO;
import com.zhongan.icare.message.push.dao.dataobject.PushTemplateDO;
import com.zhongan.icare.share.message.dto.PushTemplateDTO;
import com.zhongan.icare.share.message.service.IPushTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@RestController
@Slf4j
public class PushTemplateServiceImpl implements IPushTemplateService
{
    @Resource
    PushTemplateDAO dao;

    @Value("${za.icare.redis.templateCodeExpire}")
    private String templateCodeExpire;

    @Value("${za.icare.redis.templateAllExpire}")
    private int templateAllExpire;

    String cacheKey(long id)
    {
        return "PushTemplate." + id;
    }

    @Override
    public long insert(@RequestBody PushTemplateDTO dto)
    {
        Preconditions.checkArgument(dto != null, "dto不能为空.");
        Preconditions.checkArgument(dto.getCode() != null, "dto.code不能为空.");
        PushTemplateDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushTemplateDO.class);
        Long id = dataobject.getId();
        if (id == null)
        {
            id = SequenceFactory.nextId(PushTemplateDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insert(dataobject);
        String key = ConstantsDataKey.REDIS_TEMPLATE_CODE_PRE + dataobject.getCode();
        RedisUtils.putObject(key, dataobject, Integer.parseInt(templateCodeExpire));
        return id;
    }

    @Override
    public long insertSelective(@RequestBody PushTemplateDTO dto)
    {
        Preconditions.checkArgument(dto != null, "dto不能为空.");
        Preconditions.checkArgument(dto.getCode() != null, "dto.code不能为空.");
        PushTemplateDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushTemplateDO.class);
        Long id = dataobject.getId();
        if (id == null)
        {
            id = SequenceFactory.nextId(PushTemplateDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insertSelective(dataobject);
        String key = ConstantsDataKey.REDIS_TEMPLATE_CODE_PRE + dataobject.getCode();
        RedisUtils.putObject(key, dataobject, Integer.parseInt(templateCodeExpire));
        return id;
    }

    @Override
    public int updateByPrimaryKey(@RequestBody PushTemplateDTO dto)
    {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        PushTemplateDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushTemplateDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKey(dataobject);
        removeRedis(dto.getId());
        return cnt;
    }

    private void removeRedis(Long id)
    {
        // 首先查询出来
        PushTemplateDTO pushTemplateDTO = selectByPrimaryKey(id);
        Preconditions.checkArgument(pushTemplateDTO != null, "模板不存在.");
        String key = ConstantsDataKey.REDIS_TEMPLATE_CODE_PRE + pushTemplateDTO.getCode();
        RedisUtils.remove(key);
    }

    @Override
    public int deleteByPrimaryKey(@RequestParam("id") long id)
    {
        Preconditions.checkArgument(id > 0, "Id必须大于0");
        PushTemplateDTO pushTemplateDTO = selectByPrimaryKey(id);
        String key = ConstantsDataKey.REDIS_TEMPLATE_CODE_PRE + pushTemplateDTO.getCode();
        RedisUtils.removeExceptionOk(key);
        int cnt = dao.deleteByPrimaryKey(id);
        removeRedis(id);
        return cnt;
    }

    @Override
    public int updateByPrimaryKeySelective(@RequestBody PushTemplateDTO dto)
    {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        PushTemplateDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushTemplateDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        removeRedis(dto.getId());
        return cnt;
    }

    @Override
    public List<PushTemplateDTO> selectByCond(@RequestBody PushTemplateDTO dto)
    {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        PushTemplateDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushTemplateDO.class);
        List<PushTemplateDO> dataobjects = dao.selectByCond(dataobject);
        return BeanUtils.simpleDOAndBOConvert(dataobjects, PushTemplateDTO.class, null);
    }

    @Override
    public int countByCond(@RequestBody PushTemplateDTO dto)
    {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        PushTemplateDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushTemplateDO.class);
        int cnt = dao.countByCond(dataobject);
        return cnt;
    }

    @Override
    public PushTemplateDTO selectByPrimaryKey(@RequestParam("id") long id)
    {
        Preconditions.checkArgument(id > 0, "id必须大于0");
        String cacheKey = cacheKey(id);
        PushTemplateDO dataobject = RedisUtils.getObjectExceptionNull(cacheKey, PushTemplateDO.class);
        if (dataobject == null)
        {
            dataobject = dao.selectByPrimaryKey(id);
            if (dataobject != null)
            {
                RedisUtils.putExceptionOk(cacheKey, dataobject, 86400);
            }
        }

        return BeanUtils.simpleDOAndBOConvert(dataobject, PushTemplateDTO.class);
    }

    @Override
    public List<PushTemplateDTO> selectAllTemplate(@RequestBody PushTemplateDTO pushTemplateDTO)
    {
        String key = ConstantsDataKey.REDIS_TEMPLATE_All;
        List<PushTemplateDTO> redisList = RedisUtils.getListExceptionAsNull(key, PushTemplateDTO.class);
        if (redisList != null && !redisList.isEmpty())
        {
            return redisList;
        }
        return updateAllRedisInfo(null);
    }

    @Override
    public List<PushTemplateDTO> updateAllRedisInfo(@RequestBody PushTemplateDTO pushTemplateDTO)
    {
        String key = ConstantsDataKey.REDIS_TEMPLATE_All;
        PushTemplateDTO templateDTO = new PushTemplateDTO();
        List<PushTemplateDTO> pushTemplateDTOS = selectByCond(templateDTO);
        if (pushTemplateDTOS == null)
        {
            log.error("template is not exist. ");
            return Collections.emptyList();
        }
        RedisUtils.putListExceptionOk(key, pushTemplateDTOS, templateAllExpire);
        return pushTemplateDTOS;
    }
}
