package cn.demo.imgbed.mapper;

import cn.demo.imgbed.entity.ImageDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageDetailMapper extends BaseMapper<ImageDetail> {
    ImageDetail selectByIdAndFilename(Integer id,String filename);
    List<ImageDetail> selectById(Integer id);
}
