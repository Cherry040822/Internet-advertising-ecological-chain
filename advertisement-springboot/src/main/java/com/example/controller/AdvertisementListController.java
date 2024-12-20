package com.example.controller;

import com.example.entity.Advertisement;
import com.example.service.AdvertisementService;
import com.example.common.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/advertisementList")
public class AdvertisementListController {

    @Resource
    private AdvertisementService advertisementService;

    @PostMapping("/add")
    public Result save(@RequestBody Advertisement advertisement) {
        try {
            advertisementService.add(advertisement);
            return Result.success();
        } catch (Exception e) {
            return Result.error("500","保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/selectPage")
    public Result selectPage(Advertisement advertisement,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "3") Integer pageSize) {
        PageInfo<Advertisement> pageinfo=advertisementService.selectPage(advertisement,pageNum,pageSize);
        return Result.success(pageinfo);
    }
}