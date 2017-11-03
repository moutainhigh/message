package com.zhongan.icare.message.push.dao.dataobject;

import com.zhongan.health.common.share.bean.BaseDataObject;

public class PushGroupDO extends BaseDataObject
{
    private static final long serialVersionUID = 1481255861521L;

    /**
     * 组的编号
     *
     * @mbggenerated
     */
    private String groupCode;

    /**
     * 组名
     *
     * @mbggenerated
     */
    private String groupName;

    /**
     * 组的类型（0：普通，1：跳转）
     *
     * @mbggenerated
     */
    private Integer groupType;

    /**
     * 组跳转的URL
     *
     * @mbggenerated
     */
    private String groupUrl;

    /**
     * 组的状态（0：可用，1：不可用）
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * 组的图标
     *
     * @mbggenerated
     */
    private String ico;

    /**
     * 备注
     *
     * @mbggenerated
     */
    private String comment;


    /**
     * 属于
     */
    private Long belong;

    public Long getBelong()
    {
        return belong;
    }

    public void setBelong(Long belong)
    {
        this.belong = belong;
    }

    /**
     * This method returns the value of the database column push_group.group_code
     *
     * @return the value of push_group.group_code
     * @mbggenerated
     */
    public String getGroupCode()
    {
        return groupCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_group.group_code
     *
     * @param groupCode the value for push_group.group_code
     * @mbggenerated
     */
    public void setGroupCode(String groupCode)
    {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    /**
     * This method returns the value of the database column push_group.group_name
     *
     * @return the value of push_group.group_name
     * @mbggenerated
     */
    public String getGroupName()
    {
        return groupName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_group.group_name
     *
     * @param groupName the value for push_group.group_name
     * @mbggenerated
     */
    public void setGroupName(String groupName)
    {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * This method returns the value of the database column push_group.group_type
     *
     * @return the value of push_group.group_type
     * @mbggenerated
     */
    public Integer getGroupType()
    {
        return groupType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_group.group_type
     *
     * @param groupType the value for push_group.group_type
     * @mbggenerated
     */
    public void setGroupType(Integer groupType)
    {
        this.groupType = groupType;
    }

    /**
     * This method returns the value of the database column push_group.group_url
     *
     * @return the value of push_group.group_url
     * @mbggenerated
     */
    public String getGroupUrl()
    {
        return groupUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_group.group_url
     *
     * @param groupUrl the value for push_group.group_url
     * @mbggenerated
     */
    public void setGroupUrl(String groupUrl)
    {
        this.groupUrl = groupUrl == null ? null : groupUrl.trim();
    }

    /**
     * This method returns the value of the database column push_group.status
     *
     * @return the value of push_group.status
     * @mbggenerated
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_group.status
     *
     * @param status the value for push_group.status
     * @mbggenerated
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    /**
     * This method returns the value of the database column push_group.ico
     *
     * @return the value of push_group.ico
     * @mbggenerated
     */
    public String getIco()
    {
        return ico;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_group.ico
     *
     * @param ico the value for push_group.ico
     * @mbggenerated
     */
    public void setIco(String ico)
    {
        this.ico = ico == null ? null : ico.trim();
    }

    /**
     * This method returns the value of the database column push_group.comment
     *
     * @return the value of push_group.comment
     * @mbggenerated
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column push_group.comment
     *
     * @param comment the value for push_group.comment
     * @mbggenerated
     */
    public void setComment(String comment)
    {
        this.comment = comment == null ? null : comment.trim();
    }
}