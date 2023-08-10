package com.eiaokiang.way.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 16:47 2023/8/8
 */
@Data
public class User {

    @TableId(type = IdType.AUTO)
    private Integer Id;

    private String Name;

    private String Account;

    private String Password;

    @TableField(value = "login_time")
    private Long LoginTime;

    @TableField(value = "expire_time")
    private Long ExpireTime;


}
