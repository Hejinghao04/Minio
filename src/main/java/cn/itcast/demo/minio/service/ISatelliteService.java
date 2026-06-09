package cn.itcast.demo.minio.service;

import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.entity.Satellite;
import cn.itcast.demo.minio.vo.SatelliteVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISatelliteService extends IService<Satellite> {
    Result<List<SatelliteVO>> getList();
}
