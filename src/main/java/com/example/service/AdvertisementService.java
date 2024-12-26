package com.example.service;

import com.example.entity.Advertisement;
import com.example.mapper.AdvertisementMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {

    @Resource
    AdvertisementMapper advertisementMapper;


    public PageInfo<Advertisement> selectPage(Advertisement advertisement, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Advertisement> list=advertisementMapper.selectAll(advertisement);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        advertisementMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id :ids){
            this.deleteById(id);
        }
    }
}
