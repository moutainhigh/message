package com.zhongan.icare.message.im.service;

/**
 * Created by za-raozhikun on 2017/10/20.
 */
public interface ImOrgGroupService {

    void createOrgImGroup(String orgId, String orgName, String faceUrl);

    void addOrgImGroupMember(String orgId, String custId, String name);

    void delOrgImGroupMember(String custId);

    void delOrgImGroupMember(String orgId, String custId);
}
