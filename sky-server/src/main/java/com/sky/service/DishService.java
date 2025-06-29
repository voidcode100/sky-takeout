package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.stereotype.Service;

public interface DishService {
    /**
     * 添加菜品
     * @param dishDTO 菜品数据传输对象
     */
    void saveWithFlavor(DishDTO dishDTO);
}
