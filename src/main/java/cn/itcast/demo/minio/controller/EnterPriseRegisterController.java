package cn.itcast.demo.minio.controller;

import cn.itcast.demo.minio.entity.EnterPrise;
import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.service.IEnterPriseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EnterPriseRegisterController {

    @Autowired
    private final IEnterPriseService enterPrisService;

    public EnterPriseRegisterController(IEnterPriseService enterPrisService) {
        this.enterPrisService = enterPrisService;
    }

    @PostMapping("api/enterPriseRegister")
    public Result enterRegister(@RequestBody EnterPrise enterPrise){

        return enterPrisService.register(enterPrise);
    }
}
