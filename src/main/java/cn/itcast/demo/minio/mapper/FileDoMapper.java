package cn.itcast.demo.minio.mapper;

import cn.itcast.demo.minio.entity.SysFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileDoMapper extends BaseMapper<SysFile> {
}
