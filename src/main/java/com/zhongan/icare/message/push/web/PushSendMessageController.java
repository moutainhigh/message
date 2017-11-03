package com.zhongan.icare.message.push.web;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.bean.PageDTO;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.health.common.utils.DateUtils;
import com.zhongan.health.common.utils.security.crypt.ICipher;
import com.zhongan.icare.common.client.ClientInfo;
import com.zhongan.icare.message.push.enums.MsgStatusEnum;
import com.zhongan.icare.share.common.constants.ErrorCode;
import com.zhongan.icare.share.message.dto.PushGroupDTO;
import com.zhongan.icare.share.message.dto.PushRequestGroupDTO;
import com.zhongan.icare.share.message.dto.PushRequestLogDTO;
import com.zhongan.icare.share.message.service.IPushGroupService;
import com.zhongan.icare.share.message.service.IPushRequestLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/v2/messageGroup")
@Slf4j
public class PushSendMessageController extends MessageBaseController {
    @Resource
    public ICipher                 senstiveCipher;

    @Resource
    private IPushRequestLogService pushRequestLogService;

    @Resource
    private IPushGroupService      pushGroupService;

    /**
     * 获取当前用户的所有信息
     *
     * @return
     */
    @RequestMapping(value = "/loadAllMessage", method = RequestMethod.POST)
    public BaseResult<List<PushRequestLogDTO>> loadAllMessage(@RequestBody PushRequestLogDTO requestLogDTO) {
        BaseResult<List<PushRequestLogDTO>> result = new BaseResult<List<PushRequestLogDTO>>();
        ClientInfo client = null;
        try {
            // 检查client信息
            client = loadClientInfo(result);
            if (client == null) {
                return result;
            }
            // 查询列表
            PushRequestLogDTO searchBean = new PushRequestLogDTO();
            searchBean.setCustomerId(client.getCustId());
            searchBean.setIsDeleted(YesOrNo.NO);
            if (requestLogDTO.getStatus() == null) {
                searchBean.setStatus(MsgStatusEnum.UNREAD.getCode());
            } else if (requestLogDTO.getStatus().intValue() == -1) {
                searchBean.setStatus(null);
            } else
                searchBean.setStatus(requestLogDTO.getStatus());
            searchBean.setPage(initPageInfo(requestLogDTO.getPage().getPageSize(),
                    requestLogDTO.getPage().getCurrentPage(), requestLogDTO.getPage().getStartRow()));
            PageDTO<PushRequestLogDTO> pageInfo = pushRequestLogService.selectByCond(searchBean);
            updateStatus(pageInfo, client.getCustId());
            encryptData(pageInfo);
            ExceptionUtils.setSuccess(result, pageInfo.getResultList());
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_GROUP_INFO_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }
        return result;
    }

    /**
     * 加密处理
     *
     * @param pageInfo
     */
    private void encryptData(PageDTO<PushRequestLogDTO> pageInfo) {
        if (pageInfo == null || pageInfo.getResultList() == null || pageInfo.getResultList().isEmpty())
            return;
        for (PushRequestLogDTO dto : pageInfo.getResultList()) {
            if (dto.getCustomerId() != null)
                dto.setCustIdStr(senstiveCipher.encrypt(dto.getCustomerId().toString()));
        }
    }

    /**
     * 获取所有分组信息
     *
     * @return
     */
    @RequestMapping(value = "/loadAllGroup", method = RequestMethod.GET)
    public BaseResult<List<PushGroupDTO>> loadAllGroup() {
        BaseResult<List<PushGroupDTO>> result = new BaseResult<List<PushGroupDTO>>();
        ClientInfo client = null;
        try {
            // 检查client信息
//            client = loadClientInfo(result);
//            if (client == null) {
//                return result;
//            }
            // 查询列表(目前包含已删除的)
            PushGroupDTO searchBean = new PushGroupDTO();
            List<PushGroupDTO> pushGroupDTOS = pushGroupService.selectAllFromRedis(searchBean);
            ExceptionUtils.setSuccess(result, pushGroupDTOS);
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_GROUP_INFO_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }
        return result;
    }

