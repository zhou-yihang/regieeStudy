package com.powerbyte.regiee.controller;

import com.powerbyte.regiee.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file 文件
     * @return 文件上传结果
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("上传文件,fileName:{}", file);
        //获取文件名后缀
        String OriginalFilename = file.getOriginalFilename();
        assert OriginalFilename != null;
        String suffix = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));
        //使用uuid生成文件名称
        String fileName = UUID.randomUUID() + suffix;

        //创建一个目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            //如果不存在，则创建目录
            dir.mkdirs();
        }
        try {
            //将临时文件保存到指定目录
            file.transferTo(new File(basePath+ fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name 文件名
     * @param response 响应对象
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        ServletOutputStream outputStream = null;
        FileInputStream inputStream = null;
        try {
            //通过输入流读取文件
            inputStream = new FileInputStream(basePath + name);

            //通过输出流将文件写回到浏览器
            outputStream = response.getOutputStream();

            //设置响应头
            response.setContentType("image/jpeg");

            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
