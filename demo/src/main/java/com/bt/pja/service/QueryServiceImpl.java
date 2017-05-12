package com.bt.pja.service;


import com.bt.pja.QueryService;
import com.bt.pja.bean.User;
import com.bt.pja.dao.UserMapper;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * Created by liuchunjiang on 2017/5/11.
 */
@Service
public class QueryServiceImpl implements QueryService {
    @Resource
    private UserMapper userMappero;

    @Override
    public String queryUserName(int id) {
        User user = this.userMappero.selectByPrimaryKey(id);
        if (user == null) return  "";
        return user.getUserName();
    }
}
