package com.zhongan.icare.message.push.web;

import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.icare.common.client.ClientInfo;
import com.zhongan.icare.message.push.dao.dataobject.MsgPushRelDO;
import com.zhongan.icare.message.push.service.IMsgPushRelService;
import com.zhongan.icare.share.common.constants.ErrorCode;
import com.zhongan.icare.share.message.dto.PushBaiduChannelDTO;
import com.zhongan.icare.share.message.service.IPushBaiduChannelService;
import com.zhongan.icare.share.message.service.IPushBaiduService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/v2/deviceInfo")
@Slf4j
public class PushDeviceInfoController extends MessageBaseController
{

    @Resource
    private IPushBaiduChannelService pushBaiduChannelService;

    @Resource
    private IMsgPushRelService msgPushRelService;


    @Resource
    private IPushBaiduService pushBaiduService;


    @RequestMapping(value = "/updateDevice", method = RequestMethod.GET)
    public BaseResult<Void> updateDevice(@RequestParam Integer deviceType, @RequestParam String channelId)
    {
        BaseResult<Void> result = new BaseResult<Void>();
        log.info("deviceType : {}, channelId : {}", deviceType, channelId);
        try
        {
            // 检查client信息
            ClientInfo client = loadClientInfo(result);
            if (client == null)
            {
                return result;
            }
            // 检查设备信息
            if (!checkDeviceInfo(result, deviceType, channelId))
            {
                return result;
            }

            // 检查当前会员
            if (!checkCustomerInfo(result, client.getCustId()))
            {
                return result;
            }

            // 保存推送设备信息
            PushBaiduChannelDTO baiduChannelDTO = new PushBaiduChannelDTO();
            baiduChannelDTO.setCustomerId(client.getCustId());
            baiduChannelDTO.setChannelId(channelId);
            baiduChannelDTO.setDeviceType(deviceType);
            Boolean execResult = pushBaiduChannelService.saveOrUpdateDevice(baiduChannelDTO);
            if (execResult)
            {
                result.setCodeSuccess();
            }
        } catch (Exception e)
        {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_UPDATE_DEVICE_EXCEPTION, deviceType, channelId);
            log.error("", e);
        }
        return result;
    }

    /**
     * 该方法只会执行一次，在系统替换成V2版本的时候
     *
     * @return
     */
    @RequestMapping(value = "/initDevice", method = RequestMethod.GET)
    public BaseResult<Void> initDevice()
    {
        BaseResult<Void> result = new BaseResult<Void>();
        try
        {
            // 查询出当前的所有设备号
            List<MsgPushRelDO> msgPushRelDOS = msgPushRelService.queryCustDevices(new MsgPushRelDO());
            if (msgPushRelDOS == null || msgPushRelDOS.isEmpty()) return result;

            // 遍历执行插入操作(这个地方就不做批处理了,数据量不多并且只会用一次)
            Long start = System.currentTimeMillis();
            log.info("执行设备数据初始化......开始");
            for (MsgPushRelDO p : msgPushRelDOS)
            {
                PushBaiduChannelDTO baiduChannelDTO = new PushBaiduChannelDTO();
                baiduChannelDTO.setCustomerId(p.getCustId());
                baiduChannelDTO.setChannelId(p.getChannelId());
                baiduChannelDTO.setDeviceType(p.getDeviceType());
                pushBaiduChannelService.saveOrUpdateDevice(baiduChannelDTO);
            }
            log.info("执行设备数据初始化......结束，总共耗时（" + (System.currentTimeMillis() - start) + "毫秒）");
        } catch (Exception e)
        {
            ExceptionUtils.setErrorInfo(result, ErrorCode.MSG_UPDATE_DEVICE_EXCEPTION);
            log.error("", e);
        }
        return result;
    }

//    @RequestMapping(value = "/pushMessage", method = RequestMethod.POST)
//    public BaseResult<Void> pushMessage(@RequestBody PushMessageDTO pushMessageDTO)
//    {
//        // 组装参数
//        BaseResult<Void> result = new BaseResult<Void>();
//        Map<String, Object> contextMap = new HashMap();
//        Integer msgType = pushMessageDTO.getMsgType() == null ? BaiduMessageTypeEnum.TOUCHUAN.getCode() : BaiduMessageTypeEnum.INFORM.getCode();
//        contextMap.put(ConstantsDataKey.REQUEST_MESSAGE_TYPE, msgType);
//        contextMap.put(ConstantsDataKey.DATA_KEY_TITLE, pushMessageDTO.getTitle());
//        contextMap.put(ConstantsDataKey.ANDROID_NOTIFICATION_BASIC_STYLE, 7);// 目前默认7
//        contextMap.put(ConstantsDataKey.ANDROID_OPEN_TYPE, 0);// 目前默认0
//        Map<String, Object> data = pushMessageDTO.getData();
//        if (data != null && !data.isEmpty())
//        {
//            Map<String, Object> map = Maps.newHashMap();
//            Object customContent = data.get(ConstantsDataKey.ANDROID_CUSTOM_CONTENT);
//            if (customContent != null) map.putAll((Map) customContent);
//            Object schema = data.get(ConstantsDataKey.IOS_SCHEME);
//            if (schema != null) map.put(ConstantsDataKey.IOS_SCHEME, schema.toString());
//            contextMap.put(ConstantsDataKey.ANDROID_CUSTOM_CONTENT, map);
//        }
//        contextMap.put(ConstantsDataKey.DATA_KEY_DESCRIPTION, pushMessageDTO.getDescription());
//        PushBaiduChannelDTO searchChannelBean = new PushBaiduChannelDTO();
//        searchChannelBean.setCustomerId(pushMessageDTO.getCustomerId());
//        List<PushBaiduChannelDTO> pushBaiduChannelList = pushBaiduChannelService.selectByCond(searchChannelBean);
//        if (pushBaiduChannelList == null || pushBaiduChannelList.isEmpty())
//        {
//            log.error("customerId:{}的用户的channelId不存在，无法进行推送服务！", pushMessageDTO.getCustomerId());
//            return result;
//        }
//        pushBaiduService.pushMsgToSingleDevice(pushBaiduChannelList.get(0), contextMap);
//        return result;
//    }
}
