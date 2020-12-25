package cn.demo.imgbed.service;

import cn.demo.imgbed.dto.ApiRes;
import cn.demo.imgbed.dto.CommonRes;
import cn.demo.imgbed.dto.PicRes;
import cn.demo.imgbed.entity.ImgbedConfig;

import javax.servlet.http.HttpSession;

public interface ImgbedService {

    ApiRes doUpload(String imgBase64, String fileName, String username);

    PicRes getAllPic(String username);

    CommonRes login(String username, String password);

    CommonRes getConfig();

    CommonRes setConfig(ImgbedConfig newConfig);

    CommonRes signUp(String username,String password);

    ImgbedConfig getImgbedConfig();

    void save(ImgbedConfig imgbedConfig);
}
