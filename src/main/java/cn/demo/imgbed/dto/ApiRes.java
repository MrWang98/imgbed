package cn.demo.imgbed.dto;

import lombok.Data;

@Data
public class ApiRes {
    private Integer code;

    private String msg;

    private String img;

    @Override
    public String toString() {
        return "ApiRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
