package com.eiaokiang.way.mapper;

import com.eiaokiang.way.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 17:37 2023/8/8
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> getAll();

    User getOne(Integer id);
}
