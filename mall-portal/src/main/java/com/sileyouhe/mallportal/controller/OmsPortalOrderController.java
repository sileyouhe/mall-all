package com.sileyouhe.mallportal.controller;

import com.sileyouhe.mallportal.dto.OrderParam;
import com.sileyouhe.mallportal.service.OmsPortalOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "OmsPortalOrderController", description = "order manage")
@RequestMapping("/order")
public class OmsPortalOrderController {

    @Autowired
    private OmsPortalOrderService portalOrderService;

    @ApiOperation("generate order based on shopping cart")
    @RequestMapping(value = "/generateOrder", method = RequestMethod.POST)
    @ResponseBody
    public Object generateOrder(@RequestBody OrderParam orderParam) {
        return portalOrderService.generateOrder(orderParam);
    }
}
