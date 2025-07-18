package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "套餐相关接口")
@Slf4j
@RequestMapping("admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐数据传输对象
     * @return 成功结果
     */
    @CacheEvict(cacheNames = "setmealCache",key="#{setmealDTO.categoryId}")
    @PostMapping
    @ApiOperation("新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐: {}",setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();

    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageQueryDTO 套餐数据传输对象
     * @return 分页结果
     */
    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除套餐
     *
     * @param ids 套餐ID列表
     * @return 成功结果
     */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @DeleteMapping
    @ApiOperation("删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除套餐: {}", ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据ID查询套餐
     *
     * @param id 套餐ID
     * @return 套餐视图对象
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("根据ID查询套餐: {}",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 更新套餐
     *
     * @param setmealDTO 套餐数据传输对象
     * @return 成功结果
     */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @PutMapping
    @ApiOperation("更新套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("更新套餐: {}", setmealDTO);
        setmealService.updateWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 启用或禁用套餐
     * @param status 套餐状态
     * @param id 套餐ID
     * @return 成功结果
     */
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    @PostMapping("/status/{status}")
    @ApiOperation("启用或禁用套餐")
    public Result enableOrDisable(@PathVariable Integer status,Long id) {
        log.info("启用或禁用套餐: id={}, status={}", id, status);
        setmealService.enableOrDisable(status,id);
        return Result.success();
    }
}


