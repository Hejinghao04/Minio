package cn.itcast.demo.minio.mapper;

import cn.itcast.demo.minio.entity.Satellite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SatelliteMapper extends BaseMapper<Satellite> {

    @Select("select * from minioupload.satellite where sate_name = #{sateName} and ep_id = #{epId}")
    Satellite selectByName(String sateName,Long epId);

    @Select("select * from minioupload.satellite where ep_id = #{epId}")
    List<Satellite> selectByEpId(Long epId);
}
