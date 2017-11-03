package com.zhongan.icare.message.im.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by za-raozhikun on 2017/8/14.
 */
public interface ImAdminService {

    JSONObject invoke(String path, Object requestJson);

}
