package com.bt.pja.dao;

import com.bt.pja.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.context.annotation.Bean;


/**
 * Created by liuchunjiang on 2017/5/9.
 */

public interface  UserMapper {
    @Select("SELECT * FROM user_t WHERE id = #{id}")
    User selectByPrimaryKey(int id);
}
