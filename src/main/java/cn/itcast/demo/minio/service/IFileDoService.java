package cn.itcast.demo.minio.service;

import cn.itcast.demo.minio.config.result.PageResult;
import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.entity.SysFile;
import cn.itcast.demo.minio.vo.SysFileVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IFileDoService extends IService<SysFile> {
    Result<PageResult> getPage();
}
