package com.zhongan.icare.message.push.dao;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.PushRequestGroupDO;
import com.zhongan.icare.message.push.dao.dataobject.PushRequestLogDO;

import java.util.List;

public interface PushRequestLogDAO extends ICommonDAO
{
    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    int insert(PushRequestLogDO record);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    int insertSelective(PushRequestLogDO record);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    @Override
    PushRequestLogDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PushRequestLogDO record);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    int updateBySelective(PushRequestLogDO record);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PushRequestLogDO record);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    List<PushRequestLogDO> selectByCond(PushRequestLogDO cond);

    /**
     * This method corresponds to the database table push_request_log
     *
     * @mbggenerated
     */
    int countByCond(PushRequestLogDO cond);

    /**
     * 查询会员每个分组中的最新的一条消息，以及对应的分组信息
     *
     * @param customerId
     * @return
     */
    List<PushRequestGroupDO> selectRequestGroupByCus(Long customerId);
}