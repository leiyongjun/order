package com.bdqn.order.controller;

import com.bdqn.order.pojo.UserInfo;
import com.bdqn.order.service.UserService;
import com.bdqn.order.service.impl.UserServiceImpl;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @RequestMapping("")
    public Map doLogin(@RequestBody UserInfo userInfo, HttpSession session){
        Map retMap = new HashMap();

        if(!session.getAttribute("rightCode").toString().equals(userInfo.getImgCode())){
            retMap.put("retCode", "999");
            retMap.put("retMsg", "验证码错误!");
            return retMap;
        }

        System.out.println("sessionId :::" + session.getId());
        String sessImgCode = session.getAttribute("rightCode") == null ?
                "" : session.getAttribute("rightCode").toString();
        // 1 调用service去登陆的校验
        retMap = userService.doLogin(userInfo);
        //Map map = userService.doLogin(userInfo);
        // 2 如果登陆成功,则将用户信息存入session
        if(retMap.get("retCode").equals("1000")){
            session.setAttribute("user", retMap.get("user"));
        }
        return retMap;
    }

    @RequestMapping("/img")
    public void img(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            System.out.println("sessionId :::" + httpServletRequest.getSession().getId());
            httpServletRequest.getSession().setAttribute("rightCode", createText);
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);


            // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", 0);
            httpServletResponse.setContentType("image/jpeg");
            ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
            responseOutputStream.close();

        } catch (IllegalArgumentException e) {
            return;
        } catch (IOException e) {
            return;
        }

    }



}
