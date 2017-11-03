package com.zhongan.icare.message.im.service.impl;

import com.zhongan.icare.message.im.bean.ImGroup;
import com.zhongan.icare.message.im.service.ImAccountService;
import com.zhongan.icare.message.im.service.ImGroupService;
import com.zhongan.icare.message.im.service.ImOrgGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 企业群Service
 * Created by za-raozhikun on 2017/10/20.
 */
@Slf4j
@Service
public class ImOrgGroupServiceImpl implements ImOrgGroupService {

    private static final String ORG_GROUP_ID_PREFIX = "@ICARE#";

    @Value("${za.icare.message.im.group_name_postfix:全员群}")
    private String ORG_GROUP_NAME_POSTFIX;

    @Value("${za.icare.message.im.group_name_postfix:大家好，我是}")
    private String ORG_GROUP_HELLO_POSTFIX;

    @Resource
    private ImGroupService imGroupService;

    @Resource
    private ImAccountService imAccountService;

    @Override
    public void createOrgImGroup(String orgId, String orgName, String faceUrl) {
        ImGroup imGroup = new ImGroup();
        imGroup.setId(ORG_GROUP_ID_PREFIX + orgId);
        imGroup.setName((orgName.length() > 7 ? orgName.substring(0, 7) : orgName) + ORG_GROUP_NAME_POSTFIX);
        imGroup.setFaceUrl(faceUrl);
        imGroupService.addImGroup(imGroup);
        imGroupService.sendMessage(imGroup.getId(), "群" + imGroup.getName() + "创建成功！");
    }

    @Override
    public void addOrgImGroupMember(String orgId, String custId, String name) {
        if (StringUtils.isEmpty(orgId) || StringUtils.isEmpty(custId)) {
            return;
        }
        String groupId = ORG_GROUP_ID_PREFIX + orgId;
        String accountId = imAccountService.encryptAccountId(custId);
        List joinedGroup = imGroupService.queryJoinedGroup(accountId);
        if (joinedGroup != null && joinedGroup.contains(groupId)) {
            return;
        }
        if (imGroupService.addMember(groupId, accountId) && StringUtils.hasText(name)) {
            imGroupService.sendMessage(groupId, accountId, ORG_GROUP_HELLO_POSTFIX + name);
        }
    }

    @Override
    public void delOrgImGroupMember(String custId) {
        if (StringUtils.isEmpty(custId)) {
            return;
        }
        String imAccountId = imAccountService.encryptAccountId(custId);
        List<String> joinedGroupIds = imGroupService.queryJoinedGroup(imAccountId);
        for (String joinedGroupId : joinedGroupIds) {
            if (joinedGroupId.startsWith(ORG_GROUP_ID_PREFIX)) {
                delOrgImGroupMember(joinedGroupId, custId);
            }
        }
    }

    @Override
    public void delOrgImGroupMember(String orgId, String custId) {
        imGroupService.deleteMember(orgId.startsWith(ORG_GROUP_ID_PREFIX) ? orgId : ORG_GROUP_ID_PREFIX + orgId, imAccountService.encryptAccountId(custId));
    }

}
