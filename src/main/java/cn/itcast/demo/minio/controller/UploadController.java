package cn.itcast.demo.minio.controller;

import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.service.IUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class UploadController {

    private final IUploadService uploadService;

    public UploadController(IUploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/api/upload")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") Long type,
            @RequestParam("sateName") String sateName) {
        return uploadService.upload(file, type, sateName);
    }
}
