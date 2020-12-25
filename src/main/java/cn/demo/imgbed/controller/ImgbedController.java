package cn.demo.imgbed.controller;

import cn.demo.imgbed.dto.ApiRes;
import cn.demo.imgbed.dto.CommonRes;
import cn.demo.imgbed.entity.ImgbedConfig;
import cn.demo.imgbed.entity.UserAccount;
import cn.demo.imgbed.service.ImgbedService;
import cn.demo.imgbed.util.ApiResultUtil;
import cn.demo.imgbed.util.ResultUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class ImgbedController {

    @Autowired
    private ImgbedService imgbedService;

    @RequestMapping("/api")
    public void api( String key, String imgBase64, HttpServletResponse rsp,HttpSession session) throws IOException {

        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        if (imgBase64 == null || imgBase64.equals("")){
            rsp.getWriter().print(new Gson().toJson(ApiResultUtil.error("请传入图片的Base64编码")));
        }

        if(session.getAttribute("user")!=null){
            ApiRes apiRes = imgbedService.doUpload(imgBase64, key, (String) session.getAttribute("user"));
            rsp.getWriter().print(new Gson().toJson(apiRes));
        }else{
            rsp.getWriter().print(new Gson().toJson("请先登录"));
        }

    }

    @PostMapping("/login")
    public CommonRes login(HttpSession session,ImgbedConfig pimgbedConfig){
        String username = pimgbedConfig.getUsername();
        String password = pimgbedConfig.getPassword();
        CommonRes res = imgbedService.login(username,password);
        if(res.getCode()!=-1){
            session.setAttribute("user",username);
            return imgbedService.setConfig(pimgbedConfig);
        }else {
            return ResultUtil.error(-1,res.getMsg());
        }
    }

    @RequestMapping("/logout")
    public CommonRes loginOut(HttpSession session){
        session.removeAttribute("user");
        return ResultUtil.success();
    }

    @RequestMapping("/signUp")
    public CommonRes signUp(UserAccount userAccount){
        if(userAccount.getUsername()==""){
            return ResultUtil.error(-1,"用户名不能为空");
        }
        if(userAccount.getPassword()==""){
            return ResultUtil.error(-1,"密码不能为空");
        }
        try{
            return imgbedService.signUp(userAccount.getUsername(),userAccount.getPassword());
        }catch (Exception e){
            return ResultUtil.error(-1,"注册失败："+e.getMessage());
        }
    }

}
