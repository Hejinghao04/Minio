package cn.itcast.demo.minio.service.impl;

import cn.itcast.demo.minio.config.result.PageResult;
import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.entity.SysFile;
import cn.itcast.demo.minio.mapper.FileDoMapper;
import cn.itcast.demo.minio.service.IFileDoService;
import cn.itcast.demo.minio.vo.SysFileVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

public class IFileDoServiceImpl extends ServiceImpl<FileDoMapper, SysFile> implements IFileDoService {
    private final FileDoMapper fileDoMapper;

    public IFileDoServiceImpl(FileDoMapper fileDoMapper) {
        this.fileDoMapper = fileDoMapper;
    }

    @Override
    public Result<PageResult> getPage() {
        //fileDoMapper.selectPage();
        return null;
    }
}
