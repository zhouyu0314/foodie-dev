package com.zy.service.impl;

import com.zy.mapper.CategoryMapper;
import com.zy.pojo.Category;
import com.zy.pojo.vo.CategoryVO;
import com.zy.pojo.vo.NewItemsVO;
import com.zy.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("type", 1);
        return categoryMapper.getCategoryListByMap(param);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) throws Exception {
        return categoryMapper.getSubCatList(rootCatId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("rootCatId", rootCatId);
        return categoryMapper.getSixNewItemsLazy(param);
    }
}
