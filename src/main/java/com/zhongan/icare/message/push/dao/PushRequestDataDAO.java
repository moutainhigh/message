package com.zhongan.icare.message.push.dao;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.PushRequestDataDO;

import java.util.List;

public interface PushRequestDataDAO extends ICommonDAO
{
    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    int insert(PushRequestDataDO record);

    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    int insertSelective(PushRequestDataDO record);

    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    PushRequestDataDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PushRequestDataDO record);

    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PushRequestDataDO record);

    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    List<PushRequestDataDO> selectByCond(PushRequestDataDO cond);

    /**
     * This method corresponds to the database table push_request_data
     *
     * @mbggenerated
     */
    int countByCond(PushRequestDataDO cond);
}