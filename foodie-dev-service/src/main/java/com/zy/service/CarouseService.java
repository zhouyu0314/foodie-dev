package com.zy.service;

import com.zy.entity.Carousel;

import java.util.List;

public interface CarouseService {
    List<Carousel> queryAll(Integer isShow)throws Exception;
}
