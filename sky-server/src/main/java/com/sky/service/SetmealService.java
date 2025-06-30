package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 新增套餐
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询数据传输对象
     * @return 分页结果
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 删除套餐
     * @param ids 套餐ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID查询套餐
     * @param id 套餐ID
     * @return 套餐视图对象
     */
    SetmealVO getById(Long id);

    /**
     * 更新套餐信息
     * @param setmealDTO 套餐数据传输对象
     */
    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * 启用或禁用套餐
     * @param status,id 分类ID
     * @return 套餐列表
     */
    void enableOrDisable(Integer status, Long id);
}
