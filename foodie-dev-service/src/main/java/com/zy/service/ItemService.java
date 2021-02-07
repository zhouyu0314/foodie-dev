package com.zy.service;

import com.zy.pojo.Items;
import com.zy.pojo.ItemsImg;
import com.zy.pojo.ItemsParam;
import com.zy.pojo.ItemsSpec;

import java.util.List;

public interface ItemService {
    /**
     * 根据商品id查询详情
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    Items queryItemById(String itemId) throws Exception;

    /**
     * 根据商品id查询上篇图片列表
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    List<ItemsImg> queryItemImgList(String itemId) throws Exception;

    /**
     * 根据商品id查询商品规格
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    List<ItemsSpec> queryItemApecList(String itemId) throws Exception;

    /**
     * 根据商品id查询商品参数
     *
     * @param itemId
     * @return
     * @throws Exception
     */
    List<ItemsParam> queryItemsParam(String itemId) throws Exception;
}
