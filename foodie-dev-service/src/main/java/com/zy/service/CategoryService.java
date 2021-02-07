package com.zy.service;

import com.zy.pojo.Category;
import com.zy.pojo.vo.CategoryVO;
import com.zy.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {
    /**
     * 查询所有一级分类
     *
     * @return
     */
    List<Category> queryAllRootLevelCat() throws Exception;

    /**
     * 根据一级分类id查询子分类信息
     *
     * @param rootCatId
     * @return
     * @throws Exception
     */
    List<CategoryVO> getSubCatList(Integer rootCatId) throws Exception;

    /**
     * 查询首页每个一级分类6条最新商品数据
     * @param rootCatId
     * @return
     * @throws Exception
     */
    List<NewItemsVO>  getSixNewItemsLazy(Integer rootCatId)throws Exception;

}
