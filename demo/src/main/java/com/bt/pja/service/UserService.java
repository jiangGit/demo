package com.bt.pja.service;


import com.bt.pja.bean.User;
import com.bt.pja.dao.UserMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * Created by liuchunjiang on 2017/5/9.
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMappero;

    public User getUserById(int userId) {
        // TODO Auto-generated method stub
        return this.userMappero.selectByPrimaryKey(userId);
    }
}
