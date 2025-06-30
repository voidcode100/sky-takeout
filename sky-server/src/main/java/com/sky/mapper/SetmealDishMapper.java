package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes 套餐菜品列表
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐ID批量删除套餐菜品关系
     * @param setmealIds 套餐ID
     * @return 套餐菜品列表
     */
    void deleteBySetmealIds(List<Long> setmealIds);

    /**
     * 根据套餐ID查询套餐菜品关系
     * @param setmalId 套餐ID
     * @return 套餐菜品列表
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmalId}")
    List<SetmealDish> selectBySetmealId(Long setmalId);

    /**
     * 根据套餐ID删除套餐菜品关系
     * @param id 套餐ID
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);


}
