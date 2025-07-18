package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotiation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 根据菜品id查询关联套餐
     * @param ids
     * @return
     */
    List<Long> selectSetmealIdsbyDishIds(List<Long> ids);

    /**
     * 插入一条套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into setmeal (category_id, name, price, description, image, create_time, update_time, create_user, update_user) " +
            "values (#{categoryId}, #{name}, #{price}, #{description}, #{image}, #{createTime},#{updateTime}, #{createUser}, #{updateUser})")
    void insert(Setmeal setmeal);

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据ID查询套餐
     * @param id 套餐ID
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal selectById(Long id);

    /**
     * 批量ID删除套餐
     * @param ids 套餐ID
     */
    void deleteByIds(List<Long> ids);

    /**
     * 更新套餐信息
     * @param setmeal 套餐实体
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);


    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
