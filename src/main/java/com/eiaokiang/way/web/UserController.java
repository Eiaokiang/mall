package com.eiaokiang.way.web;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eiaokiang.way.pojo.User;
import com.eiaokiang.way.service.IUserService;
import com.eiaokiang.way.util.RedisUtil;
import com.eiaokiang.way.util.ReturnUtil;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.MARSHAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 10:35 2023/8/10
 */
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    @Value("${token.key}")
    String tokenKey;

    @Value("${token.expireTime}")
    Integer tokenExpireTime;

    @Autowired
    IUserService userService;

    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("login")
    public ModelMap login(@RequestParam("account") String account, @RequestParam("password") String password) {
        try {
            //数据库查询
            QueryWrapper<User> q = new QueryWrapper<User>().eq("account", account).eq("password", password);
            User user = userService.getOne(q);
            if (user != null) {
                //正确则在redis或数据库添加用户信息和过期时间并返回token
                long now = System.currentTimeMillis();
                user.setExpireTime(now + tokenExpireTime * 60 * 1000);
                user.setLoginTime(now);
                //更新数据库用户登录信息
                userService.updateById(user);
                //生成token
                String token = JWT.create().setPayload("uId", user.getId()).setKey(tokenKey.getBytes()).sign();

                HashMap<String, Object> map = new HashMap<>();
                map.put("token", token);
                return ReturnUtil.Success(map);

            } else {
                //验证失败
                return ReturnUtil.Error("账户验证失败");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnUtil.Error("内部错误");
        }
    }


}
