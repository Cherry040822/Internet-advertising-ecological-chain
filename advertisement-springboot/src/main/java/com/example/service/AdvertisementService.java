package com.example.service;

import com.example.entity.Advertisement;
import com.example.mapper.AdvertisementListMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AdvertisementService {

    @Resource
    AdvertisementListMapper advertisementListMapper;

    public void add(Advertisement advertisement) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        advertisement.setCreateTime(sdf.format(new Date()));
        advertisementListMapper.insert(advertisement);
    }

    public PageInfo<Advertisement> selectPage(Advertisement advertisement, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Advertisement> list=advertisementListMapper.selectAll(advertisement);
        return PageInfo.of(list);
    }
}
