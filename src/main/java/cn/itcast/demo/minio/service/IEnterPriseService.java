package cn.itcast.demo.minio.service;

import cn.itcast.demo.minio.entity.EnterPrise;
import cn.itcast.demo.minio.result.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IEnterPriseService extends IService<EnterPrise> {
    Result<String> register(EnterPrise enterPrise);
}
