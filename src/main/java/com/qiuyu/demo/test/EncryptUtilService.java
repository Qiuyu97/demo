package com.qiuyu.demo.test;

import com.qiuyu.demo.utils.DesUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EncryptUtilService {

    public String encrypt(String content,String millisTime) throws Exception {

        String key = DigestUtils.md5DigestAsHex(millisTime.getBytes());
        key = "li"+key;
        String encryptContent = DesUtil.encrypt(content, key);

        return encryptContent;
    }

    public String decrypt(String content,String millisTime) throws Exception {

        String key = DigestUtils.md5DigestAsHex(millisTime.getBytes());
        key = "li"+key;
        //content=content.toUpperCase();
        String decryptContent = DesUtil.decrypt(content, key);

        return decryptContent;
    }
}
