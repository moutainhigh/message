package com.zhongan.icare.message.im.util;

import com.alibaba.fastjson.JSONObject;
import com.zhongan.health.common.utils.exception.BusinessException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by za-raozhikun on 2017/8/25.
 */
public class ImResultUtil {

    private static final String SUCCESSFUL_ACTION_STATUS = "OK";
    private static final int SUCCESSFUL_CODE = 0;

    private static final Map<Integer, String> ERROR_CODE_MAP = new ConcurrentHashMap<>();

    static {
        ERROR_CODE_MAP.put(10015, "群组ID非法，请检查群组ID是否填写正确");
        ERROR_CODE_MAP.put(70402, "参数非法。请检查必填字段是否填充，或者字段的填充是否满足协议要求");
        ERROR_CODE_MAP.put(70403, " 发起操作者不是APP管理员，没有权限操作");
        ERROR_CODE_MAP.put(70404, "设置简单资料后端超时");
        ERROR_CODE_MAP.put(10021, "群组ID已被使用，请选择其他的群组ID");
    }

    public static boolean isSuccessful(JSONObject retJson) {
        if (retJson == null) {
            return false;
        }
        String actionStatus = retJson.getString("ActionStatus");
        Integer errorCode = retJson.getInteger("ErrorCode");
        if (errorCode != null && errorCode == SUCCESSFUL_CODE && SUCCESSFUL_ACTION_STATUS.equals(actionStatus)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getErrorCode(JSONObject retJson) {
        return retJson.getInteger("ErrorCode");
    }

    public static BusinessException throwException(JSONObject retJson) {
        Integer errorCode = retJson.getInteger("ErrorCode");
        throw new BusinessException(String.valueOf(errorCode), ERROR_CODE_MAP.get(errorCode));
    }

}
