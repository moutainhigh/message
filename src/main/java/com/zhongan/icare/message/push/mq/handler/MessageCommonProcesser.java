package com.zhongan.icare.message.push.mq.handler;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.health.common.share.bean.PageDTO;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.utils.BeanUtils;
import com.zhongan.health.common.utils.StringUtils;
import com.zhongan.icare.common.cache.redis.client.RedisUtils;
import com.zhongan.icare.common.mq.handler.MatchProcessor;
import com.zhongan.icare.message.push.constants.ConstantsDataKey;
import com.zhongan.icare.message.push.enums.BaiduMessageTypeEnum;
import com.zhongan.icare.message.push.enums.MsgStatusEnum;
import com.zhongan.icare.message.push.util.VelocityUtils;
import com.zhongan.icare.share.message.dto.*;
import com.zhongan.icare.share.message.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class MessageCommonProcesser<T> implements MatchProcessor<T>
{

    @Resource
    public IPushRequestDataService pushRequestDataService;

    @Resource
    private IPushTemplateService pushTemplateService;

    @Resource(name = "velocityEngine")
    private VelocityEngine velocityEngine;

    @Resource
    private IPushRequestLogService pushRequestLogService;

    @Resource
    private IPushBaiduService pushBaiduService;

    @Resource
    private IPushBaiduChannelService pushBaiduChannelService;

    @Value("${za.icare.redis.templateCodeExpire}")
    private String templateCodeExpire;

    @Value("${za.icare.redis.repeatConsumeExpireTime}")
    private String expireTime;

    @Value("${za.icare.message.processor.countsByOneTime}")
    private Integer countsByOneTime;

    @Value("${za.icare.message.processor.threadCount}")
    private Integer threadCount;

    /**
     * 计算key
     *
     * @param object
     * @return
     */
    protected abstract String caclKey(T object, String... keys);

    /**
     * 计算公共的key
     *
     * @param keys
     * @return
     */
    protected String commonKey(String... keys)
    {
        StringBuilder keyStr = new StringBuilder("");
        if (keys != null && keys.length > 0)
        {
            for (String k : keys)
                keyStr.append(k);
        }
        return keyStr.toString();
    }

    /**
     * 计算公共的key
     *
     * @param pushRequestLogDTO
     * @return
     */
    protected String commonKey(PushRequestLogDTO pushRequestLogDTO)
    {
        return pushRequestLogDTO.getTemplateCode() + JSONObject.toJSONString(pushRequestLogDTO.getDataMap());
    }

    /**
     * 检查是否是重复消费，目前设置2秒内的相同的key可以认为是重复消费
     *
     * @return true:重复消费，false:不是重复消费
     */
    protected Boolean isRepeatConsume(Object key)
    {
        if (key == null || StringUtils.isBlank(key.toString()))
            return true;
        Object value = RedisUtils.get(key.toString());
        if (value == null)
        {
            synchronized (key)
            {
                value = RedisUtils.get(key.toString());
                if (value == null)
                {
                    RedisUtils.put(key.toString(), key.toString(), Integer.parseInt(expireTime));
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    /**
     * 若某个key在contextMap和newContextMap中同时存在，
     * 则把key在contextMap中的值替换成newContextMap中对应的值
     *
     * @param contextMap
     * @param newContextMap
     */
    protected void convertMapData(Map<String, Object> contextMap, Map<String, Object> newContextMap)
    {
        if (newContextMap == null)
            return;
        Iterator<Map.Entry<String, Object>> iterator = newContextMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, Object> entry = iterator.next();
            if (contextMap.get(entry.getKey()) != null)
                contextMap.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 根据TemplateCode获取PushTemplateDTO
     *
     * @param requestLogDTO
     * @return
     */
    protected PushTemplateDTO loadPushTemplateDTO(PushRequestLogDTO requestLogDTO)
    {
        // 先从缓存中获取
        String key = ConstantsDataKey.REDIS_TEMPLATE_CODE_PRE + requestLogDTO.getTemplateCode();
        PushTemplateDTO redisDTO = (PushTemplateDTO) RedisUtils.getObject(key, PushTemplateDTO.class);
        if (redisDTO != null)
            return redisDTO;
        PushTemplateDTO templateDTO = queryTemplate(requestLogDTO.getTemplateCode(), true);
        if (templateDTO == null)
            return templateDTO;
        RedisUtils.putObject(key, templateDTO, Integer.parseInt(templateCodeExpire));
        return templateDTO;
    }

    private PushTemplateDTO queryTemplate(String param, Boolean isTemplate)
    {
        PushTemplateDTO searchBean = new PushTemplateDTO();
        if (isTemplate)
            searchBean.setCode(param);
        else
            searchBean.setGroupCode(param);
        searchBean.setIsDeleted(YesOrNo.NO);
        List<PushTemplateDTO> pushTemplateList = pushTemplateService.selectByCond(searchBean);
        if (pushTemplateList == null || pushTemplateList.isEmpty())
        {
            log.error("templateCode is not exist. param:{}", param);
            return null;
        }
        PushTemplateDTO dbDTO = pushTemplateList.get(0);
        return dbDTO;
    }

    /**
     * 根据groupCode获取template,如果存在多个，目前获取1个
     *
     * @param groupCode
     * @return
     */
    protected PushTemplateDTO loadTemplateByGroup(String groupCode)
    {
        if (StringUtils.isBlank(groupCode))
            return null;
        // 先从缓存中获取
        String key = ConstantsDataKey.REDIS_GROUP_CODE_PRE + groupCode;
        PushTemplateDTO redisDTO = (PushTemplateDTO) RedisUtils.getObject(key, PushTemplateDTO.class);
        if (redisDTO != null)
            return redisDTO;
        PushTemplateDTO templateDTO = queryTemplate(groupCode, false);
        if (templateDTO == null)
            return templateDTO;
        RedisUtils.putObject(key, templateDTO, Integer.parseInt(templateCodeExpire));
        return templateDTO;
    }

    /**
     * 转换其中的特殊字符
     *
     * @param contextMap
     */
    private void convertData(Map<String, Object> contextMap)
    {
        if (contextMap == null || contextMap.isEmpty())
            return;
        Iterator<Map.Entry<String, Object>> iterator = contextMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, Object> next = iterator.next();
            Object objValue = next.getValue();
            if (objValue == null)
                continue;
            String value = objValue.toString();
            value = value.replaceAll("\"", "\\\\\"");
            contextMap.put(next.getKey(), value);
        }
    }

    /**
     * 组装单个PushRequestLogDTO
     *
     * @param requestLogDTO
     * @param pushTemplateDTO
     * @return
     */
    protected void initLogDTO(PushRequestLogDTO requestLogDTO, PushTemplateDTO pushTemplateDTO,
                              Map<String, Object> contextMap)
    {
        // 设置记录原始信息
        String data = JSONObject.toJSONString(requestLogDTO.getDataMap());
        requestLogDTO.setData(data);
        //        // 转换消息
        //        convertData(contextMap);

        // 消息主体
        String msgModel = pushTemplateDTO.getMsgModel();
        String templateData = data;
        if (!StringUtils.isBlank(msgModel))
        {
            msgModel = VelocityUtils.convertData(velocityEngine, requestLogDTO.getDataMap(), msgModel);
            JSONObject newJsonObject = JSONObject.parseObject(msgModel);
            Map<String, Object> newContextMap = (Map) newJsonObject;
            // 设置一下标题
            Object title = newContextMap.get(ConstantsDataKey.DATA_KEY_TITLE);
            if (title != null && !StringUtils.isBlank(title.toString()))
                contextMap.put(ConstantsDataKey.DATA_KEY_TITLE, title);

            Object description = newContextMap.get(ConstantsDataKey.DATA_KEY_DESCRIPTION);
            if (description != null && !StringUtils.isBlank(description.toString()))
                contextMap.put(ConstantsDataKey.DATA_KEY_DESCRIPTION, description);

            // 根据模板数据转换消息
            //            convertMapData(contextMap, newContextMap);
            templateData = JSONObject.toJSONString(newContextMap);
        }
        // old：目前把转换的数据全部放入自定义字段中
        // 暂且判断如果是积分发放的就跳转到消息分组界面
        Map<String, String> customerMap = new HashMap<>();
        if (ConstantsDataKey.GROUP_CODE_1.equals(pushTemplateDTO.getGroupCode()))
        {
            customerMap.put(ConstantsDataKey.CUSTOMER_URI, "icare://message");
        }
        customerMap.put(ConstantsDataKey.CUSTOMER_GROUPCODE, pushTemplateDTO.getGroupCode());
        contextMap.put(ConstantsDataKey.ANDROID_CUSTOM_CONTENT, customerMap);
        /**
         * 当外部未设定分组信息，则以模板归属组为准
         */
        if (StringUtils.isEmpty(requestLogDTO.getGroupCode()))
            requestLogDTO.setGroupCode(pushTemplateDTO.getGroupCode());
        requestLogDTO.setTemplateData(templateData);
        requestLogDTO.setStatus(MsgStatusEnum.UNREAD.getCode());
        requestLogDTO.setMessageLayout(pushTemplateDTO.getMsgLayout());
    }

    /**
     * 组装多个PushRequestLogDTO
     *
     * @param requestLogDTO
     * @param pushTemplateDTO
     * @return
     */
    protected List<PushRequestLogDTO> initMultiLogDTO(PushRequestLogMultiDTO requestLogDTO,
                                                      PushTemplateDTO pushTemplateDTO, Map<String, Object> contextMap)
    {
        // 消息主体
        initLogDTO(requestLogDTO, pushTemplateDTO, contextMap);
        // 组装每个人的消息
        List<PushRequestLogDTO> list = new ArrayList<PushRequestLogDTO>();
        for (Long customerId : requestLogDTO.getCustomerIds())
        {
            PushRequestLogDTO newLogDTO = BeanUtils.simpleDOAndBOConvert(requestLogDTO, PushRequestLogDTO.class);
            newLogDTO.setCustomerId(customerId);
            list.add(newLogDTO);
        }
        return list;
    }

    /**
     * 插入消息
     *
     * @param newRequestLogDTO
     * @param contextMap
     * @return
     */
    private void insertRequestLog(PushRequestLogDTO newRequestLogDTO, Map<String, Object> contextMap)
    {
        Integer msgType = newRequestLogDTO.getMessageType() == null ? BaiduMessageTypeEnum.INFORM.getCode()
                : BaiduMessageTypeEnum.TOUCHUAN.getCode();
        newRequestLogDTO.setMessageType(msgType);
        long requestLogId = pushRequestLogService.insert(newRequestLogDTO);
        contextMap.put(ConstantsDataKey.REQUEST_LOG_ID, requestLogId);
        // 目前只支持通知类型的消息，后面会直接用requestLogDTO.getMessageType()
        contextMap.put(ConstantsDataKey.REQUEST_MESSAGE_TYPE, msgType);
    }

    /**
     * 保存消息并且发送推送信息 需要根据用户查询渠道信息
     *
     * @param newRequestLogDTO
     * @param contextMap
     */
    protected void insertAndSendMessage(PushRequestLogDTO newRequestLogDTO, Map<String, Object> contextMap)
    {
        // 插入消息
        insertRequestLog(newRequestLogDTO, contextMap);

        // 推送服务
        pushMessage(newRequestLogDTO.getCustomerId(), contextMap);
    }

    /**
     * 推送服务
     *
     * @param customerId
     * @param contextMap
     */
    protected void pushMessage(Long customerId, Map<String, Object> contextMap)
    {
        PushBaiduChannelDTO searchChannelBean = new PushBaiduChannelDTO();
        searchChannelBean.setCustomerId(customerId);
        List<PushBaiduChannelDTO> pushBaiduChannelList = pushBaiduChannelService.selectByCond(searchChannelBean);
        if (pushBaiduChannelList == null || pushBaiduChannelList.isEmpty())
        {
            log.info("customerId:{}的用户的channelId不存在，无法进行推送服务！", customerId);
            return;
        }
        pushBaiduService.pushMsgToSingleDevice(pushBaiduChannelList.get(0), contextMap);
    }

    /**
     * 保存消息并且发送推送信息 根据查询出来的渠道信息
     *
     * @param newRequestLogDTO
     * @param contextMap
     * @param pushBaiduChannelDTO
     */
    private void insertAndSendMessageByChannel(PushRequestLogDTO newRequestLogDTO, Map<String, Object> contextMap,
                                               PushBaiduChannelDTO pushBaiduChannelDTO)
    {
        // 插入消息
        insertRequestLog(newRequestLogDTO, contextMap);

        // 百度推广发送
        pushBaiduService.pushMsgToSingleDevice(pushBaiduChannelDTO, contextMap);
    }

    /**
     * 给所有设备发送推送消息，并且保存发送数据
     */
    protected void pushMsg2AllAndSaveData(Map<String, Object> contextMap, PushTemplateDTO pushTemplateDTO,
                                          PushRequestLogDTO requestLogDTO)
    {
        // 首先加载所有的设备信息
        final PushBaiduChannelDTO searchBean = new PushBaiduChannelDTO();
        searchBean.setIsDeleted(YesOrNo.NO);
        final int countAll = pushBaiduChannelService.countByCond(searchBean);
        if (countAll <= 0)
            return;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setPageSize(countsByOneTime);
        if (countAll < countsByOneTime.intValue())
        {
            searchBean.setPage(pageDTO);
            executor(contextMap, pushTemplateDTO, requestLogDTO, executorService, searchBean);
            return;
        }
        // 循环的次数
        BigDecimal times = BigDecimal.valueOf(countAll / countsByOneTime.doubleValue());
        BigDecimal exactTimes = new BigDecimal(times.intValue());
        exactTimes = times.compareTo(exactTimes) != 0 ? exactTimes.add(new BigDecimal(1)) : exactTimes;
        for (int i = 1; i <= exactTimes.intValue(); i++)
        {
            pageDTO.setCurrentPage(i);
            searchBean.setPage(pageDTO);
            executor(contextMap, pushTemplateDTO, requestLogDTO, executorService, searchBean);
        }

    }

    /**
     * 线程执行
     *
     * @param executorService
     * @param searchBean
     */
    private void executor(final Map<String, Object> contextMap, final PushTemplateDTO pushTemplateDTO,
                          final PushRequestLogDTO requestLogDTO, ExecutorService executorService,
                          final PushBaiduChannelDTO searchBean)
    {
        executorService.execute(new Runnable()
        {
            @Override
            public void run()
            {
                List<PushBaiduChannelDTO> list = pushBaiduChannelService.selectByCond(searchBean);
                if (list == null || list.isEmpty())
                    return;
                for (PushBaiduChannelDTO baiduChannelDTO : list)
                {
                    Map<String, Object> contextMapNew = new HashMap<String, Object>();
                    contextMapNew.putAll(contextMap);
                    requestLogDTO.setCustomerId(baiduChannelDTO.getCustomerId());
                    // 组装contextMap和初始化PushRequestLogDTO对象
                    initLogDTO(requestLogDTO, pushTemplateDTO, contextMapNew);
                    // 根据渠道信息进行保存数据和发送
                    insertAndSendMessageByChannel(requestLogDTO, contextMapNew, baiduChannelDTO);
                }
            }
        });
    }

    public PushRequestDataDTO queryRequestData(Long id){
        // 查询消息
        PushRequestDataDTO pushRequestDataDTO = pushRequestDataService.selectByPrimaryKey(id);
        if(pushRequestDataDTO == null){
            log.error("<<<<<<,errorMessage:pushRequestDataDTO=null,id={}",id);
        }
        return pushRequestDataDTO;
    }

    public boolean deleteRequestData(Long id){
        PushRequestDataDTO updateBean = new PushRequestDataDTO();
        updateBean.setIsDeleted(YesOrNo.YES);
        updateBean.setId(id);
        int i = pushRequestDataService.updateByPrimaryKeySelective(updateBean);
        if(i<=0){
            log.error("<<<<<<删除PushRequestDataDTO失败,updateBean={}",JSONObject.toJSONString(updateBean));
            return false;
        }
        return true;
    }

}
