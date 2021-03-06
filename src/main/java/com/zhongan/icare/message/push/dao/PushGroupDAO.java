package com.zhongan.icare.message.push.dao;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.PushGroupDO;

import java.util.List;

public interface PushGroupDAO extends ICommonDAO
{
    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    int insert(PushGroupDO record);

    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    int insertSelective(PushGroupDO record);

    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    @Override
    PushGroupDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PushGroupDO record);

    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PushGroupDO record);

    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    List<PushGroupDO> selectByCond(PushGroupDO cond);

    /**
     * This method corresponds to the database table push_group
     *
     * @mbggenerated
     */
    int countByCond(PushGroupDO cond);
}