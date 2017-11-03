package com.zhongan.icare.message.push.dao;

import java.util.List;

import com.zhongan.icare.common.dao.ICommonDAO;
import com.zhongan.icare.message.push.dao.dataobject.MailLogDO;
import com.zhongan.icare.message.push.dao.dataobject.MailLogPageDO;

public interface MailLogDAO extends ICommonDAO {
    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    int insert(MailLogDO record);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    int insertSelective(MailLogDO record);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    MailLogDO selectByPrimaryKey(Long id);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MailLogDO record);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(MailLogDO record);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MailLogDO record);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    List<MailLogDO> selectByCond(MailLogDO cond);

    /**
     * This method corresponds to the database table mail_log
     *
     * @mbggenerated
     */
    int countByCond(MailLogDO cond);

    List<MailLogDO> selectByCondPage(MailLogPageDO pageDO);

    /**
     * This method corresponds to the database table sms_log
     *
     * @mbggenerated
     */
    int countByCondPage(MailLogPageDO pageDO);

    int updateIsDeletedBytemplateNoOrBatchId(MailLogPageDO dataobject);

    int updateSendTypeBytemplateNoOrBatchId(MailLogPageDO record);

    List<MailLogDO> selectNeedSendMailLog(MailLogDO dataobject);
}
