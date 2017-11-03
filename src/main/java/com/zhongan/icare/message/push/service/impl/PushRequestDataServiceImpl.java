package com.zhongan.icare.message.push.service.impl;

import com.google.common.base.Preconditions;
import com.zhongan.health.common.persistence.CommonFieldUtils;
import com.zhongan.health.common.persistence.SequenceFactory;
import com.zhongan.health.common.utils.BeanUtils;
import com.zhongan.icare.common.cache.redis.client.RedisUtils;
import com.zhongan.icare.message.push.dao.PushRequestDataDAO;
import com.zhongan.icare.message.push.dao.dataobject.PushRequestDataDO;
import com.zhongan.icare.share.message.dto.PushRequestDataDTO;
import com.zhongan.icare.share.message.service.IPushRequestDataService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Service
@RestController
class PushRequestDataServiceImpl implements IPushRequestDataService
{
    @Resource
    PushRequestDataDAO dao;

    String cacheKey(PushRequestDataDO dataobject)
    {
        return "PushRequestData." + dataobject.getId();
    }

    String cacheKey(long id)
    {
        return "PushRequestData." + id;
    }

    public long insert(@RequestBody PushRequestDataDTO dto)
    {
        Preconditions.checkArgument(dto != null, "dto不能为空.");
        PushRequestDataDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushRequestDataDO.class);
        Long id = dataobject.getId();
        if (id == null)
        {
            id = SequenceFactory.nextId(PushRequestDataDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insert(dataobject);
        return id;
    }

    public long insertSelective(@RequestBody PushRequestDataDTO dto)
    {
        Preconditions.checkArgument(dto != null, "dto不能为空.");
        PushRequestDataDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushRequestDataDO.class);
        Long id = dataobject.getId();
        if (id == null)
        {
            id = SequenceFactory.nextId(PushRequestDataDO.class);
            dataobject.setId(id);
        }
        CommonFieldUtils.populate(dataobject, true);
        dao.insertSelective(dataobject);
        return id;
    }

    public int updateByPrimaryKey(@RequestBody PushRequestDataDTO dto)
    {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        PushRequestDataDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushRequestDataDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKey(dataobject);
        if (cnt > 0)
        {
            RedisUtils.removeExceptionOk(cacheKey(dataobject));
        }
        return cnt;
    }

    public int deleteByPrimaryKey(@RequestParam("id") long id)
    {
        Preconditions.checkArgument(id > 0, "Id必须大于0");
        int cnt = dao.deleteByPrimaryKey(id);
        if (cnt > 0)
        {
            RedisUtils.removeExceptionOk(cacheKey(id));
        }
        return cnt;
    }

    public int updateByPrimaryKeySelective(@RequestBody PushRequestDataDTO dto)
    {
        Preconditions.checkArgument(dto != null && dto.getId() != null, "Id不能为空.");
        PushRequestDataDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushRequestDataDO.class);
        CommonFieldUtils.populate(dataobject, false);
        int cnt = dao.updateByPrimaryKeySelective(dataobject);
        if (cnt > 0)
        {
            RedisUtils.removeExceptionOk(cacheKey(dataobject));
        }
        return cnt;
    }

    public List<PushRequestDataDTO> selectByCond(@RequestBody PushRequestDataDTO dto)
    {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        PushRequestDataDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushRequestDataDO.class);
        List<PushRequestDataDO> dataobjects = dao.selectByCond(dataobject);
        return BeanUtils.simpleDOAndBOConvert(dataobjects, PushRequestDataDTO.class, null);
    }

    public int countByCond(@RequestBody PushRequestDataDTO dto)
    {
        Preconditions.checkArgument(dto != null, "查询条件不能为空.");
        PushRequestDataDO dataobject = BeanUtils.simpleDOAndBOConvert(dto, PushRequestDataDO.class);
        int cnt = dao.countByCond(dataobject);
        return cnt;
    }

    public PushRequestDataDTO selectByPrimaryKey(@RequestParam("id") long id)
    {
        Preconditions.checkArgument(id > 0, "id必须大于0");
        String cacheKey = cacheKey(id);
        PushRequestDataDO dataobject = RedisUtils.getObjectExceptionNull(cacheKey, PushRequestDataDO.class);
        if (dataobject == null)
        {
            dataobject = dao.selectByPrimaryKey(id);
            if (dataobject != null)
            {
                {
                    RedisUtils.putExceptionOk(cacheKey, dataobject, 86400);
                }
            }
        }
        return BeanUtils.simpleDOAndBOConvert(dataobject, PushRequestDataDTO.class);
    }
}