package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.sys_user;
import generator.service.sys_userService;
import generator.mapper.sys_userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
* @createDate 2025-12-01 14:23:58
*/
@Service
public class sys_userServiceImpl extends ServiceImpl<sys_userMapper, sys_user>
    implements sys_userService{


    @Autowired
    private sys_userMapper userMapper;


    /**
     *
     * @param user 用户实体
     * @return
     */
    @Override
    public sys_user selectByUser(sys_user user)
    {
        return userMapper.selectByUser(user);
    }

    public sys_user selectByUserName(String user)
    {
        sys_user user1=new sys_user();
        user1.setUsername(user);
        return userMapper.selectByUser(user1);
    }

}




