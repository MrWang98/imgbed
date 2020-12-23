package cn.demo.imgbed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgbedConfig {
    private String username;
    private String password;
    private Integer type;

    @Override
    public String toString() {
        return "ImgbedConfig{" +
                ", Username='" + username + '\'' +
                ", Password='" + password + '\'' +
                ", type=" + type +
                '}';
    }
}
