package cn.demo.imgbed.mapper;

import cn.demo.imgbed.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author MrWang
 * @Date 2020/10/24 9:58
 */
@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {
    UserAccount selectByName(String name);
}
