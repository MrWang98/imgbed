package cn.demo.imgbed.mapper;

import cn.demo.imgbed.entity.UserAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAccountMapper extends BaseMapper<UserAccount> {
    UserAccount selectByName(String name);
}
