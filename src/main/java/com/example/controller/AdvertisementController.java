package com.example.controller;

import com.example.entity.Advertisement;
import com.example.service.AdvertisementService;
import com.example.common.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

import java.util.List;

@RestController
@RequestMapping("/advertisement")
public class AdvertisementController {

    @Resource
    private AdvertisementService advertisementService;

    @GetMapping("/selectPage")
    public Result selectPage(Advertisement advertisement,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "3") Integer pageSize) {
        PageInfo<Advertisement> pageinfo=advertisementService.selectPage(advertisement,pageNum,pageSize);
        return Result.success(pageinfo);
    }
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        advertisementService.deleteById(id);
        return Result.success();
    }
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        advertisementService.deleteBatch(ids);
        return Result.success();
    }
}