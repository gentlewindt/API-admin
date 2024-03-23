package com.gentlewind.sdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具类
 *
 * author gentlewind
 */
public class SignUtils {
    /**
     * 生成签名
     * @param body 包含需要签名的参数的哈希表
     * @param secretKey   秘钥
     * @return 生成的签名字符串
     */
    public static String genSign(String body, String secretKey){
        // 使用SHA256算法的Digester
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        // 构建签名内容，将哈希表转换为字符串并拼接秘钥
        String content = body +"." + secretKey;
        // 对签名内容进行加密
        return md5.digestHex(content);

    }

}
