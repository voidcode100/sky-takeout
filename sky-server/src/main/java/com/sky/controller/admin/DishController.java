package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags="菜品管理")
public class DishController {
    @Autowired
    private DishService dishService;


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("添加菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        // 清除相关缓存
        Long categoryId = dishDTO.getCategoryId();
        String key = "dish_" + categoryId;
        cleanCache(key);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询菜品")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO)
    {
        log.info("分页查询菜品: {}", dishPageQueryDTO);
        PageResult pageResult=dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids)
    {
        log.info("删除菜品: {}",ids);
        dishService.delete(ids);

        // 清除相关缓存
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据ID查询菜品
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("根据ID查询菜品")
    public Result<DishVO> getById(@PathVariable Long id)
    {
        log.info("根据ID查询菜品: {}", id);
        DishVO dishVO = dishService.getByIdWhithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 更新菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新菜品")
    public Result update(@RequestBody DishDTO dishDTO)
    {
        log.info("更新菜品: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        // 清除相关缓存
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 启用或禁用菜品
     * @param status 菜品状态
     * @param id 菜品ID
     * @return 成功结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用菜品")
    public Result startOrStop(@PathVariable Integer status,Long id){
        log.info("菜品状态更新: status={}, id={}", status, id);
        dishService.startOrStop(status,id);

        // 清除相关缓存
        cleanCache("dish_*");
        return Result.success();
    }

    //清理缓存数据
    private void cleanCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
