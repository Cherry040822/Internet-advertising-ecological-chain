package com.example.mapper;

import com.example.entity.Advertisement;

import java.util.List;

public interface AdvertisementListMapper {
    void insert(Advertisement advertisement);

    List<Advertisement> selectAll(Advertisement advertisement);
}
