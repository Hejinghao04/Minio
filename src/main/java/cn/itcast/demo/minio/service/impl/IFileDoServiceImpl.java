package cn.itcast.demo.minio.service.impl;

import cn.itcast.demo.minio.context.BaseContext;
import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.dto.SatelliteDTO;
import cn.itcast.demo.minio.entity.SysFile;
import cn.itcast.demo.minio.mapper.FileDoMapper;
import cn.itcast.demo.minio.service.IFileDoService;
import cn.itcast.demo.minio.service.IUploadService;
import cn.itcast.demo.minio.vo.SysFileVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.minio.errors.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class IFileDoServiceImpl extends ServiceImpl<FileDoMapper, SysFile> implements IFileDoService {

    private final IUploadServiceImpl uploadService;
    public IFileDoServiceImpl(IUploadServiceImpl uploadService, FileDoMapper fileDoMapper) {
        this.uploadService = uploadService;
        this.fileDoMapper = fileDoMapper;
    }
    private final FileDoMapper fileDoMapper;

    @Override
    public Result<IPage<SysFileVO>> getPage(String bucketName, SatelliteDTO satelliteDTO) {
        Long userid = BaseContext.getCurrentId();

        Page<SysFile> page = new Page<>(satelliteDTO.getPageNum(), satelliteDTO.getPageSize());

        Long sateId = null;
        if (satelliteDTO.getId() != null && !satelliteDTO.getId().isEmpty()) {
            sateId = Long.parseLong(satelliteDTO.getId());
        }

        QueryWrapper<SysFile> wrapper = new QueryWrapper<>();
        wrapper.eq(sateId != null, "sate_id", sateId)
                .like(StringUtils.hasText(bucketName), "bucket_name", bucketName)
                .eq(userid != null, "user_id", userid);

        Page<SysFile> sysFilePage = baseMapper.selectPage(page, wrapper);
        IPage<SysFileVO> iPage = sysFilePage.convert(sysFile -> {
            SysFileVO vo = new SysFileVO();
            BeanUtils.copyProperties(sysFile, vo);
            try {
                vo.setUrl(uploadService.getUrl(sysFile.getBucketName(),sysFile.getObjectName()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return vo;
        });

        return Result.success(iPage);
    }
}
