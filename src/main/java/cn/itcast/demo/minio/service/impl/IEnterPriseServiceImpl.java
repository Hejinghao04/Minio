package cn.itcast.demo.minio.service.impl;

import cn.itcast.demo.minio.entity.EnterPrise;
import cn.itcast.demo.minio.result.Result;
import cn.itcast.demo.minio.mapper.EnterPriseMapper;
import cn.itcast.demo.minio.service.IEnterPriseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IEnterPriseServiceImpl extends ServiceImpl<EnterPriseMapper, EnterPrise> implements IEnterPriseService {
    private final EnterPriseMapper enterPriseMapper;

    public IEnterPriseServiceImpl(EnterPriseMapper enterPriseMapper) {
        this.enterPriseMapper = enterPriseMapper;
    }

    @Override
    public Result<String> register(EnterPrise enterPrise) {
        if (enterPrise==null){
            return Result.error("商户不能为空");
        }
        String enterKey = enterPrise.getEnterKey();
        if(enterKey == null){
            enterKey = enterPrise.getEpName() + UUID.randomUUID();
        }
        EnterPrise enterPrise1 = enterPriseMapper.selectByName(enterPrise.getEpName());
        if (enterPrise1 != null){
            return Result.error("商户已存在");
        }
        enterPriseMapper.insert(enterPrise);
        return Result.success("公司注册成功");
    }
}
