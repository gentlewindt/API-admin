package com.gentlewind.sdk.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author NingWest
 * <p>
 * MD5加密/验证工具类
 */
public class Md5Utils {

    static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final char[] HEX_DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 对字符串 MD5 无盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @return MD5加密后生成32位(小写字母 + 数字)字符串
     */
    public static String md5Lower(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 对字符串 MD5 加密
     *
     * @param plainText 传入要加密的字符串
     * @return MD5加密后生成32位(大写字母 + 数字)字符串
     */
    public static String md5Upper(String plainText) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());

            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : mdResult) {
                // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                // 取字节中低 4 位的数字转换
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对字符串 MD5 加盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @param saltValue 传入要加的盐值
     * @return MD5加密后生成32位(小写字母 + 数字)字符串
     */
    public static String md5Lower(String plainText, String saltValue) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            md.update(saltValue.getBytes());

            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值。1 固定值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对字符串 MD5 加盐值加密
     *
     * @param plainText 传入要加密的字符串
     * @param saltValue 传入要加的盐值
     * @return MD5加密后生成32位(大写字母 + 数字)字符串
     */
    public static String md5Upper(String plainText, String saltValue) {
        try {
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 使用指定的字节更新摘要
            md.update(plainText.getBytes());
            md.update(saltValue.getBytes());

            // 获得密文
            byte[] mdResult = md.digest();
            // 把密文转换成十六进制的字符串形式
            int j = mdResult.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : mdResult) {
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5加密后生成32位(小写字母+数字)字符串
     * 同 MD5Lower() 一样
     */
    public static String md5(String plainText) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            mdTemp.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = HEX_DIGITS_LOWER[byte0 >>> 4 & 0xf];
                str[k++] = HEX_DIGITS_LOWER[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验MD5码
     *
     * @param text 要校验的字符串
     * @param md5  md5值
     * @return 校验结果
     */
    public static boolean valid(String text, String md5) {
        return md5.equals(md5(text)) || md5.equals(Objects.requireNonNull(md5(text)).toUpperCase());
    }


    public static void main(String[] args) {
        String plainText = "111";
        String saltValue = "admin123";

        System.out.println(md5(plainText));
        System.out.println("=====校验结果======");
        //System.out.println(valid(plainText, Objects.requireNonNull(md5(plainText))));
        boolean valid = valid("111", "698d51a19d8a121ce581499d7b701668");
        System.out.println(valid);

    }
}