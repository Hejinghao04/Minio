package cn.itcast.demo.minio.mapper;

import cn.itcast.demo.minio.entity.EnterPrise;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EnterPriseMapper extends BaseMapper<EnterPrise> {

    @Select("select * from minioupload.enterprise where ep_name = #{epName}")
    EnterPrise selectByName(String epName);
}
