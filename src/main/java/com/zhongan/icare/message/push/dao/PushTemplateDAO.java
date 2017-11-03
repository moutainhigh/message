package com.zhongan.icare.message.push.dao;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.PushTemplateDO;

import java.util.List;

public interface PushTemplateDAO extends ICommonDAO
{
    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    int insert(PushTemplateDO record);

    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    int insertSelective(PushTemplateDO record);

    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    @Override
    PushTemplateDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PushTemplateDO record);

    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PushTemplateDO record);

    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    List<PushTemplateDO> selectByCond(PushTemplateDO cond);

    /**
     * This method corresponds to the database table push_template
     *
     * @mbggenerated
     */
    int countByCond(PushTemplateDO cond);
}