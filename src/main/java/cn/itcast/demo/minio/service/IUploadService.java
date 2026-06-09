package cn.itcast.demo.minio.service;

import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.entity.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;


public interface IUploadService extends IService<SysFile> {
    Result<String> upload(MultipartFile file, Long type, String sateId);
}
