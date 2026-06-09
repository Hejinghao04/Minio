package cn.itcast.demo.minio.service;

import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.dto.SatelliteDTO;
import cn.itcast.demo.minio.entity.SysFile;
import cn.itcast.demo.minio.vo.SysFileVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IFileDoService extends IService<SysFile> {
    Result<IPage<SysFileVO>> getPage(String bucketName, SatelliteDTO satelliteDTO);
}
