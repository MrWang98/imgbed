package cn.demo.imgbed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonRes<T> {
    private Integer code;

    private String msg;

    private T data;

    @Override
    public String toString() {
        return "CommonRes{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
