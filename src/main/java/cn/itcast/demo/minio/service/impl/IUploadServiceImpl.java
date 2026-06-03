package cn.itcast.demo.minio.service.impl;

import cn.itcast.demo.minio.context.BaseContext;
import cn.itcast.demo.minio.entity.*;
import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.mapper.EnterPriseMapper;
import cn.itcast.demo.minio.mapper.SatelliteMapper;
import cn.itcast.demo.minio.mapper.UploadMapper;
import cn.itcast.demo.minio.mapper.UserMapper;
import cn.itcast.demo.minio.service.IUploadService;
import cn.itcast.demo.minio.utils.Context;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IUploadServiceImpl extends ServiceImpl<UploadMapper, SysFile> implements IUploadService {

    private final MinioClient minioClient;

    @Autowired
    private UploadMapper uploadMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EnterPriseMapper enterPriseMapper;
    @Autowired
    private SatelliteMapper satelliteMapper;

    public IUploadServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public Result<String> upload(MultipartFile file, Long type, String sateName) {
        Long userId = BaseContext.getCurrentId();
        if (file == null || file.isEmpty()) {
            BaseContext.removeCurrentId();
            return Result.error("上传文件不能为空");
        }
        if (userId == null) {
            BaseContext.removeCurrentId();
            return Result.error("用户ID不能为空");
        }
        if (sateName == null){
            BaseContext.removeCurrentId();
            return Result.error("卫星未指定");
        }

        String bucketName = Context.COM_FILE;
        if (type != null && type == 1L){
            bucketName = Context.YAO_CE;
        } else if (type != null && type == 2L){
            bucketName = Context.GUI_DAO;
        }

        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            Long epId = user.getEPId();
            EnterPrise enterPrise = enterPriseMapper.selectById(epId);
            if (enterPrise == null) {
                return Result.error("所属公司不存在");
            }
            Satellite satellite = satelliteMapper.selectByName(sateName);
            if (satellite == null) {
                return Result.error("指定卫星不存在");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                originalFilename = "unnamed_" + System.currentTimeMillis();
            }

            String objectName = "enterprise_" + enterPrise.getEpName() + "/" + satellite.getSateName() + "/" + originalFilename;
            String contentType = file.getContentType();

            ensureBucketExists(bucketName);

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build());
            String ObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(3, TimeUnit.MINUTES)
                    .build());

            SysFile sysFile = new SysFile();
            sysFile.setFileName(originalFilename);
            sysFile.setSize(file.getSize());
            sysFile.setObjectName(objectName);
            sysFile.setBucketName(bucketName);
            sysFile.setUserId(userId);
            sysFile.setSateId(satellite.getId());
            sysFile.setContentType(contentType);
            sysFile.setYuanFilename(originalFilename);
            sysFile.setDldUrl(ObjectUrl);
            uploadMapper.insert(sysFile);
        } catch (Exception e) {
            log.error("上传失败", e);
            BaseContext.removeCurrentId();
            return Result.error("上传失败: " + e.getMessage());
        }
        BaseContext.removeCurrentId();
        return Result.success("上传成功");
    }

    private void ensureBucketExists(String bucketName) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName)
                .build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
    }
}
