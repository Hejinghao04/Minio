package cn.itcast.demo.minio.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("satellite")
public class Satellite {
    @TableId
    private Long id;

    private String sateName;

    private Long userId;

    private Long epId;
}
