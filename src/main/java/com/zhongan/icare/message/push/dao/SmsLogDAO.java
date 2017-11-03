package com.zhongan.icare.message.push.dao;

import java.util.List;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.SmsLogDO;
import com.zhongan.icare.message.push.dao.dataobject.SmsLogPageDO;

public interface SmsLogDAO extends ICommonDAO {
    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    int insert(SmsLogDO record);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    int insertSelective(SmsLogDO record);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    SmsLogDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(SmsLogDO record);

    int updateIsDeletedBytemplateNoOrBatchId(SmsLogDO record);

    int updateSendTypeBytemplateNoOrBatchId(SmsLogDO record);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(SmsLogDO record);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    List<SmsLogDO> selectByCond(SmsLogDO cond);

    List<SmsLogDO> selectNeedSendSmsLog(SmsLogDO cond);

    List<SmsLogDO> selectByCondPage(SmsLogPageDO queryCondition);

    int countByCond(SmsLogDO queryCondition);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    int countByCondPage(SmsLogPageDO queryCondition);
}
