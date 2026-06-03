package cn.itcast.demo.minio.mapper;

import cn.itcast.demo.minio.entity.SysFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UploadMapper extends BaseMapper<SysFile> {
    @Select("select * from sys_file where user_id = #{userId} limit 1")
    SysFile selectByUserid(Long userId);
}
