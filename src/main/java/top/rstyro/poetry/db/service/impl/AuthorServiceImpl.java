package top.rstyro.poetry.db.service.impl;

import top.rstyro.poetry.db.entity.Author;
import top.rstyro.poetry.db.mapper.AuthorMapper;
import top.rstyro.poetry.db.service.IAuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2023-07-25
 */
@Service
public class AuthorServiceImpl extends ServiceImpl<AuthorMapper, Author> implements IAuthorService {

}