    /**
     * 获取会员的消息分组信息
     *
     * @return
     */
    @RequestMapping(value = "/loadGroupByCus", method = RequestMethod.GET)
    public BaseResult<List<PushRequestGroupDTO>> loadGroupByCus() {
        BaseResult<List<PushRequestGroupDTO>> result = new BaseResult<List<PushRequestGroupDTO>>();
        ClientInfo client = null;
        try {
            // 检查client信息
            client = loadClientInfo(result);
            if (client == null) {
                return result;
            }
            // 查询列表
            List<PushRequestGroupDTO> pushRequestGroupDTOList = pushRequestLogService
                    .selectRequestGroupByCus(client.getCustId());
            initData(pushRequestGroupDTOList);
            ExceptionUtils.setSuccess(result, pushRequestGroupDTOList);
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_GROUP_INFO_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }
        return result;
    }

    /**
     * 处理id信息
     *
     * @param pushRequestGroupDTOList
     */
    private void initData(List<PushRequestGroupDTO> pushRequestGroupDTOList) {
        if (pushRequestGroupDTOList == null || pushRequestGroupDTOList.isEmpty())
            return;
        for (PushRequestGroupDTO requestGroupDTO : pushRequestGroupDTOList) {
            if (requestGroupDTO.getPushRequestLogDTO() != null) {
                if (requestGroupDTO.getPushRequestLogDTO().getCustomerId() != null)
                    requestGroupDTO.getPushRequestLogDTO().setCustIdStr(
                            senstiveCipher.encrypt(requestGroupDTO.getPushRequestLogDTO().getCustomerId().toString()));
            }
        }
    }

    /**
     * 根据分组id获取该分组下的所有消息
     *
     * @return
     */
    @RequestMapping(value = "/loadMessageByGroup", method = RequestMethod.POST)
    public BaseResult<PageDTO<PushRequestLogDTO>> loadMessageByGroup(@RequestBody PushRequestLogDTO requestLogDTO) {
        BaseResult<PageDTO<PushRequestLogDTO>> result = new BaseResult<PageDTO<PushRequestLogDTO>>();
        ClientInfo client = null;
        try {
            // 检查client信息
            client = loadClientInfo(result);
            if (client == null) {
                return result;
            }

            // 查询分组信息
            PushRequestLogDTO searchBean = new PushRequestLogDTO();
            searchBean.setCustomerId(client.getCustId());
            searchBean.setGroupCode(requestLogDTO.getGroupCode());
            searchBean.setIsDeleted(YesOrNo.NO);
            searchBean.setPage(initPageInfo(requestLogDTO.getPage().getPageSize(),
                    requestLogDTO.getPage().getCurrentPage(), requestLogDTO.getPage().getStartRow()));
            PageDTO<PushRequestLogDTO> pageInfo = pushRequestLogService.selectByCond(searchBean);
            updateStatus(pageInfo, client.getCustId());
            ExceptionUtils.setSuccess(result, pageInfo);
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_GROUP_MESSAGE_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }
        return result;
    }

