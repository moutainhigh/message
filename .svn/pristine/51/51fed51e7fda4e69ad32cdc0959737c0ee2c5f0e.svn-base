package com.zhongan.icare.message.im.service;

import com.zhongan.icare.message.im.web.dto.ImLogDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "icare-message")
@RequestMapping(path = "/int/v1/imLog")
public interface IImLogService {
    @RequestMapping(path = "create",method={RequestMethod.POST})
    long create(@RequestBody ImLogDTO dto);

    @RequestMapping(path = "list",method={RequestMethod.POST})
    List<ImLogDTO> list(@RequestBody ImLogDTO dto);

    @RequestMapping(path = "count",method={RequestMethod.POST})
    int count(@RequestBody ImLogDTO dto);

    @RequestMapping(path = "createBatch",method={RequestMethod.POST})
    void createBatch(@RequestBody List<ImLogDTO> imLogDTOList);
}