package com.zhongan.icare.message.push.dao;


import com.zhongan.icare.message.push.dao.dataobject.ImLogDO;

import java.util.List;

public interface ImLogDAO {
    /**
     * This method corresponds to the database table im_log
     *
     * @mbggenerated
     */
    int insert(ImLogDO record);

    /**
     * This method corresponds to the database table im_log
     *
     * @mbggenerated
     */
    int insertSelective(ImLogDO record);

    /**
     * This method corresponds to the database table im_log
     *
     * @mbggenerated
     */
    List<ImLogDO> selectByCond(ImLogDO cond);

    /**
     * This method corresponds to the database table im_log
     *
     * @mbggenerated
     */
    int countByCond(ImLogDO cond);
}