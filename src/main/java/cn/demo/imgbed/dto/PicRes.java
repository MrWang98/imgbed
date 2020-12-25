package cn.demo.imgbed.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class PicRes {
    private Integer code;

    private String msg;

    private List<String> pics;
}
