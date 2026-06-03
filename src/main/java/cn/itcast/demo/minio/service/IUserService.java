package cn.itcast.demo.minio.service;

import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.dto.SatelliteDTO;
import cn.itcast.demo.minio.dto.UserDTO;
import cn.itcast.demo.minio.entity.*;
import cn.itcast.demo.minio.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserService extends IService<User> {
    Result<String> register(UserDTO userDTO);

    Result<UserVO> login(UserDTO userDTO);

    Result<String> insertSate(SatelliteDTO satelliteDTO);
}
