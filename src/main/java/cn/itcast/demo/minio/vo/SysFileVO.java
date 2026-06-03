package cn.itcast.demo.minio.vo;

import lombok.Data;

@Data
public class SysFileVO {
    private Long id;

    private String Url;

    private String fileName;


    private String bucketName;

}
