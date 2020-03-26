package com.pingjin.encrypt;


import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;

/**
 * AES加、解密算法工具类
 */
public class AesUtils {
    /**
     * 加密算法AES
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * key的长度，Wrong key size: must be equal to 128, 192 or 256
     * 传入时需要16、24、36 密钥长度则可以是128，192或256比特
     */
    private static final Integer KEY_LENGTH = 16 * 8;

    /**
     * 算法名称/加密模式/数据填充方式
     * 默认：AES/ECB/PKCS5Padding
     */
    private static final String ALGORITHMS = "AES/ECB/PKCS5Padding";

    /**
     * 后端AES的明文key，由静态代码块赋值
     */
    public static String key;

    static {
        key = getKey();
    }

    /**
     * 获取key
     */
    public static String getKey() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < KEY_LENGTH / 8; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * 加密
     *
     * @param content    加密的字符串
     * @param key key值
     */
    public static String encrypt(String content, String key) throws Exception {
        //设置Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHMS,new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), KEY_ALGORITHM));

        //调用doFinal
        byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));

        // 转base64
        return Base64.encodeBase64String(b);

    }

    /**
     * 解密
     *
     * @param encryptStr 解密的字符串
     * @param key 解密的key值
     */
    public static String decrypt(String encryptStr, String key) throws Exception {
        //base64格式的key字符串转byte
        byte[] decodeBase64 = Base64.decodeBase64(encryptStr);

        //设置Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHMS,new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), KEY_ALGORITHM));

        //调用doFinal解密
        byte[] decryptBytes = cipher.doFinal(decodeBase64);
        return new String(decryptBytes);
    }

    public static void main(String[] args) {
        //System.out.println("密码：" + key);
        //16位
        String key = "g1cT212gFjvdq6T0";

        //字符串
        String str = "我是最棒的";
        try {
            //加密
            String encrypt = AesUtils.encrypt(str, key);
            //解密
            String decrypt = AesUtils.decrypt(encrypt, key);

            System.out.println("加密前：" + str);
            System.out.println("加密后：" + encrypt);
            System.out.println("解密后：" + decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

