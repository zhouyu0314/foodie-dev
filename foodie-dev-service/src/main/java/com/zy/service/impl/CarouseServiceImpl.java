package com.zy.service.impl;

import com.zy.entity.Carousel;
import com.zy.mapper.CarouselMapper;
import com.zy.service.CarouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarouseServiceImpl implements CarouseService {
    @Autowired(required = false)
    private CarouselMapper carouselMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(Integer isShow)throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("isShow",isShow);
        return carouselMapper.getCarouselListByMap(param);
    }
}
