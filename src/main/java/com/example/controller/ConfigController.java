package com.example.controller;

import com.example.common.Result;
import com.example.entity.Config;
import com.example.service.ConfigService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    private ConfigService configService;

    @GetMapping("/select")
    public Result select() {
        Config config=configService.select();
        System.out.println(config.toString());
        return Result.success(config);
   }

    @PutMapping("/update")
    public Result update(@RequestBody Config config){
        configService.update(config);
        return Result.success();
    }
}
