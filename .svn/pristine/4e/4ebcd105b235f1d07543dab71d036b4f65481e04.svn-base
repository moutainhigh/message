package com.zhongan.icare.message.im.web;

import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.icare.message.im.bean.ImAccount;
import com.zhongan.icare.message.im.bean.ImGroup;
import com.zhongan.icare.message.im.service.ImOrgGroupService;
import com.zhongan.icare.share.common.constants.ErrorCode;
import com.zhongan.icare.share.common.dto.PageDTO;
import com.zhongan.icare.share.customer.dto.CustCustomerDTO;
import com.zhongan.icare.share.customer.enm.CustTypeEnum;
import com.zhongan.icare.share.customer.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 帐号相关api
 * Created by za-raozhikun on 2017/8/14.
 */
@Slf4j
@RestController
@RequestMapping(path = "/v1/im/orgs")
public class ImOrgController {

    private final ExecutorThreadPool executorService = new ExecutorThreadPool(1, 20, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000));

    @Resource
    private ICustomerService customerService;

    @Resource
    private ImOrgGroupService imOrgGroupService;

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResult<Void> addOrg(@RequestBody ImGroup imGroup) {
        imOrgGroupService.createOrgImGroup(imGroup.getId(), imGroup.getName(), imGroup.getFaceUrl());
        return ExceptionUtils.success(null);
    }

    @RequestMapping(path = "{orgId}/member/{custId}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResult<Void> addOrgMember(@PathVariable String orgId, @PathVariable String custId) {
        imOrgGroupService.addOrgImGroupMember(orgId, custId, null);
        return ExceptionUtils.success(null);
    }

    @RequestMapping(path = "sync", method = RequestMethod.POST)
    public BaseResult<String> syncAllOrg() {
        executorService.execute(() -> {
            PageDTO<CustCustomerDTO> pageInfo = new PageDTO<>();
            pageInfo.setPageSize(100);
            CustCustomerDTO custDto = new CustCustomerDTO();
            custDto.setIsDeleted(YesOrNo.NO);
            pageInfo.setParam(custDto);
            try {
                for (int currentPage = 1; ; currentPage++) {
                    pageInfo.setCurrentPage(currentPage);
                    PageDTO<CustCustomerDTO> pageDTO = customerService.queryCustomerByCondPage(pageInfo);
                    if (pageDTO.getResultList() == null || pageDTO.getResultList().isEmpty()) {
                        break;
                    } else {
                        for (final CustCustomerDTO custCustomerDTO : pageDTO.getResultList()) {
                            if (custCustomerDTO.getCustType() != CustTypeEnum.PERSON && custCustomerDTO.getCustType() != CustTypeEnum.VIRTUAL) {
                                syncCustCustomer(custCustomerDTO);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Sync All Im Orgs Filed : msg={}", e.getMessage());
            }
        });
        return ExceptionUtils.success("后台执行中");
    }

    private void syncCustCustomer(final CustCustomerDTO custCustomerDTO) throws InterruptedException {
        try {
            executorService.execute(() -> {
                ImAccount imAccount = new ImAccount();
                try {
                    imOrgGroupService.createOrgImGroup(String.valueOf(custCustomerDTO.getId()), StringUtils.hasText(custCustomerDTO.getNickName()) ? custCustomerDTO.getNickName() : custCustomerDTO.getName(), custCustomerDTO.getCustIcon());
                } catch (Exception e) {
                    log.warn("Sync All Im Orgs Filed : account={}, msg={}", imAccount, e.getMessage(), e);
                }
            });
        } catch (RejectedExecutionException e) {
            Thread.sleep(1000);
            syncCustCustomer(custCustomerDTO);
        }
    }

    @ExceptionHandler
    public BaseResult<String> catchExceptions(Exception e) {
        log.warn("Im Org Filed : msg={}", e.getMessage(), e);
        BaseResult<String> result = new BaseResult<>();
        ExceptionUtils.setErrorInfo(result, e);
        return ExceptionUtils.fail(ErrorCode.CMN_CREATE_ERR, e.getMessage());
    }

}


