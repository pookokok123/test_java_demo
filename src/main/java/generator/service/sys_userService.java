package generator.service;

import generator.domain.sys_user;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户表)】的数据库操作Service
* @createDate 2025-12-01 14:23:58
*/
public interface sys_userService extends IService<sys_user> {

    public sys_user selectByUser(sys_user user);

    public sys_user selectByUserName(String username);

}
