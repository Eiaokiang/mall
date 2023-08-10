package com.eiaokiang.way.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.eiaokiang.way.pojo.User;
import com.eiaokiang.way.service.IUserService;
import com.eiaokiang.way.util.RedisUtil;
import com.eiaokiang.way.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 13:55 2023/8/10
 */
public class JWTInterceptor implements HandlerInterceptor {

    @Autowired
    RedisUtil redisUtil;


    @Value("${token.expireTime}")
    Integer tokenExpireTime;

    @Autowired
    IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setContentType("application/json;charset=UTF-8");

        String token = request.getHeader("token");
        ModelMap result;
        if (StrUtil.isEmpty(token)) {
            result = ReturnUtil.Error("token 不存在");
        } else {
            Integer uId = null;
            try {
                uId = Integer.parseInt(JWTUtil.parseToken(token).getPayload("uId").toString());
            } catch (Exception e) {
                result = ReturnUtil.Error("token 不存在");
                response.getWriter().print(JSONUtil.toJsonStr(result));
                return false;
            }

            User user = userService.getById(uId);

            if (user != null && user.getExpireTime() >= System.currentTimeMillis()) {
                //token小于5分钟  刷新token
                if (user.getExpireTime() - System.currentTimeMillis() <= 5 * 60 * 1000) {
                    user.setExpireTime(System.currentTimeMillis() + tokenExpireTime * 60 * 1000);
                    userService.updateById(user);
                }
                return true;
            } else {
                result = ReturnUtil.Error("token 已过期");
            }
        }

        response.getWriter().print(JSONUtil.toJsonStr(result));

        return false;
    }
}
