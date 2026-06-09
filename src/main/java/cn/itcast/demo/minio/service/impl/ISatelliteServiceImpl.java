package cn.itcast.demo.minio.service.impl;

import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.context.BaseContext;
import cn.itcast.demo.minio.entity.EnterPrise;
import cn.itcast.demo.minio.entity.Satellite;
import cn.itcast.demo.minio.entity.User;
import cn.itcast.demo.minio.mapper.EnterPriseMapper;
import cn.itcast.demo.minio.mapper.SatelliteMapper;
import cn.itcast.demo.minio.mapper.UserMapper;
import cn.itcast.demo.minio.service.ISatelliteService;
import cn.itcast.demo.minio.vo.SatelliteVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ISatelliteServiceImpl extends ServiceImpl<SatelliteMapper, Satellite> implements ISatelliteService {
    private final SatelliteMapper satelliteMapper;
    private final EnterPriseMapper enterPriseMapper;
    private final UserMapper userMapper;

    public ISatelliteServiceImpl(SatelliteMapper satelliteMapper, EnterPriseMapper enterPriseMapper, UserMapper userMapper) {
        this.satelliteMapper = satelliteMapper;
        this.enterPriseMapper = enterPriseMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Result<List<SatelliteVO>> getList() {
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        Long epId = user.getEPId();
        EnterPrise enterPrise = enterPriseMapper.selectById(epId);

        List<Satellite> satelliteList = satelliteMapper.selectByEpId(epId);
        List<SatelliteVO> satelliteVOS = new ArrayList<>();
        satelliteList.forEach(satellite -> {
            SatelliteVO satelliteVO = new SatelliteVO();
            satelliteVO.setId(satellite.getId());
            satelliteVO.setName(satellite.getSateName());
            satelliteVO.setEpName(enterPrise != null ? enterPrise.getEpName() : null);
            satelliteVOS.add(satelliteVO);
        });

        return Result.success(satelliteVOS);
    }
}
