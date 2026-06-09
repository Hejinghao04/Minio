package cn.itcast.demo.minio.controller;

import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.dto.SatelliteDTO;
import cn.itcast.demo.minio.service.IFileDoService;
import cn.itcast.demo.minio.vo.SysFileVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FileDoController {
    private final IFileDoService fileDoService;

    public FileDoController(IFileDoService fileDoService) {
        this.fileDoService = fileDoService;
    }

    @GetMapping("api/fileEnterprisePage")
    public Result<IPage<SysFileVO>> getEPPage(@RequestParam(required = false, defaultValue = "") String bucketName,
                                              @RequestParam(required = false) String id,
                                              @RequestParam(required = false, defaultValue = "1") Long pageNum,
                                              @RequestParam(required = false, defaultValue = "10") Long pageSize) {
        SatelliteDTO satelliteDTO = new SatelliteDTO();
        satelliteDTO.setId(id);
        satelliteDTO.setPageNum(pageNum);
        satelliteDTO.setPageSize(pageSize);
        return fileDoService.getPage(bucketName, satelliteDTO);
    }
}
