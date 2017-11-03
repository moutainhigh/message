package com.zhongan.icare.message.im;

import com.zhongan.health.common.utils.security.crypt.impl.SymmetricCipher;
import com.zhongan.icare.message.im.service.ImGroupService;
import com.zhongan.icare.message.im.service.impl.ImGroupServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by za-raozhikun on 2017/8/16.
 */
@Configuration
@ComponentScan
public class ImConfiguration {

    @Bean
    @ConditionalOnMissingBean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    SymmetricCipher symmetricCipher() {
        SymmetricCipher symmetricCipher = new SymmetricCipher();
        symmetricCipher.setAlgorithm("AES");
        symmetricCipher.setSeed("");
        return symmetricCipher;
    }

}
 