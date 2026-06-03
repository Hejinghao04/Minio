package cn.itcast.demo.minio.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("enterprise")
public class EnterPrise {
    private Long id;

    private String epName;

    private LocalDateTime loginTime;

    private Long numSate;

    private String enterKey;

}
