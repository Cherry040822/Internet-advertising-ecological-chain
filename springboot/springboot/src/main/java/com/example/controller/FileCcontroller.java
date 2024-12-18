package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileCcontroller {
    private static final String filePath = System.getProperty("user.dir")+"/files/";
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        try {
            response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName, StandardCharsets.UTF_8));//设置文件名编码
            response.setContentType("application/octet-stream");//设置响应头
            OutputStream os=response.getOutputStream();
            String realPath=filePath+fileName;
            //获取到文件的字节数组
            byte[] bytes= FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();
        }catch (IOException e){
            e.printStackTrace();
            throw new CustomException("500","文件失败");
        }

    }
}
