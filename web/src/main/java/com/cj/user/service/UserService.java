package com.cj.user.service;

import com.cj.user.dao.UserMapper;
import com.cj.user.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liuchunjiang on 2017/6/30.
 */

@Service
public class UserService {

    @Resource
    private UserMapper userMappero;

    public User getUserById(int userId) {
        return this.userMappero.selectByPrimaryKey(userId);
    }
}
