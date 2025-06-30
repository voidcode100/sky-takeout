package com.sky.mapper;

import com.sky.annotiation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
}
