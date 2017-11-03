package com.zhongan.icare.message.im.web;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.icare.message.im.service.ImAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by za-raozhikun on 2017/10/26.
 */
@Slf4j
@RestController
@RequestMapping(path = "/v1/im/tim")
public class ImTimController {

    @Resource
    private ImAdminService imAdminService;

    @RequestMapping(path = "{path}/{method}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResult<JSONObject> generateImSig(@PathVariable String path, @PathVariable String method, @RequestBody Map<String, Object> request) {
        String _path = path + "/" + method;
        JSONObject ret = null;
        try {
            ret = imAdminService.invoke(_path, request);
        } catch (Exception e) {
            log.warn("Tim Request Failed: path={}, request={}, ret={}, msg={}", _path, request, ret, e.getMessage(), e);
        } finally {
            log.info("Tim Request : path={}, request={}, ret={}", _path, request, ret);
        }
        return ExceptionUtils.success(ret);
    }

}
