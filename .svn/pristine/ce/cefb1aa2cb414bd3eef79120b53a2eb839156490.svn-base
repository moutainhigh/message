package com.zhongan.icare.message.push.web;

import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.icare.common.cache.redis.client.RedisUtils;
import com.zhongan.icare.common.client.ClientInfo;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushDetailDO;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushTagDO;
import com.zhongan.icare.message.push.enums.IsDeleteEnum;
import com.zhongan.icare.message.push.enums.MsgStatusEnum;
import com.zhongan.icare.message.push.service.IMsgPushCustTagService;
import com.zhongan.icare.message.push.service.IMsgPushDetailService;
import com.zhongan.icare.message.push.service.IMsgPushTagService;
import com.zhongan.icare.message.push.util.TimeConvertUtil;
import com.zhongan.icare.share.common.constants.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/v1/pushMsgDetail")
public class MsgPushDetailController
{
    @Resource
    private IMsgPushDetailService msgPushDetailService;
    @Resource
    private IMsgPushCustTagService msgPushCustTagService;
    @Resource
    private IMsgPushTagService msgPushTagService;

    private final static String CACHKEY = "PUSHTAG_";

    private final static int EXPIRE_TIME_IN_SECOND = 864000;

    /**
     * 通过消息Id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryMsgById", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResult<MsgPushDetailDO> queryMsgDetailById(@RequestParam Long id)
    {
        BaseResult<MsgPushDetailDO> result = new BaseResult<MsgPushDetailDO>();
        try
        {
            MsgPushDetailDO detail = msgPushDetailService.queryMsgPushDetail(id);
            if (detail != null)
            {
                updateMessageIcon(detail);
                detail.setMsgTime(TimeConvertUtil.formatDisplayTime(detail.getGmtCreated()));
                result.setResult(detail);
                result.setCode(BaseResult.SUCCESS_CODE);
                MsgPushDetailDO _do = new MsgPushDetailDO();
                _do.setId(detail.getId());
                _do.setMessageStatus(MsgStatusEnum.READED.getCode());
                msgPushDetailService.updateMsg(_do);
            } else
            {
                log.warn("can't query push msg detail by id : {}", id);
                ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_DETAIL_QUERY_FAILED);
            }

        } catch (Exception e)
        {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_DETAIL_QUERY_FAILED);
            log.error("", e);
        }
        return result;
    }

    /**
     * 分页查询消息列表
     *
     * @param tagName
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/queryMsgByPage", method = {RequestMethod.GET})
    public BaseResult<List<MsgPushDetailDO>> queryMsgDetailByPage(@RequestParam(required = false) String tagName,
                                                                  @RequestParam Integer pageSize,
                                                                  @RequestParam Integer currentPage)
    {
        BaseResult<List<MsgPushDetailDO>> result = new BaseResult<List<MsgPushDetailDO>>();
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
            Integer newPageSize = pageSize;
            if (pageSize == null)
            {
                newPageSize = 10;
            }

            Integer start = 0;

            if (currentPage != null)
            {
                start = (currentPage - 1) * newPageSize;
            }
            MsgPushDetailDO custMsgPushDetailDO = new MsgPushDetailDO();
            custMsgPushDetailDO.setCustId(custId);
            custMsgPushDetailDO.setTagName(tagName);
            custMsgPushDetailDO.setStart(start);
            custMsgPushDetailDO.setEnd(newPageSize);
            List<MsgPushDetailDO> list = msgPushDetailService.queryMsgs(custMsgPushDetailDO);
            if (list != null && !list.isEmpty())
            {
                for (MsgPushDetailDO detail : list)
                {
                    updateMessageIcon(detail);
                    detail.setMsgTime(TimeConvertUtil.formatDisplayTime(detail.getGmtCreated()));
                }
            }
            result.setResult(list);
            result.setCodeSuccess();

        } catch (Exception e)
        {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_DETAIL_QUERY_FAILED);
            log.error("", e);
        }
        return result;
    }

    /**
     * 删除消息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteMsg", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResult<Void> deleteMsg(@RequestParam Long id)
    {
        BaseResult<Void> result = new BaseResult<Void>();
        try
        {
            MsgPushDetailDO detail = new MsgPushDetailDO();
            detail.setId(id);
            detail.setIsDeleted(IsDeleteEnum.YES.getValue());
            msgPushDetailService.updateMsg(detail);
            result.setCodeSuccess();
        } catch (Exception e)
        {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_DELETE_ERROR);
            log.error("", e);
        }
        return result;
    }

    /**
     * 更新消息状态
     *
     * @param id
     * @param msgStatus
     * @return
     */
    @RequestMapping(value = "/updateMsg", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResult<Void> updateMsg(@RequestParam Long id, @RequestParam Integer msgStatus)
    {
        BaseResult<Void> result = new BaseResult<Void>();
        try
        {
            MsgPushDetailDO detail = new MsgPushDetailDO();
            detail.setId(id);
            detail.setMessageStatus(msgStatus);
            msgPushDetailService.updateMsg(detail);
            result.setCodeSuccess();
        } catch (Exception e)
        {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_UPDATE_ERROR);
            log.error("", e);
        }
        return result;
    }

    /**
     * 统计未读消息
     *
     * @return
     */
    @RequestMapping(value = "/countUnreadMsg", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResult<Integer> countUnreadMsg()
    {
        BaseResult<Integer> result = new BaseResult<Integer>();
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
            int count = msgPushDetailService.countUnreadMsg(custId);
            result.setResult(count);
            result.setCodeSuccess();
        } catch (Exception e)
        {
            log.error("", e);
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_COUNT_QUERY_ERROR);
        }
        return result;
    }

    /**
     * 查询账户下的所有消息推送标签组
     *
     * @return
     */
    @RequestMapping(value = "/queryTags", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResult<List<MsgPushTagDO>> queryTags()
    {
        BaseResult<List<MsgPushTagDO>> result = new BaseResult<List<MsgPushTagDO>>();
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
            List<MsgPushTagDO> list = msgPushCustTagService.queryCustTag(custId);
            result.setResult(list);
            result.setCodeSuccess();
        } catch (Exception e)
        {
            log.error("", e);
            ExceptionUtils.setErrorInfo(result, ErrorCode.CMN_QUERY_ERR);
        }
        return result;
    }

    private void updateMessageIcon(MsgPushDetailDO detailDO)
    {
        String redisKey = new StringBuilder().append(CACHKEY).append(detailDO.getOrgId()).toString();
        MsgPushTagDO tagDO = RedisUtils.getObjectExceptionNull(redisKey, MsgPushTagDO.class);
        if (tagDO == null)
        {
            List<MsgPushTagDO> tags = msgPushTagService.queryPushTagsByOrgId(detailDO.getOrgId());
            if (tags != null && !tags.isEmpty())
            {
                tagDO = tags.get(0);
                detailDO.setMessageIcon(tagDO.getTagIcon());
                RedisUtils.putExceptionOk(redisKey, tagDO, EXPIRE_TIME_IN_SECOND);
            }
        } else
        {
            detailDO.setMessageIcon(tagDO.getTagIcon());
        }
    }

}
