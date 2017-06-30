package com.cj.user.dao;


import com.cj.user.entity.User;
import org.apache.ibatis.annotations.Select;


/**
 * Created by liuchunjiang on 2017/5/9.
 */

public interface  UserMapper {
    @Select("SELECT * FROM user_t WHERE id = #{id}")
    User selectByPrimaryKey(int id);
}
