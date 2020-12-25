package cn.demo.imgbed.service.Impl;

import cn.demo.imgbed.dto.ApiRes;
import cn.demo.imgbed.dto.CommonRes;
import cn.demo.imgbed.entity.ImageDetail;
import cn.demo.imgbed.entity.ImgbedConfig;
import cn.demo.imgbed.entity.UserAccount;
import cn.demo.imgbed.mapper.ImageDetailMapper;
import cn.demo.imgbed.mapper.UserAccountMapper;
import cn.demo.imgbed.service.ImgbedService;
import cn.demo.imgbed.util.ApiResultUtil;
import cn.demo.imgbed.util.ResultUtil;
import cn.demo.imgbed.util.ImgbedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import sun.misc.BASE64Decoder;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


@Service
public class ImgbedServiceImpl implements ImgbedService {

    private ImgbedConfig imgbedConfig = null;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private ImageDetailMapper imageDetailMapper;

    //上传
    @Override
    public ApiRes doUpload(String imgBase64, String fileName, String username){
        if (imgbedConfig == null){
            getImgbedConfig();
        }
        //
        UserAccount currentUser = userAccountMapper.selectByName(username);
        if (imageDetailMapper.selectByIdAndFilename(currentUser.getId(),fileName) != null) {
            return ApiResultUtil.error("duplicated filename");
        }
        else {
            System.out.print("已经收到了把字节码转化为图片的方法");
            //对字节数组字符串进行Base64解码并生成图片
            if (imgBase64 == null) //图像数据为空
            {
                return ApiResultUtil.error("无图片");
            }

            BASE64Decoder decoder = new BASE64Decoder();
            try
            {
                //Base64解码
                byte[] b = decoder.decodeBuffer(imgBase64);
                for(int i=0;i<b.length;++i)
                {
                    if(b[i]<0)
                    {//调整异常数据
                        b[i]+=256;
                    }
                }
                String filePath = "/tmp"+"/"+username;
                File file = new File(filePath);
                if(!file.exists()){
                    file.mkdir();
                }
                String imgFilePath = filePath+"/"+fileName;//新生成的图片
                System.out.println(imgFilePath);
                OutputStream out = new FileOutputStream(imgFilePath);
                out.write(b);
                out.flush();
                out.close();

                ImageDetail imageDetail = new ImageDetail();
                imageDetail.setFilename(fileName);
                imageDetail.setOwnerId(currentUser.getId());
                imageDetailMapper.insert(imageDetail);
                return ApiResultUtil.success("http://akimcserver.xyz:10000/"+username+"/"+fileName);
            }
            catch (Exception e)
            {
                return ApiResultUtil.error(e.getMessage());
            }
        }
    }

    //用户登录
    @Override
    public CommonRes login(String username, String password){
        UserAccount userAccount = null;
        if(username!=""){
            try{
                userAccount = userAccountMapper.selectByName(username);
            }catch (Exception e){
                return ResultUtil.error(-1,e.getMessage());
            }
        }

        if(userAccount==null){
            return ResultUtil.error(-1,"用户名错误");
        }

        if(!userAccount.getPassword().equals(password)){
            return ResultUtil.error(-1,"密码错误");
        }
        return ResultUtil.success();
    }

    //拉取配置
    @Override
    public CommonRes getConfig(){
        if (imgbedConfig == null){
            imgbedConfig = getImgbedConfig();
        }
        return ResultUtil.success(imgbedConfig);
    }

    //设置配置
    @Override
    public CommonRes setConfig(ImgbedConfig newConfig){

        if (imgbedConfig == null){
            getImgbedConfig();
        }
        imgbedConfig.setType(newConfig.getType());
        imgbedConfig.setUsername(newConfig.getUsername());
        imgbedConfig.setPassword(newConfig.getPassword());
        save(imgbedConfig);
        return ResultUtil.success();
    }

    @Override
    public ImgbedConfig getImgbedConfig(){
        if (imgbedConfig == null){
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            String myPath = "";
            String[] tmp = path.split("/");
            for (int i = 1;i<tmp.length;i++) {
                if (tmp[i].indexOf(".war")<0){
                    myPath+=tmp[i]+"/";
                }else {
                    break;
                }
            }
            File file = new File(myPath+"config.txt");
            String myIni = ImgbedUtil.txt2String(file);
            imgbedConfig = new ImgbedConfig();

            if (myIni!=null && !myIni.equals("")){
                imgbedConfig.setUsername(ImgbedUtil.getSubString(myIni,"Username=[","]"));
                imgbedConfig.setPassword(ImgbedUtil.getSubString(myIni,"Password=[","]"));
                try {
                    imgbedConfig.setType(Integer.valueOf(ImgbedUtil.getSubString(myIni,"Type=[","]")));
                }catch (Exception e){
                    imgbedConfig.setType(2);
                }
            }else{
                imgbedConfig.setUsername("");
                imgbedConfig.setPassword("");
                imgbedConfig.setType(2);
                save(imgbedConfig);
            }
        }

        return imgbedConfig;
    }

    @Override
    public CommonRes signUp(String username, String password){
        if (userAccountMapper.selectByName(username) == null){
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername(username);
            userAccount.setPassword(password);
            try{
                userAccountMapper.insert(userAccount);
                return ResultUtil.success();
            }catch (Exception e){
                return ResultUtil.error(-1,e.getMessage());
            }
        }
        else {
            return ResultUtil.error(-1,"duplicated username");
        }
    }

    @Override
    public void save(ImgbedConfig imgbedConfig){
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String myPath = "";
        String[] tmp = path.split("/");
        for (int i = 1;i<tmp.length;i++) {
            if (tmp[i].indexOf(".war")<0){
                myPath+=tmp[i]+"/";
            }else {
                break;
            }
        }
        myPath+="config.txt";
        String myIni="User=["+ imgbedConfig.getUsername()+"]\r\n";
        myIni+="Pass=["+ imgbedConfig.getPassword()+"]\r\n";
        myIni+="Type=["+ imgbedConfig.getType()+"]\r\n";

        ImgbedUtil.contentToTxt(myPath,myIni);
    }
}
