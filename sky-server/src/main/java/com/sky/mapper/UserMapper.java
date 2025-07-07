package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * 插入新用户
     * @param user 用户实体
     */
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) " +
            "values (#{openid},#{name}, #{phone},#{sex},#{idNumber},#{avatar}, #{createTime})")
    void insert(User user);

    /**
     * 根据ID查询用户信息
     * @param id 用户ID
     * @return 用户实体
     */
    @Select("select * from user where id = #{id}")
    User getById(Long id);
}
