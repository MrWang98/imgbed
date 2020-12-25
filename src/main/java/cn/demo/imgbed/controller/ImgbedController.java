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
    public void api( String key, String imgBase64, String onlyUrl, HttpServletResponse rsp,HttpSession session) throws IOException {

        rsp.setHeader("Content-Type", "application/json;charset=UTF-8");

        boolean str = false;
        if (onlyUrl!=null && onlyUrl.equals("1")){
            str = true;
        }

        if (imgBase64 == null || imgBase64.equals("")){
            if (str){
                rsp.getWriter().print("null");
            }else{
                rsp.getWriter().print(new Gson().toJson(ApiResultUtil.error("请传入图片的Base64编码")));
            }
        }

        ApiRes apiRes = ApiResultUtil.error("请先登录");

        if(session.getAttribute("user")!=null&&imgbedService.getImgbedConfig().getUsername()!=""){
            apiRes = imgbedService.doUpload(imgBase64, key, (String) session.getAttribute("user"));
        }
        if (str){
            rsp.getWriter().print(apiRes.getImg());
        }else{
            rsp.getWriter().print(new Gson().toJson(apiRes));
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
//            return ResultUtil.error(-1,"登录失败");
            return ResultUtil.error(-1,res.getMsg());
        }
    }

//    @GetMapping("/getConfig")
//    public CommonRes getConfig(HttpSession session){
//        return imgbedService.getConfig();
//    }

    @RequestMapping("/loginOut")
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
