package com.ht.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class SignatureUtil {

    /**
     * 生成签名
     *
     * @return 签名及签名时间
     */
    public static SignResult generateSignature(String accessKey, String accessKeySecret, String appCode) {
        long timestamp = System.currentTimeMillis();

        Map<String, String> params = new HashMap<>();
        params.put("accessKey", accessKey);
        params.put("accessKeySecret", accessKeySecret);
        params.put("appCode", appCode);
        params.put("timestamp", String.valueOf(timestamp));

        // 将参数按照字典序排序
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append(params.get(key));
        }

        String signStr = sb.toString();

        try {
            // 计算MD5签名
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(signStr.getBytes());

            // 将字节数组转换为十六进制字符串
            StringBuilder signSb = new StringBuilder();
            for (byte b : md5Bytes) {
                signSb.append(String.format("%02x", b));
            }

            return new SignResult(timestamp, signSb.toString().toUpperCase());

        } catch (NoSuchAlgorithmException e) {
            log.error("生成签名错误", e);
            throw new RuntimeException("生成签名错误");
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class SignResult {

        private Long timestamp;

        private String signature;
    }
}
