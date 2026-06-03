package cn.itcast.demo.minio.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.INPUT)
    private Long id;

    private String username;

    @TableField("userPass")
    private String userPass;

    @TableField("ep_id")
    private Long ePId;
}
