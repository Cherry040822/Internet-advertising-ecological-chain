package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/files")
public class FileController {
    // System.getProperty("user.dir");//获取到当前项目的根路径
    private static final String filePath = System.getProperty("user.dir")+"/files/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if (!FileUtil.isDirectory(filePath)){//如果目录不存在 需要先创建目录
            FileUtil.mkdir(filePath); //创建一个files目录
        }
        //提供文件存储的完整路径 给文件名加一个唯一的标识
        String fileName = System.currentTimeMillis()+"_"+ originalFilename; //1562336256_xxx.pgn
        String realPath = filePath + fileName;//完整的文件路径
        try{
            FileUtil.writeBytes(file.getBytes(),realPath);
        }catch (IOException e){
            e.printStackTrace();
            throw new CustomException("500","文件上传失败");
        }
        //返回一个网络连接 http://localhost:9090/files/downlode/文件名.后缀
        String url = "http://localhost:9090/files/download/"+fileName;
        return Result.success(url);
    }

    /*
     * 文件下载
     * */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response){
        try{
            response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");
            OutputStream os = response.getOutputStream();
            String realPath = filePath + fileName; //完整文件路径
            //获取文件的字节数组
            byte[] bytes = FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件下载失败");
        }
    }
}
