package com.example.mapper;

import com.example.entity.Advertisement;

import java.util.List;

public interface AdvertisementMapper {

    List<Advertisement> selectAll(Advertisement advertisement);

    void deleteById(Integer id);
}
