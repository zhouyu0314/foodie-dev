package com.zy.service.base;

import com.github.pagehelper.PageInfo;
import com.zy.utils.PagedGridResult;

import java.util.List;

public class BaseServiceImpl {
    protected PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
