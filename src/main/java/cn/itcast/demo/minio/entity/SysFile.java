package cn.itcast.demo.minio.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_file")
public class SysFile {
    @TableId
    private Long id;

    private String fileName;

    private String objectName;

    @TableField("bucket_name")
    private String bucketName;

    private Long size;

    private String contentType;

    private Long userId;

    private Long sateId;

    private String dldUrl;

    private String yuanFilename;

    @TableField("create_time")
    private LocalDateTime createTime;
}
