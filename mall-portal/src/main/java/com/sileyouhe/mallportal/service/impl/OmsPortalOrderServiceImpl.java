package com.sileyouhe.mallportal.service.impl;

import com.sileyouhe.mallportal.common.api.CommonResult;
import com.sileyouhe.mallportal.component.CancelOrderSender;
import com.sileyouhe.mallportal.dto.OrderParam;
import com.sileyouhe.mallportal.service.OmsPortalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    private static Logger LOGGER = LoggerFactory.getLogger(OmsPortalOrderServiceImpl.class);

    @Autowired
    private CancelOrderSender cancelOrderSender;

    @Override
    public CommonResult generateOrder(OrderParam orderParam) {
        // to do: place order
        LOGGER.info("process generateOrder");

        // order placed, send a delay message to cancel order when timeout
        sendDelayMessageCancelOrder(11L);


        return CommonResult.success(null, "order placed, thanks!");
    }

    @Override
    public void cancelOrder(Long orderId) {
        // to do: cancelOrder


        LOGGER.info("process cancelOrder orderId:{}",orderId);

    }

    private void sendDelayMessageCancelOrder(Long orderId){
        // delay times, set 30s here
        long delayTimes = 30 * 1000;
        // send message
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }
}
