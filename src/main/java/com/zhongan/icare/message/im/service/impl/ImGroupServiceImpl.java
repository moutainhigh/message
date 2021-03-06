package com.zhongan.icare.message.im.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.ImmutableMap;
import com.zhongan.icare.message.im.bean.ImGroup;
import com.zhongan.icare.message.im.bean.ImTextMessage;
import com.zhongan.icare.message.im.service.ImAdminService;
import com.zhongan.icare.message.im.service.ImGroupService;
import com.zhongan.icare.message.im.util.ImResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * IM群聊Service
 * Created by za-raozhikun on 2017/10/20.
 */
@Slf4j
@Service
public class ImGroupServiceImpl implements ImGroupService {

    private static final String ADD_GROUP_PATH = "group_open_http_svc/create_group";
    private static final String ADD_GROUP_MEMBER_PATH = "group_open_http_svc/add_group_member";
    private static final String DELETE_GROUP_MEMBER_PATH = "group_open_http_svc/delete_group_member";
    private static final String QUERY_ACCOUNT_GROUP_PATH = "group_open_http_svc/get_joined_group_list";
    private static final String SEND_GROUP_MESSAGE = "group_open_http_svc/send_group_msg";

    private static final int GROUP_MEMBER_OVERFLOW_CODE = 10014;

    @Resource
    private ImAdminService imAdminService;

    @Override
    public boolean addImGroup(ImGroup imGroup) {
        JSONObject ret = imAdminService.invoke(ADD_GROUP_PATH, imGroup);
        if (ImResultUtil.isSuccessful(ret)) {
            return true;
        } else if (ImResultUtil.getErrorCode(ret) == 10021) {
            return false;
        } else {
            log.warn("Im Add Group : id={}, name={}, ret={}", imGroup.getId(), imGroup.getName(), ret.toJSONString());
            throw ImResultUtil.throwException(ret);
        }
    }

    @Override
    public boolean addMember(String groupId, String... accountIds) {
        if (accountIds == null || accountIds.length == 0) {
            return true;
        }
        JSONObject addMemberRequest = new JSONObject();
        addMemberRequest.put("GroupId", groupId);
        JSONArray memberList = new JSONArray();
        addMemberRequest.put("MemberList", memberList);
        for (String accountId : accountIds) {
            if (StringUtils.hasText(accountId)) {
                memberList.add(ImmutableMap.of("Member_Account", accountId));
            }
        }
        if (memberList.isEmpty()) {
            return true;
        }
        JSONObject ret = imAdminService.invoke(ADD_GROUP_MEMBER_PATH, addMemberRequest);
        if (!ImResultUtil.isSuccessful(ret)) {
            if (ImResultUtil.getErrorCode(ret) == GROUP_MEMBER_OVERFLOW_CODE) {
                return false;
            } else {
                log.warn("Im Add Group Member: groupId={}, accountIds={}, ret={}", groupId, accountIds, ret.toJSONString());
                throw ImResultUtil.throwException(ret);
            }
        }
        return true;
    }

    @Override
    public void deleteMember(String groupId, String... accountIds) {
        if (accountIds == null || accountIds.length == 0) {
            return;
        }
        JSONObject deleteMemberRequest = new JSONObject();
        deleteMemberRequest.put("GroupId", groupId);
        deleteMemberRequest.put("MemberToDel_Account", accountIds);
        JSONObject ret = imAdminService.invoke(DELETE_GROUP_MEMBER_PATH, deleteMemberRequest);
        if (!ImResultUtil.isSuccessful(ret)) {
            log.warn("Im Delete Group Member: groupId={}, accountIds={}, ret={}", groupId, accountIds, ret.toJSONString());
            throw ImResultUtil.throwException(ret);
        }
    }

    @Override
    public List<String> queryJoinedGroup(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return new LinkedList<>();
        }
        JSONObject queryRequest = new JSONObject();
        queryRequest.put("Member_Account", accountId);
        JSONObject ret = imAdminService.invoke(QUERY_ACCOUNT_GROUP_PATH, queryRequest);
        if (!ImResultUtil.isSuccessful(ret)) {
            log.warn("Get Joined Group Failed: accountId={}, ret={}", accountId, ret.toJSONString());
            throw ImResultUtil.throwException(ret);
        } else {
            JSONArray jsonArray = ret.getJSONArray("GroupIdList");
            List<String> groupIds = new ArrayList<>(jsonArray.size());
            for (int i = 0; i < jsonArray.size(); i++) {
                groupIds.add(jsonArray.getJSONObject(i).getString("GroupId"));
            }
            return groupIds;
        }
    }

    @Override
    public void sendMessage(String groupId, String fromAccount, String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        JSONObject request = new JSONObject();
        request.put("GroupId", groupId);
        request.put("From_Account", fromAccount);
        request.put("Random", RandomUtils.nextInt(Integer.MAX_VALUE));
        request.put("MsgBody", Lists.newArrayList(new ImTextMessage(message)));
        JSONObject ret = imAdminService.invoke(SEND_GROUP_MESSAGE, request);
        if (!ImResultUtil.isSuccessful(ret)) {
            log.warn("Send Group Message Failed: groupId={}, message={}, ret={}", groupId, message, ret.toJSONString());
            throw ImResultUtil.throwException(ret);
        }
    }

    @Override
    public void sendMessage(String groupId, String message) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        JSONObject request = new JSONObject();
        request.put("GroupId", groupId);
        request.put("Random", RandomUtils.nextInt(Integer.MAX_VALUE));
        request.put("MsgBody", Lists.newArrayList(new ImTextMessage(message)));
        JSONObject ret = imAdminService.invoke(SEND_GROUP_MESSAGE, request);
        if (!ImResultUtil.isSuccessful(ret)) {
            log.warn("Send Group Message Failed: groupId={}, message={}, ret={}", groupId, message, ret.toJSONString());
            throw ImResultUtil.throwException(ret);
        }
    }

}
