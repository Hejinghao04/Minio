package cn.itcast.demo.minio.service.impl;

import cn.itcast.demo.minio.entity.EnterPrise;
import cn.itcast.demo.minio.config.result.Result;
import cn.itcast.demo.minio.entity.Satellite;
import cn.itcast.demo.minio.vo.SatelliteVO;
import cn.itcast.demo.minio.mapper.EnterPriseMapper;
import cn.itcast.demo.minio.mapper.SatelliteMapper;
import cn.itcast.demo.minio.service.ISatelliteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ISatelliteServiceImpl extends ServiceImpl<SatelliteMapper, Satellite> implements ISatelliteService {
    private final SatelliteMapper satelliteMapper;
    private final EnterPriseMapper enterPriseMapper;

    public ISatelliteServiceImpl(SatelliteMapper satelliteMapper, EnterPriseMapper enterPriseMapper) {
        this.satelliteMapper = satelliteMapper;
        this.enterPriseMapper = enterPriseMapper;
    }

    @Override
    public Result<List<SatelliteVO>> getList() {
        List<Satellite> satelliteList = satelliteMapper.selectList(null);
        List<SatelliteVO> satelliteVOS = new ArrayList<>();
        satelliteList.forEach(satellite -> {
            SatelliteVO satelliteVO = new SatelliteVO();
            satelliteVO.setId(satellite.getId());
            satelliteVO.setName(satellite.getSateName());

            EnterPrise ep = enterPriseMapper.selectById(satellite.getEpId());
            satelliteVO.setEpName(ep != null ? ep.getEpName() : null);

            satelliteVOS.add(satelliteVO);
        });

        return Result.success(satelliteVOS);
    }
}