    private void updateStatus(final PageDTO<PushRequestLogDTO> pageInfo, final Long custId) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<PushRequestLogDTO> resultList = pageInfo.getResultList();
                if (resultList == null || resultList.isEmpty())
                    return;
                List<Long> ids = new ArrayList<Long>();
                for (PushRequestLogDTO p : resultList) {
                    ids.add(p.getId());
                }
                PushRequestLogDTO updateDTO = new PushRequestLogDTO();
                updateDTO.setIds(ids);
                updateDTO.setStatus(MsgStatusEnum.READED.getCode());
                updateDTO.setModifier(custId.toString());
                pushRequestLogService.updateStatusBatch(updateDTO);
            }
        });
    }

    /**
     * 删除当前员工的消息分组的下的所有的消息
     *
     * @param groupCode
     * @return
     */
    @RequestMapping(value = "/deleteMessageGroup", method = RequestMethod.GET)
    public BaseResult<Void> deleteMessageGroup(@RequestParam String groupCode) {
        BaseResult<Void> result = new BaseResult<Void>();
        ClientInfo client = null;
        try {
            // 检查client信息
            client = loadClientInfo(result);
            if (client == null) {
                return result;
            }
            PushRequestLogDTO updateBean = new PushRequestLogDTO();
            updateBean.setGroupCode(groupCode);
            updateBean.setCustomerId(client.getCustId());
            updateBean.setIsDeleted(YesOrNo.YES);
            pushRequestLogService.updateBySelective(updateBean);
            result.setCodeSuccess();
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_GROUP_DELETE_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }

        return result;
    }

    /**
     * 批量删除消息
     *
     * @param requestLogDTO
     * @return
     */
    @RequestMapping(value = "/deleteMessage", method = RequestMethod.POST)
    public BaseResult<Void> deleteMessage(@RequestBody PushRequestLogDTO requestLogDTO) {
        BaseResult<Void> result = new BaseResult<Void>();
        ClientInfo client = null;
        try {
            // 检查client信息
            client = loadClientInfo(result);
            if (client == null) {
                return result;
            }
            PushRequestLogDTO updateBean = new PushRequestLogDTO();
            updateBean.setIds(requestLogDTO.getIds());
            updateBean.setIsDeleted(YesOrNo.YES);
            updateBean.setModifier(String.valueOf(client.getCustId()));
            updateBean.setGmtModified(new Date());
            pushRequestLogService.deleteBatchUpdate(updateBean);
            result.setCodeSuccess();
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_GROUP_DELETE_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }

        return result;
    }

    /**
     * 查询当前用户未读的信息的数量
     *
     * @return
     */
    @RequestMapping(value = "/queryUnreadCount", method = RequestMethod.GET)
    public BaseResult<Integer> queryUnreadCount() {
        BaseResult<Integer> result = new BaseResult<Integer>();
        ClientInfo client = null;
        try {
            // 检查client信息
            client = loadClientInfo(result);
            if (client == null) {
                return result;
            }
            PushRequestLogDTO queryBean = new PushRequestLogDTO();
            queryBean.setIsDeleted(YesOrNo.NO);
            queryBean.setCustomerId(client.getCustId());
            queryBean.setStatus(MsgStatusEnum.UNREAD.getCode());
            int i = pushRequestLogService.countByCond(queryBean);
            result.setResult(i);
            result.setCodeSuccess();
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_QUERY_UNREAD_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }

        return result;
    }

    /**
     * 查询某个时间点到现在的所有消息
     * 
     * @param dateStr
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/loadOldMessage", method = RequestMethod.GET)
    public BaseResult<PageDTO<PushRequestLogDTO>> loadOldMessage(@RequestParam String dateStr,
                                                                 @RequestParam Integer pageSize,
                                                                 @RequestParam Integer currentPage) {
        BaseResult<PageDTO<PushRequestLogDTO>> result = new BaseResult<PageDTO<PushRequestLogDTO>>();
        ClientInfo client = null;
        try {
            // 检查client信息
            client = loadClientInfo(result);
            if (client == null) {
                return result;
            }

            if (com.zhongan.health.common.utils.StringUtils.isBlank(dateStr)) {
                ExceptionUtils.setErrorInfo(result, ErrorCode.CMN_ILLEGAL_ARG, "查询日期为空");
                return result;
            }
            PushRequestLogDTO queryBean = new PushRequestLogDTO();
            queryBean
                    .setSearchStartDate(DateUtils.getDateFormatStr(dateStr, DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
            queryBean.setIsDeleted(YesOrNo.NO);
            queryBean.setCustomerId(client.getCustId());
            queryBean.setPage(initPageInfo(pageSize, currentPage, null));
            PageDTO<PushRequestLogDTO> pageInfo = pushRequestLogService.selectByCond(queryBean);
            updateViewData(pageInfo);
            ExceptionUtils.setSuccess(result, pageInfo);
        } catch (Exception e) {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_QUERY_UNREAD_EXCEPTION, JSONObject.toJSONString(client));
            log.error("", e);
        }

        return result;
    }

    /**
     * 隐藏customerId
     * @param pageInfo
     */
    private void updateViewData(PageDTO<PushRequestLogDTO> pageInfo){
        if(pageInfo == null || pageInfo.getResultList() == null ||pageInfo.getResultList().isEmpty()) return;
        List<PushRequestLogDTO> resultList = pageInfo.getResultList();
        for (PushRequestLogDTO requestLogDTO : resultList) {
            if (requestLogDTO.getCustomerId() == null) continue;
            requestLogDTO.setCustIdStr(senstiveCipher.encrypt(requestLogDTO.getCustomerId().toString()));
            requestLogDTO.setCustomerId(null);
        }
    }
}
