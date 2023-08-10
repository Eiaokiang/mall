

package com.eiaokiang.way.util;

import org.springframework.ui.ModelMap;

/**
 * 项目内调用返回工具
 */

public class ReturnUtil {


    public static ModelMap Success(Object obj) {
        ModelMap mp = new ModelMap();
        mp.put("return_code", "SUCCESS");
        mp.put("return_msg", obj);
        return mp;
    }


    public static ModelMap Error(Object obj) {
        ModelMap mp = new ModelMap();
        mp.put("return_code", "FAIL");
        mp.put("return_msg", obj);
        return mp;
    }


}
