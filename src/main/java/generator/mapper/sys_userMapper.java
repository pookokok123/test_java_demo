package generator.mapper;

import generator.domain.sys_user;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户表)】的数据库操作Mapper
* @createDate 2025-12-01 14:23:58
* @Entity generator.domain.sys_user
*/
public interface sys_userMapper extends BaseMapper<sys_user> {

    public sys_user selectByUser(sys_user user);
}




