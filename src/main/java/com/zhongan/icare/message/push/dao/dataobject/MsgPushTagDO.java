package com.zhongan.icare.message.push.dao.dataobject;

import com.zhongan.health.common.share.bean.BaseDataObject;

public class MsgPushTagDO extends BaseDataObject {
    private Long   orgId;

    private String tagName;

    private String tagIcon;

    /**
     * This method returns the value of the database column
     * cust_msg_push_tag.org_id
     * 
     * @return the value of cust_msg_push_tag.org_id
     * @mbggenerated
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column cust_msg_push_tag.org_id
     * 
     * @param orgId the value for cust_msg_push_tag.org_id
     * @mbggenerated
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    /**
     * This method returns the value of the database column
     * cust_msg_push_tag.tag_name
     * 
     * @return the value of cust_msg_push_tag.tag_name
     * @mbggenerated
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column cust_msg_push_tag.tag_name
     * 
     * @param tagName the value for cust_msg_push_tag.tag_name
     * @mbggenerated
     */
    public void setTagName(String tagName) {
        this.tagName = tagName == null ? null : tagName.trim();
    }

    public String getTagIcon() {
        return tagIcon;
    }

    public void setTagIcon(String tagIcon) {
        this.tagIcon = tagIcon;
    }

}
