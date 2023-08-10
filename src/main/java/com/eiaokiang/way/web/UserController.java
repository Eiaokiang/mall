package com.eiaokiang.way.web;

import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eiaokiang.way.pojo.User;
import com.eiaokiang.way.service.IUserService;
import com.eiaokiang.way.util.RedisUtil;
import com.eiaokiang.way.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 10:35 2023/8/10
 */
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
                //正确则在redis添加用户信息和过期时间并返回token
                long now = System.currentTimeMillis();
                user.setExpireTime(now + tokenExpireTime * 60 * 1000);
                user.setLoginTime(now);
                //生成token
                String uid = user.getId().toString();
                String token = JWT.create().setPayload("uId", uid).setKey(tokenKey.getBytes()).sign();
                System.out.println(token);
                redisUtil.saveValue("token:" + uid, JSONUtil.toJsonStr(user), tokenExpireTime, TimeUnit.MINUTES);
                //todo mq 更新数据库用户登录信息

                return ReturnUtil.Success(new HashMap<>().put("token", token));

            } else {
                //验证失败
                return ReturnUtil.Error("账户验证失败");
            }
        } catch (Exception e) {
            return ReturnUtil.Error("内部错误");
        }
    }


}
