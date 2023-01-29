package com.qiuyu.demo.resource;

import com.qiuyu.demo.config.SnowFlakeProperties;
import com.qiuyu.demo.service.OpenApiService;
import com.qiuyu.demo.study.designpatters.observe.springImpl.UserService;
import com.qiuyu.demo.utils.PicUtils;
import com.qiuyu.demo.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Description:
 * @Author: qiuyu
 * @Date: 2021/3/8
 **/
@Slf4j
@RestController
@RequestMapping("/open")
public class OpenApiResource {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OpenApiService openApiService;

    @Autowired
    private SnowFlakeProperties snowFlakeProperties;

    @Autowired
    private UserService userService;


    @GetMapping("/test/demo/{str}")
    public String productQuote(@PathVariable String str) {

        return openApiService.testAop();
    }

    @GetMapping("/test/getSnowFlakeId")
    public String getSnowFlakeId() {

        return String.valueOf(SnowFlake.getInstance().nextId());
    }

    /**
     * 图片上传
     */
    @PostMapping(value = "/file/img", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] imgUploads(@RequestParam("file") MultipartFile file) throws IOException {
        return PicUtils.compressPicForScale(file.getBytes(), 64);
    }

//    public static void main(String[] args) throws IOException {
//        System.out.println("begin-------->reading");
//        long start = System.currentTimeMillis();
//        String url = "http://localhost:81/6867509453444.jpg";
//        ByteArrayResource byteArrayResource;
//        try {
//            URLConnection conn = new URL(url).openConnection();
//            byteArrayResource = new ByteArrayResource(FileBinary.toByteArray(conn)) {
//                @Override
//                public String getFilename() {
//                    return UUID.randomUUID().toString() + "." +"jpg";
//                }
//            };
//        } catch (IOException e) {
//            throw new ServiceException("getUrlByteArrayResource解析网络地址出错。请核对资源地址，保证可以在网络中访问。");
//        }
//
//        byte[] bytes = PicUtils.compressPicForScale(FileBinary.toByteArray(byteArrayResource.getInputStream()), 64);
//        String filePath = "C:\\Users\\qiuyu\\Desktop\\yasuohou.jpg";
//        FileOutputStream out = new FileOutputStream(filePath);
//        out.write(bytes);
//        out.flush();
//        out.close();
//        System.out.println("begin-------->reading"+" "+ (System.currentTimeMillis()-start) +"ms");
//    }


    @GetMapping("/user/register")
    public String userRegister(@RequestParam String username) {
        userService.register(username);
        return "ok";
    }

}
