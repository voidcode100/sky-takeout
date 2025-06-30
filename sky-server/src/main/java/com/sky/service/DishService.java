package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DishService {
    /**
     * 添加菜品
     * @param dishDTO 菜品数据传输对象
     */
    void saveWithFlavor(DishDTO dishDTO);


    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 删除菜品
     * @param ids 菜品ID列表
     */
    void delete(List<Long> ids);

    /**
     * 根据ID查询菜品
     * @param id 菜品ID
     * @return 菜品视图对象
     */
    DishVO getByIdWhithFlavor(Long id);

    /**
     * 更新菜品信息
     * @param dishDTO 菜品数据传输对象
     */
    void updateWithFlavor(DishDTO dishDTO);
}
