package com.sileyouhe.mallportal.service;

import com.sileyouhe.mallportal.common.api.CommonResult;
import com.sileyouhe.mallportal.dto.OrderParam;
import org.springframework.transaction.annotation.Transactional;

public interface OmsPortalOrderService {

    @Transactional
    CommonResult generateOrder(OrderParam orderParam);

    @Transactional
    void cancelOrder(Long orderId);
}
