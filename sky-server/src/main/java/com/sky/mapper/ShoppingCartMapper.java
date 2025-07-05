package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    /**
     * 根据购物车dishId,setmeaalId,flavor,userId查询购物车列表
     *
     * @param shoppingCart 购物车信息
     * @return 购物车列表
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据购物车id更新数量
     *
     * @param shoppingCart 购物车信息
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入一条购物车记录
     *
     * @param shoppingCart 购物车信息
     */
    @Insert("insert into  shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) \n" +
            "        values (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}, create_time)")
    void insert(ShoppingCart shoppingCart);
}
