package com.example.mapper;

import com.example.entity.Config;

public interface ConfigMapper {
    Config select();

    void update(Config config);
}
