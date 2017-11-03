package com.zhongan.icare.message.im.web;

import com.zhongan.health.common.share.bean.BaseResult;
import com.zhongan.health.common.share.enm.YesOrNo;
import com.zhongan.health.common.share.exception.ExceptionUtils;
import com.zhongan.icare.message.im.bean.ImAccount;
import com.zhongan.icare.message.im.service.ImAccountService;
import com.zhongan.icare.message.im.service.ImOrgGroupService;
import com.zhongan.icare.share.common.constants.ErrorCode;
import com.zhongan.icare.share.common.dto.PageDTO;
import com.zhongan.icare.share.customer.dto.CustCustomerDTO;
import com.zhongan.icare.share.customer.enm.CustTypeEnum;
import com.zhongan.icare.share.customer.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.springframework.http.MediaType;
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
@RequestMapping(path = "/v1/im/accounts")
public class ImAccountController {

    private final ExecutorThreadPool executorService = new ExecutorThreadPool(1, 20, 10, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(1000));

    @Resource
    private ImAccountService imAccountService;

    @Resource
    private ICustomerService customerService;

    @Resource
    private ImOrgGroupService imOrgGroupService;

    @RequestMapping(path = "{accountId}/encryption")
    public BaseResult<String> addAccount(@PathVariable String accountId) {
        return ExceptionUtils.success(imAccountService.encryptAccountId(accountId));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public BaseResult<ImAccount> addAccount(@RequestBody ImAccount addImAccountRequest) {
        String custId = addImAccountRequest.getId();
        ImAccount imAccount = imAccountService.addImAccount(addImAccountRequest);
        if (imAccount != null) {
            CustCustomerDTO customerDTO = customerService.queryCustomerByCustId(Long.valueOf(custId));
            if (customerDTO.getParentCustId() != null && customerDTO.getParentCustId() != 0 && customerDTO.getCustType() == CustTypeEnum.PERSON) {
                imOrgGroupService.addOrgImGroupMember(String.valueOf(customerDTO.getParentCustId()), imAccount.getId(), customerDTO.getName());
            }
        }
        return ExceptionUtils.success(imAccount);
    }

    @RequestMapping(path = "sync", method = RequestMethod.POST)
    public BaseResult<String> syncAllAccount() {
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
                            syncCustCustomer(custCustomerDTO);
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Sync All Im Accounts Filed : msg={}", e.getMessage(), e);
            }
        });
        return ExceptionUtils.success("后台执行中");
    }

    private void syncCustCustomer(final CustCustomerDTO custCustomerDTO) throws InterruptedException {
        try {
            executorService.execute(() -> {
                ImAccount imAccount = new ImAccount();
                try {
                    long id = custCustomerDTO.getId();
                    String name = custCustomerDTO.getName();
                    String custIcon = custCustomerDTO.getCustIcon();
                    imAccount.setId(String.valueOf(id));
                    imAccount.setNickName(name == null ? "" : name);
                    imAccount.setFaceUrl(custIcon == null || NumberUtils.isNumber(custIcon) ? "" : custIcon);
                    imAccountService.addImAccount(imAccount);
                    if (custCustomerDTO.getParentCustId() != null && custCustomerDTO.getParentCustId() != 0 && custCustomerDTO.getCustType() == CustTypeEnum.PERSON) {
                        imOrgGroupService.addOrgImGroupMember(String.valueOf(custCustomerDTO.getParentCustId()), String.valueOf(id), name);
                    }
                } catch (Exception e) {
                    log.warn("Sync All Im Accounts Filed : account={}, msg={}", imAccount, e.getMessage(), e);
                }
            });
        } catch (RejectedExecutionException e) {
            Thread.sleep(1000);
            syncCustCustomer(custCustomerDTO);
        }
    }

    @ExceptionHandler
    public BaseResult<String> catchExceptions(Exception e) {
        log.warn("Im Account Filed : msg={}", e.getMessage(), e);
        BaseResult<String> result = new BaseResult<>();
        ExceptionUtils.setErrorInfo(result, e);
        return ExceptionUtils.fail(ErrorCode.CMN_CREATE_ERR, e.getMessage());
    }

}


