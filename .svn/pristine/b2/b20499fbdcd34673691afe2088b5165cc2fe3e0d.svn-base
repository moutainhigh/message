package com.zhongan.icare.message.push.web;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.icare.common.client.ClientInfo;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushDO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushDetailDO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushRelDO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushTagDO;
import com.zhongan.icare.message.push.enums.IsDeleteEnum;
import com.zhongan.icare.message.push.enums.MsgStatusEnum;
import com.zhongan.icare.message.push.service.*;
import com.zhongan.icare.message.push.web.dto.SingleMsgPushDTO;
import com.zhongan.icare.share.common.constants.ErrorCode;
import com.zhongan.icare.share.customer.dto.CustCustomerDTO;
import com.zhongan.icare.share.customer.dto.CustOptionDTO;
import com.zhongan.icare.share.customer.enm.CustTypeEnum;
import com.zhongan.icare.share.customer.service.ICustomerQueryService;
import com.zhongan.icare.share.customer.service.ICustomerService;
import com.zhongan.icare.share.message.enm.MsgTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@RestController
@Slf4j
@RequestMapping(value = "/v1/pushMsg")
public class MsgPushController
{
    @Resource
    private IMsgPushDetailService msgPushDetailService;
    @Resource
    private IMsgPushService msgPushService;
    @Resource
    private IMsgPushRelService msgPushRelService;
    @Resource
    private IBaiduPushService baiduPushService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICustomerQueryService customerQueryService;
    @Resource
    private IMsgPushTagService msgPushTagService;

    @Autowired
    private ExecutorService threadPool;

