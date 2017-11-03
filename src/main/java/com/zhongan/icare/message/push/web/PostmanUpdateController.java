package com.zhongan.icare.message.push.web;

import com.zhongan.icare.common.cache.redis.client.RedisUtils;
import com.zhongan.icare.message.push.constants.ConstantsDataKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/postman/update")
@Slf4j
public class PostmanUpdateController extends MessageBaseController
{


    /**
     * 清除模板的redis信息
     *
     * @return
     */
    @RequestMapping(value = "deleteTemplateRedis", method = RequestMethod.GET)
    public void deleteTemplateRedis(@RequestParam String templateCode)
    {
        String key = ConstantsDataKey.REDIS_TEMPLATE_CODE_PRE + templateCode;
        RedisUtils.removeExceptionOk(key);
    }

}
