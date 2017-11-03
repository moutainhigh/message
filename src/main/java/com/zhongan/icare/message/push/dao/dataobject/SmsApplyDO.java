package com.zhongan.icare.message.push.dao.dataobject;

import com.zhongan.health.common.share.bean.BaseDataObject;

public class SmsApplyDO extends BaseDataObject {
    private static final long serialVersionUID = 1492595383437L;

    /**
     * 模板编号（满天星回写）
     *
     * @mbggenerated
     */
    private String            templateNo;

    /**
     * 短信内容
     *
     * @mbggenerated
     */
    private String            content;

    /**
     * 类型（0：生日）
     *
     * @mbggenerated
     */
    private Integer           smsType;
    /**
     * 类型
     *
     * @mbggenerated
     */
    private Integer           messageType;
    /**
     * 状态（0：待审批，1：审批通过，2：审批拒绝）
     *
     * @mbggenerated
     */
    private Integer           status;

    /**
     * 满天星审核状态（0：待审核，1：审核通过，2：审核拒绝）
     *
     * @mbggenerated
     */
    private Integer           starStatus;

    /**
     * 满天星入库状态（0：未入库，1：已入库）
     *
     * @mbggenerated
     */
    private Integer           starLibStatus;

    /**
     * 申请人
     *
     * @mbggenerated
     */
    private Long              proposerCustId;

    /**
     * 审批人
     *
     * @mbggenerated
     */
    private String            approve;

    /**
     * 备注
     *
     * @mbggenerated
     */
    private String            remark;

    /**
     * 审批拒绝的原因
     *
     * @mbggenerated
     */
    private String            refuseReason;

    /**
     * 所属公司
     *
     * @mbggenerated
     */
    private Long              orgCustId;

    /**
     * 是否营销.Y:是，N否
     *
     * @mbggenerated
     */
    private String            isMarket;
    private String            title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    /**
     * This method returns the value of the database column
     * sms_apply.template_no
     *
     * @return the value of sms_apply.template_no
     * @mbggenerated
     */
    public String getTemplateNo() {
        return templateNo;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.template_no
     *
     * @param templateNo the value for sms_apply.template_no
     * @mbggenerated
     */
    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo == null ? null : templateNo.trim();
    }

    /**
     * This method returns the value of the database column sms_apply.content
     *
     * @return the value of sms_apply.content
     * @mbggenerated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.content
     *
     * @param content the value for sms_apply.content
     * @mbggenerated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method returns the value of the database column sms_apply.sms_type
     *
     * @return the value of sms_apply.sms_type
     * @mbggenerated
     */
    public Integer getSmsType() {
        return smsType;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.sms_type
     *
     * @param smsType the value for sms_apply.sms_type
     * @mbggenerated
     */
    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    /**
     * This method returns the value of the database column sms_apply.status
     *
     * @return the value of sms_apply.status
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.status
     *
     * @param status the value for sms_apply.status
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method returns the value of the database column
     * sms_apply.star_status
     *
     * @return the value of sms_apply.star_status
     * @mbggenerated
     */
    public Integer getStarStatus() {
        return starStatus;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.star_status
     *
     * @param starStatus the value for sms_apply.star_status
     * @mbggenerated
     */
    public void setStarStatus(Integer starStatus) {
        this.starStatus = starStatus;
    }

    /**
     * This method returns the value of the database column
     * sms_apply.star_lib_status
     *
     * @return the value of sms_apply.star_lib_status
     * @mbggenerated
     */
    public Integer getStarLibStatus() {
        return starLibStatus;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.star_lib_status
     *
     * @param starLibStatus the value for sms_apply.star_lib_status
     * @mbggenerated
     */
    public void setStarLibStatus(Integer starLibStatus) {
        this.starLibStatus = starLibStatus;
    }

    /**
     * This method returns the value of the database column
     * sms_apply.proposer_cust_id
     *
     * @return the value of sms_apply.proposer_cust_id
     * @mbggenerated
     */
    public Long getProposerCustId() {
        return proposerCustId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.proposer_cust_id
     *
     * @param proposerCustId the value for sms_apply.proposer_cust_id
     * @mbggenerated
     */
    public void setProposerCustId(Long proposerCustId) {
        this.proposerCustId = proposerCustId;
    }

    /**
     * This method returns the value of the database column sms_apply.approve
     *
     * @return the value of sms_apply.approve
     * @mbggenerated
     */
    public String getApprove() {
        return approve;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.approve
     *
     * @param approve the value for sms_apply.approve
     * @mbggenerated
     */
    public void setApprove(String approve) {
        this.approve = approve == null ? null : approve.trim();
    }

    /**
     * This method returns the value of the database column sms_apply.remark
     *
     * @return the value of sms_apply.remark
     * @mbggenerated
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.remark
     *
     * @param remark the value for sms_apply.remark
     * @mbggenerated
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method returns the value of the database column
     * sms_apply.refuse_reason
     *
     * @return the value of sms_apply.refuse_reason
     * @mbggenerated
     */
    public String getRefuseReason() {
        return refuseReason;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.refuse_reason
     *
     * @param refuseReason the value for sms_apply.refuse_reason
     * @mbggenerated
     */
    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason == null ? null : refuseReason.trim();
    }

    /**
     * This method returns the value of the database column
     * sms_apply.org_cust_id
     *
     * @return the value of sms_apply.org_cust_id
     * @mbggenerated
     */
    public Long getOrgCustId() {
        return orgCustId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.org_cust_id
     *
     * @param orgCustId the value for sms_apply.org_cust_id
     * @mbggenerated
     */
    public void setOrgCustId(Long orgCustId) {
        this.orgCustId = orgCustId;
    }

    /**
     * This method returns the value of the database column sms_apply.is_market
     *
     * @return the value of sms_apply.is_market
     * @mbggenerated
     */
    public String getIsMarket() {
        return isMarket;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the
     * value of the database column sms_apply.is_market
     *
     * @param isMarket the value for sms_apply.is_market
     * @mbggenerated
     */
    public void setIsMarket(String isMarket) {
        this.isMarket = isMarket == null ? null : isMarket.trim();
    }
}