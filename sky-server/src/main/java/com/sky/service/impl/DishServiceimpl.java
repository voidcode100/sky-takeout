package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceimpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 添加菜品
     * @param dishDTO 菜品数据传输对象
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //向菜品插入一条数据
        dishMapper.insert(dish);
        Long dishId=dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&& flavors.size()>0) {
            for(DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            //向口味表插入多条数据
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除菜品
     * @param ids 菜品id集合
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //判断菜品售卖状态
        for (Long id : ids) {
            Dish dish=dishMapper.selectById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //判断菜品是否与套餐相关联
        List<Long> selectSetmealIdsbyDishIds = setmealMapper.selectSetmealIdsbyDishIds(ids);
        if(selectSetmealIdsbyDishIds!=null && selectSetmealIdsbyDishIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //批量删除菜品,同时删除该菜品关联口味
//        for (Long id:ids) {
//            dishMapper.deleteById(id);
//            dishFlavorMapper.deleteByDishId(id);
//
//        }

        //sql批量删除优化
        //批量删除菜品
        dishMapper.deleteByIds(ids);
        //批量删除菜品口味
        dishFlavorMapper.deleteBydishIds(ids);

    }


}
