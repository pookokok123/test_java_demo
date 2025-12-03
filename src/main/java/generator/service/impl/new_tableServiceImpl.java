package generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.new_table;
import generator.mapper.sys_userMapper;
import generator.service.new_tableService;
import generator.mapper.new_tableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
* @author Administrator
* @description 针对表【new_table】的数据库操作Service实现
* @createDate 2025-11-25 17:10:32
*/
@Service
public class new_tableServiceImpl extends ServiceImpl<new_tableMapper, new_table>
    implements new_tableService{

    @Autowired
    private new_tableMapper tableMapper;

    public IPage<new_table> GetPage(int pageNo, int pageSize)
    {
//        IPage<new_table> page = tableMapper.selectPage(new Page<new_table>(1, 10), null);
        LambdaQueryWrapper<new_table> queryWrapper = new LambdaQueryWrapper<new_table>()
                .gt(new_table::getId, 5); // 条件：年龄大于18
        IPage<new_table> PageWithCondition = tableMapper.selectPage(new Page<new_table>(pageNo, pageSize), queryWrapper);

        return PageWithCondition;
    }

}




