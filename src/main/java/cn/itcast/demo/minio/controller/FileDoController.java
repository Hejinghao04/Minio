package cn.itcast.demo.minio.controller;

import cn.itcast.demo.minio.config.result.PageResult;
import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.service.IFileDoService;
import cn.itcast.demo.minio.vo.SysFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class FileDoController {
    private final IFileDoService fileDoService;

    public FileDoController(IFileDoService fileDoService) {
        this.fileDoService = fileDoService;
    }

    @GetMapping("api/filePage")
    public Result<PageResult> getPage(){
        fileDoService.getPage();
        //TODO
        return null;
    }
}
