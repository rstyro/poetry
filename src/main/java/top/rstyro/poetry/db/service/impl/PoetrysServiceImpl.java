package top.rstyro.poetry.db.service.impl;

import top.rstyro.poetry.db.entity.Poetrys;
import top.rstyro.poetry.db.mapper.PoetrysMapper;
import top.rstyro.poetry.db.service.IPoetrysService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2023-07-26
 */
@Service
public class PoetrysServiceImpl extends ServiceImpl<PoetrysMapper, Poetrys> implements IPoetrysService {

}
