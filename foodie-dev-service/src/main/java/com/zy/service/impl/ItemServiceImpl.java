package com.zy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zy.enums.CommentLevel;
import com.zy.mapper.*;
import com.zy.pojo.Items;
import com.zy.pojo.ItemsImg;
import com.zy.pojo.ItemsParam;
import com.zy.pojo.ItemsSpec;
import com.zy.pojo.vo.CommentLevelCountsVO;
import com.zy.pojo.vo.ItemCommentVO;
import com.zy.pojo.vo.SearchItemsVO;
import com.zy.pojo.vo.ShopcartVO;
import com.zy.service.ItemService;
import com.zy.utils.DesensitizationUtil;
import com.zy.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired(required = false)
    private ItemsCommentsMapper itemsCommentsMapper;

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) throws Exception {
        Integer goodCounts = this.getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = this.getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = this.getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;
        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        commentLevelCountsVO.setTotalCounts(totalCounts);
        commentLevelCountsVO.setGoodCounts(goodCounts);
        commentLevelCountsVO.setNormalCounts(normalCounts);
        commentLevelCountsVO.setBadCounts(badCounts);
        return commentLevelCountsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer commentLevel, Integer page, Integer pageSize) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("itemId", itemId);
        param.put("commentLevel", commentLevel);
        /*
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> list = itemsCommentsMapper.queryItemComments(param);
        list.stream().forEach(node -> node.setNickname(DesensitizationUtil.commonDisplay(node.getNickname())));
        PagedGridResult pagedGridResult = this.setterPagedGrid(list, page);
        return pagedGridResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("keywords", keywords);
        param.put("sort", sort);
        /*
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list = itemsMapper.searchItems(param);
        return this.setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("catId", catId);
        param.put("sort", sort);
        /*
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list = itemsMapper.searchItemsByThirdCat(param);
        return this.setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) throws Exception {
        String[] ids = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        //ids里的元素全部加入到specIdsList
        Collections.addAll(specIdsList,ids);
        return itemsSpecMapper.queryItemsBySpecIds(specIdsList);
    }


    //*****************************************************************************
    private Integer getCommentCounts(String itemId, Integer level) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("itemId", itemId);
        param.put("commentLevel", level);
        return itemsCommentsMapper.getItemsCommentsCountByMap(param);
    }

    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
