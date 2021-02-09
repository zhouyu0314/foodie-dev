package com.zy.service;

import com.zy.pojo.Items;
import com.zy.pojo.ItemsImg;
import com.zy.pojo.ItemsParam;
import com.zy.pojo.ItemsSpec;
import com.zy.pojo.vo.CommentLevelCountsVO;
import com.zy.pojo.vo.ShopcartVO;
import com.zy.utils.PagedGridResult;

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

    /**
     * 根据商品id查询商品的评价等级数量
     */
    CommentLevelCountsVO queryCommentCounts(String itemId) throws Exception;

    /**
     * 根据商品id和评价等级查询商品评价（分页）
     * @param itemId
     * @param commentLevel
     * @return
     * @throws Exception
     */
    PagedGridResult queryPagedComments(String itemId, Integer commentLevel, Integer page, Integer pageSize) throws Exception;


    /**
     * 搜索商品列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) throws Exception;

    /**
     * 根据分类id搜索商品列表
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) throws Exception;

    /**
     * 根据规格ids查询最新的购物车中商品数据
     * @param specIds
     * @return
     * @throws Exception
     */
    List<ShopcartVO> queryItemsBySpecIds(String specIds)throws Exception;


}
