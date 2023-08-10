package com.eiaokiang.way.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.eiaokiang.way.pojo.User;
import com.eiaokiang.way.util.RedisUtil;
import com.eiaokiang.way.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.setContentType("application/json;charset=UTF-8");

        String token = request.getHeader("token");
        ModelMap result;
        if (StrUtil.isEmpty(token)) {
            result = ReturnUtil.Error("token 不存在");
        } else {
            String uId = null;
            try {
                uId = JWTUtil.parseToken(token).getPayload("uId").toString();
            } catch (Exception e) {
                result = ReturnUtil.Error("token 不存在");
                response.getWriter().print(JSONUtil.toJsonStr(result));
                return false;
            }


            String key = "token:" + uId;
            Object value = redisUtil.getValue(key);
            if (value != null) {
                User user = JSONUtil.toBean((String) value, User.class);
                //token小于5分钟  刷新token
                if (user.getExpireTime() - System.currentTimeMillis() <= 5 * 60 * 1000) {
                    user.setExpireTime(System.currentTimeMillis() + tokenExpireTime * 60 * 1000);
                    redisUtil.saveValue(key, user, tokenExpireTime, TimeUnit.MINUTES);
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
