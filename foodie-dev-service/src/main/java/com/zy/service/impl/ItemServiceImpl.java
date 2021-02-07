package com.zy.service.impl;

import com.zy.mapper.ItemsImgMapper;
import com.zy.mapper.ItemsMapper;
import com.zy.mapper.ItemsParamMapper;
import com.zy.mapper.ItemsSpecMapper;
import com.zy.pojo.Items;
import com.zy.pojo.ItemsImg;
import com.zy.pojo.ItemsParam;
import com.zy.pojo.ItemsSpec;
import com.zy.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired(required = false)
    private ItemsMapper itemsMapper;

    @Autowired(required = false)
    private ItemsImgMapper itemsImgMapper;

    @Autowired(required = false)
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired(required = false)
    private ItemsParamMapper itemsParamMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) throws Exception {
        return itemsMapper.getItemsById(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("itemId", itemId);
        return itemsImgMapper.getItemsImgListByMap(param);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemApecList(String itemId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("itemId", itemId);
        return itemsSpecMapper.getItemsSpecListByMap(param);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsParam> queryItemsParam(String itemId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("itemId", itemId);
        return itemsParamMapper.getItemsParamListByMap(param);
    }
}
