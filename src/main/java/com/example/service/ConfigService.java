package com.example.service;

import com.example.entity.Config;
import com.example.mapper.ConfigMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    @Resource
    ConfigMapper configMapper;

    public Config select() {
        return configMapper.select();
    }
    public void update(Config config) {
        configMapper.update(config);
    }

}
