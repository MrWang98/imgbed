package cn.demo.imgbed.entity;

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
