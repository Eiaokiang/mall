package com.eiaokiang.way.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eiaokiang.way.mapper.UserMapper;
import com.eiaokiang.way.pojo.User;
import com.eiaokiang.way.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 14:43 2023/8/10
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
