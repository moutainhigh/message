package com.zhongan.icare.message.push.dao.dataobject;

import com.zhongan.health.common.share.bean.BaseDataObject;
import com.zhongan.health.common.share.bean.PageDTO;

public class PushBaiduChannelDO extends BaseDataObject
{
    private static final long serialVersionUID = 1481255861443L;

    /**
     * 用户ID
     *
     * @mbggenerated
     */
    private Long customerId;

    /**
     * 百度推广渠道ID
     *
     * @mbggenerated
     */
    private String channelId;

    /**
     * 设备类型（3：安卓，4：IOS）
     *
     * @mbggenerated
     */
    private Integer deviceType;


    /**
     * 分页信息
     */
    private PageDTO page;

    public PageDTO getPage()
    {
        return page;
    }

    public void setPage(PageDTO page)
    {
        this.page = page;
    }

    /**
     * This method returns the value of the database column push_baidu_channel.customer_id
     *
     * @return the value of push_baidu_channel.customer_id
     * @mbggenerated
     */
    public Long getCustomerId()
    {
        return customerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_baidu_channel.customer_id
     *
     * @param customerId the value for push_baidu_channel.customer_id
     * @mbggenerated
     */
    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    /**
     * This method returns the value of the database column push_baidu_channel.channel_id
     *
     * @return the value of push_baidu_channel.channel_id
     * @mbggenerated
     */
    public String getChannelId()
    {
        return channelId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_baidu_channel.channel_id
     *
     * @param channelId the value for push_baidu_channel.channel_id
     * @mbggenerated
     */
    public void setChannelId(String channelId)
    {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    /**
     * This method returns the value of the database column push_baidu_channel.device_type
     *
     * @return the value of push_baidu_channel.device_type
     * @mbggenerated
     */
    public Integer getDeviceType()
    {
        return deviceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_baidu_channel.device_type
     *
     * @param deviceType the value for push_baidu_channel.device_type
     * @mbggenerated
     */
    public void setDeviceType(Integer deviceType)
    {
        this.deviceType = deviceType;
    }
}