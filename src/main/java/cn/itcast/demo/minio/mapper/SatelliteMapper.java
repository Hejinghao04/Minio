package cn.itcast.demo.minio.mapper;

import cn.itcast.demo.minio.entity.Satellite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SatelliteMapper extends BaseMapper<Satellite> {

    @Select("select * from minioupload.satellite where sate_name = #{sateName}")
    Satellite selectByName(String sateName);
}
