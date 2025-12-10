package generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import generator.domain.new_table;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Administrator
 * @description 针对表【new_table】的数据库操作Service
 * @createDate 2025-11-25 17:10:32
 */
public interface new_tableService extends IService<new_table> {
    IPage<new_table> GetPage(int pageNo, int pageSize);
}
