package com.zhongan.icare.message.push.dao;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.PushBaiduChannelDO;

import java.util.List;

public interface PushBaiduChannelDAO extends ICommonDAO
{
    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    int insert(PushBaiduChannelDO record);

    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    int insertSelective(PushBaiduChannelDO record);

    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    @Override
    PushBaiduChannelDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PushBaiduChannelDO record);

    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PushBaiduChannelDO record);

    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    List<PushBaiduChannelDO> selectByCond(PushBaiduChannelDO cond);

    /**
     * This method corresponds to the database table push_baidu_channel
     *
     * @mbggenerated
     */
    int countByCond(PushBaiduChannelDO cond);
}