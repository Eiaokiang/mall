package com.eiaokiang.way.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: Eiaokiang
 * @Description:
 * @Date: Created in 18:04 2023/8/8
 */
@Data
public class Way {
    @TableId(type = IdType.AUTO)
    private Integer Id;
    @TableField(value = "desc")
    private String Desc;

}