    /**
     * 向个人推送消息
     *
     * @param singleMsg
     * @return
     */
    @RequestMapping(value = "/pushMsgToSingle", method = RequestMethod.POST)
    public BaseResult<Void> pushMsgToSingle(@RequestBody SingleMsgPushDTO singleMsg)
    {
        log.info("push mssage to cust : {}", JSON.toJSON(singleMsg));
        BaseResult<Void> result = new BaseResult<Void>();
        try
        {
            final MsgPushDO msgPush = new MsgPushDO();
            BeanUtils.copyProperties(singleMsg, msgPush);
            String targetMobile = singleMsg.getTargetMobile();
            ClientInfo client = ClientInfo.client();
            if (client == null)
            {
                log.warn("can't get client info");
                ExceptionUtils.setErrorInfo(result, ErrorCode.CUST_LOGIN_ACCT_EXPIRED);
                return result;
            }

            if (StringUtils.isBlank(targetMobile))
            {
                log.warn("can't find customer info by phone : {}", targetMobile);
                ExceptionUtils.setErrorInfo(result, ErrorCode.CUST_LOGIN_ACCT_NOT_EXISTS, targetMobile);
                return result;
            }
            Long custId = client.getCustId();

            CustCustomerDTO customer = new CustCustomerDTO();
            customer.setPhone(targetMobile);
            customer.setIsDeleted(YesOrNo.NO);
            List<CustCustomerDTO> list = customerService.queryCustomerByCond(customer);
            if (list == null || list.isEmpty())
            {
                log.warn("can't find customer info by phone : {}", targetMobile);
                result.setMessage("被推送人的手机号不能为空");
                result.setCode("3000009");
                return result;
            }
            Long targetCustId = list.get(0).getId();
            log.info("push msg to single customer , targetMobile: {},custId : {}", targetMobile, targetCustId);
            final MsgPushRelDO rel = msgPushRelService.queryCustDeviceByCustId(targetCustId);
            if (rel == null)
            {
                log.warn("can't find baidu push device info by custId : {}", targetCustId);
                ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_DEVICE_QUERY_FAILED);
                return result;
            }

            MsgPushTagDO tagDO = queryTagName(custId, result);
            if (tagDO == null)
            {
                return result;
            }
            CustCustomerDTO orgCust = customerService.queryCustomerByCustId(custId);
            //            if (orgCust.getParentCustId() != null) {
            //                ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_PUSH_AUTHOR_ERROR);
            //                return result;
            //            }
            if (orgCust.getParentCustId() != null && orgCust.getParentCustId() != 0L)
            {
                custId = orgCust.getParentCustId();
            }
            msgPush.setCustId(custId);
            msgPush.setMessageIcon(orgCust.getCustIcon());
            msgPush.setTagName(tagDO.getTagName());
            msgPush.setMessageType(MsgTypeEnum.SINGLE.getCode());
            savePushMsg(msgPush, targetCustId);
            baiduPushService.pushMsgToSingleDevice(rel.getChannelId(), rel.getDeviceType(), msgPush);

        } catch (Exception e)
        {
            log.error("", e);
        }
        return result;
    }

    @RequestMapping(value = "/pushMsgByTag", method = RequestMethod.POST)
    public BaseResult<Void> pushMsgByTag(@RequestBody final MsgPushDO msgPush)
    {
        BaseResult<Void> result = new BaseResult<Void>();
        try
        {
            ClientInfo client = ClientInfo.client();
            if (client == null)
            {
                log.warn("can't get client info");
                ExceptionUtils.setErrorInfo(result, ErrorCode.CUST_LOGIN_ACCT_EXPIRED);
                return result;
            }
            Long custId = client.getCustId();
            CustCustomerDTO customer = customerService.queryCustomerByCustId(custId);
            //只有企业账号才能推送消息
            if (customer.getParentCustId() != null)
            {
                ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_PUSH_AUTHOR_ERROR);
                return result;
            }
            MsgPushTagDO tagDO = queryTagName(custId, result);
            if (tagDO == null)
            {
                return result;
            }
            MsgPushRelDO relDO = new MsgPushRelDO();
            relDO.setOrgId(tagDO.getOrgId());
            relDO.setIsDeleted(IsDeleteEnum.NO.getValue());
            final List<MsgPushRelDO> list = msgPushRelService.queryCustDevices(relDO);
            if (list == null || list.isEmpty())
            {
                log.warn("no device info by orgId : {}", tagDO.getOrgId());
                ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_DEVICE_NOT_EXISTS);
                return result;
            }
            msgPush.setCustId(custId);
            msgPush.setMessageType(MsgTypeEnum.GROUP.getCode());
            msgPush.setTagName(tagDO.getTagName());
            msgPush.setMessageIcon(customer.getCustIcon());
            threadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    saveMsgByTag(list, msgPush);
                }

            });
            log.info("push msg by tagName : {}, push msg : {}", tagDO.getTagName(), new Gson().toJson(msgPush));
            baiduPushService.pushMsgByTag(msgPush, tagDO.getTagName());
        } catch (Exception e)
        {
            log.error("", e);
        }
        return result;
    }

    @RequestMapping(value = "/pushMsgToAll", method = RequestMethod.POST)
    public BaseResult<Void> pushMsgToAll(@RequestBody final MsgPushDO msgPush)
    {
        BaseResult<Void> result = new BaseResult<Void>();
        log.info("push msg to all : {}", new Gson().toJson(msgPush));
        try
        {
            ClientInfo client = ClientInfo.client();
            if (client == null)
            {
                log.warn("can't get client info");
                ExceptionUtils.setErrorInfo(result, ErrorCode.CUST_LOGIN_ACCT_EXPIRED);
                return result;
            }
            Long custId = client.getCustId();
            CustCustomerDTO customer = customerService.queryCustomerByCustId(custId);
            //只有企业账号才能推送消息
            if (customer.getParentCustId() != null)
            {
                ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_PUSH_AUTHOR_ERROR);
                return result;
            }
            MsgPushTagDO tagDO = queryTagName(custId, result);
            if (tagDO == null)
            {
                return result;
            }
            msgPush.setCustId(custId);
            msgPush.setMessageType(MsgTypeEnum.ALL.getCode());
            msgPush.setTagName(tagDO.getTagName());
            msgPush.setMessageIcon(customer.getCustIcon());
            saveAllPushMsg(msgPush);
            baiduPushService.pushMsgToAll(msgPush);
        } catch (Exception e)
        {
            log.error("", e);
        }
        return result;
    }

    private MsgPushTagDO queryTagName(Long custId, BaseResult result)
    {
        CustOptionDTO custDTO = new CustOptionDTO();
        custDTO.setCustId(custId);
        CustCustomerDTO customer = customerQueryService.query(custDTO);
        if (customer == null || customer.getParentCustId() == null)
        {
            log.warn("can't find orgId by custId : {}", custId);
            ExceptionUtils.setErrorInfo(result, ErrorCode.CUST_ORG_NOT_EXIST);
            return null;
        }
        List<MsgPushTagDO> tags = msgPushTagService.queryPushTagsByOrgId(customer.getParentCustId());
        if (tags != null && !tags.isEmpty())
        {
            return tags.get(0);
        } else
        {
            log.warn("can't find message tag info by orgId : {}", customer.getParentCustId());
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_TAG_NOT_EXISTS);
            return null;
        }

    }

    private void saveMsgByTag(List<MsgPushRelDO> rels, MsgPushDO msgPush)
    {
        msgPushService.addMsgPush(msgPush);
        List<MsgPushDetailDO> details = new ArrayList<MsgPushDetailDO>();
        for (MsgPushRelDO rel : rels)
        {
            MsgPushDetailDO detail = new MsgPushDetailDO();
            detail.setCustId(rel.getCustId());
            detail.setMessageDetail(msgPush.getMessageDetail());
            detail.setMessageLayout(msgPush.getMessageLayout());
            detail.setMessageStatus(MsgStatusEnum.UNREAD.getCode());
            detail.setMessageTitle(msgPush.getMessageTitle());
            detail.setMessageType(msgPush.getMessageType());
            detail.setTagName(msgPush.getTagName());
            detail.setMessageIcon(msgPush.getMessageIcon());
            details.add(detail);
        }
        msgPushDetailService.addMsgPushDetails(details);
    }

    private void savePushMsg(final MsgPushDO msgPush, final Long custId)
    {
        threadPool.execute(new Runnable()
        {
            @Override
            public void run()
            {
                msgPushService.addMsgPush(msgPush);
                MsgPushDetailDO detail = new MsgPushDetailDO();
                detail.setCustId(custId);
                detail.setMessageDetail(msgPush.getMessageDetail());
                detail.setMessageTitle(msgPush.getMessageTitle());
                detail.setMessageLayout(msgPush.getMessageLayout());
                detail.setMessageIcon(msgPush.getMessageIcon());
                detail.setMessageStatus(MsgStatusEnum.UNREAD.getCode());
                detail.setTagName(msgPush.getTagName());
                msgPushDetailService.addMsgPushDetail(detail);
            }
        });
    }

    private void saveAllPushMsg(final MsgPushDO msgPush)
    {
        threadPool.execute(new Runnable()
        {
            @Override
            public void run()
            {
                msgPushService.addMsgPush(msgPush);
                CustCustomerDTO customer = new CustCustomerDTO();
                customer.setIsDeleted(YesOrNo.NO);
                customer.setCustType(CustTypeEnum.PERSON);
                List<CustCustomerDTO> list = customerService.queryCustomerByCond(customer);
                List<MsgPushDetailDO> msgs = new ArrayList<MsgPushDetailDO>();
                for (CustCustomerDTO custDTO : list)
                {
                    MsgPushDetailDO tempDO = new MsgPushDetailDO();
                    tempDO.setMessageDetail(msgPush.getMessageDetail());
                    tempDO.setMessageTitle(msgPush.getMessageTitle());
                    tempDO.setMessageLayout(msgPush.getMessageLayout());
                    tempDO.setMessageType(msgPush.getMessageType());
                    tempDO.setMessageStatus(MsgStatusEnum.UNREAD.getCode());
                    tempDO.setCustId(custDTO.getId());
                    tempDO.setTagName(msgPush.getTagName());
                    tempDO.setMessageIcon(msgPush.getMessageIcon());
                    msgs.add(tempDO);
                }
                msgPushDetailService.addMsgPushDetails(msgs);
            }
        });

    }

    //    private String upLoadIcon(MultipartHttpServletRequest multipartHttpServletRequest, Long custId) {
    //        Map<String, MultipartFile> map = multipartHttpServletRequest.getFileMap();
    //        String encoding = multipartHttpServletRequest.getCharacterEncoding();
    //        if (map != null && !map.isEmpty()) {
    //            Iterator<Entry<String, MultipartFile>> it = map.entrySet().iterator();
    //            while (it.hasNext()) {
    //                MultipartFile file = it.next().getValue();
    //                String fileName = file.getOriginalFilename();
    //                CustAttachementDTO attachementDTO = new CustAttachementDTO();
    //                attachementDTO.setFileName(fileName);
    //                attachementDTO.setObjectId(custId);
    //                List<CustAttachementDTO> list = attachementService.queryAttachementByCond(attachementDTO);
    //                if (list != null && !list.isEmpty()) {
    //                    return list.get(0).getFileShortName();
    //                }
    //                attachementDTO.setAttachType(AttachmentTypeEnum.IMG_PICTURE);
    //                attachementDTO.setStoreType(FileStoreTypeEnum.IMG);
    //                attachementDTO.setIsDeleted(YesOrNo.NO);
    //
    //                String imgPath = buildFilePath(fileName, AttachmentTypeEnum.IMG_PICTURE);
    //                log.error("upload encoding:{}", encoding);
    //                String fileUrl = imgUtil.uploadFile(file, encoding, imgPath);
    //                log.info("uploadAttachement fileName:{}.", file.getOriginalFilename());
    //                attachementDTO.setFilePath(fileUrl);
    //                String shortName = imgUtil.publicURLNoAuth(fileUrl);
    //                attachementDTO.setFileShortName(shortName);
    //                attachementService.createAttachement(attachementDTO);
    //                return shortName;
    //
    //            }
    //        }
    //        return null;
    //    }
    //
    //    private String buildFilePath(String fileName, AttachmentTypeEnum type) {
    //        StringBuilder builder = new StringBuilder();
    //        builder.append(type.getPath() + "/");
    //        builder.append(System.currentTimeMillis() + "/");
    //        builder.append(fileName);
    //        return builder.toString();
    //    }

}
