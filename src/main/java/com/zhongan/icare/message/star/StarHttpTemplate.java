package com.zhongan.icare.message.star;

import com.zhongan.icare.common.http.HttpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 满天星HTTP访问封装
 */
@Component
public class StarHttpTemplate extends HttpTemplate {
    @Value("${sky.star.url}")
    private String skyStarUrl;

    @Override
    public String getRootUrl() {
        return skyStarUrl;
    }

}
