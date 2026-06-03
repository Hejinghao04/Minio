package cn.itcast.demo.minio.mapper;

import cn.itcast.demo.minio.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from minioupload.sys_user where username = #{username}")
    User selectByName(String username);
}
