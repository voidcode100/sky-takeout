package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes 套餐菜品列表
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐ID查询套餐菜品关系
     * @param setmealIds 套餐ID
     * @return 套餐菜品列表
     */
    void deleteBySetmealIds(List<Long> setmealIds);
}
