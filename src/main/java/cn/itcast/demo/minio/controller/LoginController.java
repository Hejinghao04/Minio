package cn.itcast.demo.minio.controller;


import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.dto.SatelliteDTO;
import cn.itcast.demo.minio.dto.UserDTO;
import cn.itcast.demo.minio.service.ISatelliteService;
import cn.itcast.demo.minio.service.IUserService;
import cn.itcast.demo.minio.utils.JwtClaimsConstant;
import cn.itcast.demo.minio.utils.JwtProperties;
import cn.itcast.demo.minio.utils.JwtUtil;
import cn.itcast.demo.minio.vo.SatelliteVO;
import cn.itcast.demo.minio.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    private final IUserService userService;

    private final ISatelliteService satelliteService;

    public LoginController(IUserService userService, ISatelliteService satelliteService, JwtProperties jwtProperties) {
        this.userService = userService;
        this.satelliteService = satelliteService;
        this.jwtProperties = jwtProperties;
    }
    private final JwtProperties jwtProperties;

    @PostMapping("api/login")
    public Result<UserVO> login(@RequestBody UserDTO userDTO){
        Result<UserVO> login = userService.login(userDTO);
        if (login.getCode() != 1) {
            return login;
        }
        UserVO vo = login.getData();


        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, vo.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        vo.setToken(token);

        return Result.success(vo);
    }

    @PostMapping("api/register")
    public Result<String> register(@RequestBody UserDTO userDTO){

        Result<String> register = userService.register(userDTO);






        return register;
    }

    @PostMapping("api/insertSatellite")
    public Result<String> insert(@RequestBody SatelliteDTO satelliteDTO){

        return userService.insertSate(satelliteDTO);


    }

    @GetMapping("api/getSate")
    public Result<List<SatelliteVO>> getSate(){
        return satelliteService.getList();

    }
}
