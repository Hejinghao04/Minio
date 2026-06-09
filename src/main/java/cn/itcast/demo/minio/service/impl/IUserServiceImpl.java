package cn.itcast.demo.minio.service.impl;

import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.context.BaseContext;
import cn.itcast.demo.minio.dto.SatelliteDTO;
import cn.itcast.demo.minio.dto.UserDTO;
import cn.itcast.demo.minio.entity.*;
import cn.itcast.demo.minio.mapper.EnterPriseMapper;
import cn.itcast.demo.minio.mapper.SatelliteMapper;
import cn.itcast.demo.minio.mapper.UserMapper;
import cn.itcast.demo.minio.service.IUserService;
import cn.itcast.demo.minio.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Objects;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private final UserMapper userMapper;
    private final EnterPriseMapper enterPriseMapper;
    private final SatelliteMapper satelliteMapper;

    public IUserServiceImpl(UserMapper userMapper, EnterPriseMapper enterPriseMapper, SatelliteMapper satelliteMapper) {
        this.userMapper = userMapper;
        this.enterPriseMapper = enterPriseMapper;
        this.satelliteMapper = satelliteMapper;
    }

    @Override
    public Result<String> register(UserDTO userDTO) {
        String enterKey = userDTO.getEnterKey();
        String pass = userDTO.getUserPass();
        if (enterKey==null){
            return Result.error("请输入公司Key!");
        }
        String ePName = userDTO.getEnterPrise();
        if (ePName==null){
            return Result.error("请输入公司名!");
        }
        EnterPrise enterPrise = enterPriseMapper.selectByName(ePName);
        if (enterPrise == null){
            return Result.error("该公司未注册!!");
        }
        String enterKeyT = enterPrise.getEnterKey();
        if (!Objects.equals(enterKeyT, enterKey)){
            return Result.error("Key错误！请核对后重试");
        }
        User user = new User();
        user.setEPId(enterPrise.getId());

        BeanUtils.copyProperties(userDTO,user);

        //用户密码加密
        user.setUserPass(DigestUtils.md5DigestAsHex(pass.getBytes()));

        userMapper.insert(user);

        return Result.success("注册成功");
    }

    @Override
    public Result<UserVO> login(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getUserPass();
        if (username == null || username.isEmpty()){
            return Result.error("用户名不能为空");
        }
        User user = userMapper.selectByName(username);
        if (user == null){
            return Result.error("用户不存在，请注册");
        }
        String s = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!s.equals(user.getUserPass())){
            return Result.error("密码或账号错误");
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(username);
        return Result.success(userVO);
    }

    @Override
    public Result<String> insertSate(SatelliteDTO satelliteDTO) {
        if (satelliteDTO == null){
            return Result.error("后台异常，请联系管理员");
        }
        Long id = BaseContext.getCurrentId();
        if (id == null){
            return Result.error("后台异常，请联系管理员");
        }
        User user = userMapper.selectById(id);
        if (user == null){
            return Result.error("操作人员信息异常");
        }

        Long epId = user.getEPId();
        Satellite satellite = new Satellite();

        BeanUtils.copyProperties(satelliteDTO,satellite);

        satellite.setUserId(id);
        satellite.setEpId(epId);

        satelliteMapper.insert(satellite);

        BaseContext.removeCurrentId();
        return Result.success("插入卫星成功");
    }
}
