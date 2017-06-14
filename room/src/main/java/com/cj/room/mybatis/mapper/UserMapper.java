package com.cj.room.mybatis.mapper;


import com.cj.room.bean.User;
import org.apache.ibatis.annotations.Select;


/**
 * Created by liuchunjiang on 2017/5/9.
 */

public interface  UserMapper {
    @Select("SELECT * FROM user_t WHERE id = #{id}")
    User selectByPrimaryKey(int id);
}
