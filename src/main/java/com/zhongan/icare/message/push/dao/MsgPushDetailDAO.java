package com.zhongan.icare.message.push.dao;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushDetailDO;

import java.util.List;

public interface MsgPushDetailDAO extends ICommonDAO<MsgPushDetailDO>
{
    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    @Override
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    @Override
    int insert(MsgPushDetailDO record);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    @Override
    int insertSelective(MsgPushDetailDO record);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    @Override
    MsgPushDetailDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    @Override
    int updateByPrimaryKeySelective(MsgPushDetailDO record);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(MsgPushDetailDO record);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    @Override
    int updateByPrimaryKey(MsgPushDetailDO record);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    int countUnreadMsgs(MsgPushDetailDO record);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    List<MsgPushDetailDO> selectByPage(MsgPushDetailDO record);

    /**
     * This method corresponds to the database table cust_msg_push_detail
     *
     * @mbggenerated
     */
    int insertMsgDetailBatch(List<MsgPushDetailDO> list);
}
