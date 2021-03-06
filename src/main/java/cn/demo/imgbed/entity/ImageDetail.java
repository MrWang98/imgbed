package cn.demo.imgbed.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDetail {
    private Integer imgId;
    private String filename;
    private Integer ownerId;
}
