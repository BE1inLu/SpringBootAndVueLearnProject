package com.testdemo01.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;
import com.testdemo01.common.lang.Const;
import com.testdemo01.entity.SysUser;
import com.testdemo01.result.result;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class AuthController extends BaseController {

    @Autowired
    private Producer producer;

    /**
     * 图片验证码生成
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/captcha")
    public result captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 验证码生成
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        // 通过 BufferedImage 生成图片
        BufferedImage image = producer.createImage(code);
        // System.out.println(image);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        // base64加密
        String str = "data:image/jpeg;base64,";
        String base64Imgstr = str + Base64Encoder.encode(outputStream.toByteArray());

        redisUtil.hashmapset(Const.CAPTCHA_KEY, key, code, 120);

        // log
        System.out.println("key: " + key);
        System.out.println("code: " + code);

        // result 回传消息请求头
        return result.succ(
                MapUtil.builder()
                        .put("token", key)
                        .put("captchaImg", base64Imgstr)
                        .build());
    }

    @GetMapping("/sys/userInfo")
    public result userInfo(@RequestParam Principal principal) {

        System.out.println("==执行 Authcontroller：/sys/userInfo ===");

        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        return result.succ(MapUtil.builder()
                .put("id", sysUser.getId())
                .put("username", sysUser.getUsername())
                .put("avatar", sysUser.getAvatar())
                .put("created", sysUser.getCreated())
                .map());
    }

}
