package com.sileyouhe.mallportal.controller;

import com.sileyouhe.mallportal.common.api.CommonPage;
import com.sileyouhe.mallportal.common.api.CommonResult;
import com.sileyouhe.mallportal.nosql.mongodb.document.MemberReadHistory;
import com.sileyouhe.mallportal.service.MemberReadHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "MemberReadHistoryController", description = "Member browsing history management")
@RequestMapping("/member/readHistory")
public class MemberReadHistoryController {

    @Autowired
    private MemberReadHistoryService memberReadHistoryService;

    @ApiOperation("create browsing history")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody MemberReadHistory memberReadHistory){
        int count = memberReadHistoryService.create(memberReadHistory);
        if (count > 0){
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }

    }

    @ApiOperation("delete browsing history by id")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("ids") List<String> ids){
        int count = memberReadHistoryService.delete(ids);
        if (count > 0){
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }


    @ApiOperation("list browsing history by member Id")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<MemberReadHistory>> list(Long memberId){
        List<MemberReadHistory> memberReadHistoryList = memberReadHistoryService.list(memberId);
        return CommonResult.success(CommonPage.restPage(memberReadHistoryList));
    }



}
