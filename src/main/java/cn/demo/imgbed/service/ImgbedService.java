package cn.demo.imgbed.service;

import cn.demo.imgbed.dto.ApiRes;
import cn.demo.imgbed.dto.CommonRes;
import cn.demo.imgbed.entity.ImgbedConfig;

/**
 * @Author MrWang
 * @Date 2020/12/22 19:40
 */
public interface ImgbedService {

    ApiRes doUpload(String imgBase64, String fileName, String username);

    CommonRes login(String username, String password);

    CommonRes getConfig();

    CommonRes setConfig(ImgbedConfig newConfig);

    ImgbedConfig getImgbedConfig();

    void save(ImgbedConfig imgbedConfig);
}
