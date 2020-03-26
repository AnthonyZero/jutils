package com.pingjin.encrypt;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加、解密算法工具类
 */
public class RsaUtil {

    /**
     * 加密算法AES
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 算法名称/加密模式/数据填充方式
     * 默认：RSA/ECB/PKCS1Padding
     */
    private static final String ALGORITHMS = "RSA/ECB/PKCS1Padding";

    /**
     * Map获取公钥的key
     */
    private static final String PUBLIC_KEY = "publicKey";

    /**
     * Map获取私钥的key
     */
    private static final String PRIVATE_KEY = "privateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA 位数 如果采用2048 上面最大加密和最大解密则须填写:  245 256
     */
    private static final int INITIALIZE_LENGTH = 1024;

    /**
     * 后端RSA的密钥对(公钥和私钥)Map，由静态代码块赋值
     */
    private static Map<String, Object> genKeyPair = new HashMap<>();

    static {
        try {
            genKeyPair.putAll(genKeyPair());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成密钥对(公钥和私钥)
     */
    private static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(INITIALIZE_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        //公钥
        keyMap.put(PUBLIC_KEY, publicKey);
        //私钥
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     */
    public static byte[] encrypt(byte[] data, String publicKey) throws Exception {
        //base64格式的key字符串转Key对象
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);

        //设置加密、填充方式
        /*
            如需使用更多加密、填充方式，引入
            <dependency>
                <groupId>org.bouncycastle</groupId>
                <artifactId>bcprov-jdk16</artifactId>
                <version>1.46</version>
            </dependency>
            并改成
            Cipher cipher = Cipher.getInstance(ALGORITHMS ,new BouncyCastleProvider());
         */
        Cipher cipher = Cipher.getInstance(ALGORITHMS); //每次生成的密文都不一致 是因为RSA的PKCS#1padding 方案在加密前对明文信息进行了随机数填充。
        cipher.init(Cipher.ENCRYPT_MODE, publicK);

        //分段进行加密操作
        return encryptAndDecryptOfSubsection(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     */
    public static byte[] decrypt(byte[] encryptedData, String privateKey) throws Exception {
        //base64格式的key字符串转Key对象
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);

        //设置加密、填充方式
        /*
            如需使用更多加密、填充方式，引入
            <dependency>
                <groupId>org.bouncycastle</groupId>
                <artifactId>bcprov-jdk16</artifactId>
                <version>1.46</version>
            </dependency>
            并改成
            Cipher cipher = Cipher.getInstance(ALGORITHMS ,new BouncyCastleProvider());
         */
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.DECRYPT_MODE, privateK);

        //分段进行解密操作
        return encryptAndDecryptOfSubsection(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 获取私钥
     */
    public static String getPrivateKey() {
        Key key = (Key) genKeyPair.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey() {
        Key key = (Key) genKeyPair.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 分段进行加密、解密操作
     */
    private static byte[] encryptAndDecryptOfSubsection(byte[] data, Cipher cipher, int encryptBlock) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > encryptBlock) {
                cache = cipher.doFinal(data, offSet, encryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * encryptBlock;
        }
        byte[] toByteArray = out.toByteArray();
        out.close();
        return toByteArray;
    }


    /*public static void main(String[] args) {
        String str = "我是aes 的key(明文)";
        try {
            System.out.println("私钥：" + RsaUtil.getPrivateKey());
            System.out.println("公钥：" + RsaUtil.getPublicKey());

            //公钥加密
            byte[] ciphertext = RsaUtil.encrypt(str.getBytes(), RsaUtil.getPublicKey());
            //私钥解密
            byte[] plaintext = RsaUtil.decrypt(ciphertext, RsaUtil.getPrivateKey());

            System.out.println("公钥加密前：" + str);
            System.out.println("公钥加密后：" + Base64.encodeBase64String(ciphertext));
            System.out.println("私钥解密后：" + new String(plaintext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * http://tool.chacuo.net/cryptrsapubkey 公钥加密
     * http://tool.chacuo.net/cryptrsaprikey 私钥解密
     * https://the-x.cn/Cryptography/Rsa.aspx
     * @param args
     */
    public static void main(String[] args) {
        //String str = "我是aes 的key(明文)";
        String str = AesUtils.getKey();
        System.out.println(str);
        try {
            System.out.println("私钥：" + RsaUtil.getPrivateKey());
            System.out.println("公钥：" + RsaUtil.getPublicKey());

            //公钥加密
            byte[] ciphertext = RsaUtil.encrypt(str.getBytes(), RsaUtil.getPublicKey());
            String base64 = Base64.encodeBase64String(ciphertext);

            //私钥解密
            byte[] plaintext = RsaUtil.decrypt(Base64.decodeBase64(base64), RsaUtil.getPrivateKey());

            System.out.println("公钥加密前：" + str);
            System.out.println("公钥加密后：" + base64);
            System.out.println("再一次公钥加密后：" + Base64.encodeBase64String(RsaUtil.encrypt(str.getBytes(), RsaUtil.getPublicKey()))); //每次公钥加密后结果是不同的
            System.out.println("私钥解密后：" + new String(plaintext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCH6Wjxl+2AT053x74ELOn3iJBvk5SpJ8AfushmIQSmsgJ0JhBlqryza9ZdU9ONXW1cFfoqfakGPYnFOAlU7BZVqsRkRn+ZglVW7FxRZvsol9PIIuLMvAf+eo8mK//qwK8E0UC/zUzoRuKIaf+pNiwoIG0X7fI457myR6kBCPSiWwIDAQAB";
        String privteKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIfpaPGX7YBPTnfHvgQs6feIkG+TlKknwB+6yGYhBKayAnQmEGWqvLNr1l1T041dbVwV+ip9qQY9icU4CVTsFlWqxGRGf5mCVVbsXFFm+yiX08gi4sy8B/56jyYr/+rArwTRQL/NTOhG4ohp/6k2LCggbRft8jjnubJHqQEI9KJbAgMBAAECgYAczzlTnOTXQfhcI5b/w5WExwn23M2yXZ64GxvHqlAWFb23aSoootyFG+LAvbgmSxuXXYE96I0fqYO+rnYr7i99gHXFRHMo3nCqF0448kcmGp6BoDbW75oqsFdwEyyTCCHvIz+GW7Pqq4IyhOwpGDLEvVruG/RScy7F7GqTIWpjoQJBAOYSmNXi55f5zaDTQ22tThknFxpE/u7FS3lFyUPgzf7C/N6ajs99v0gz5v4wo7ru+C2NaTBj31CoMLaYJsQvOFcCQQCXOlmpgm3simgy+9GYTExrknIbjTwHeOie6o/wzrnlulzqWCzN2tYnWttf5Elv0PYoGXt1M0WfmKwv1Mk90HOdAkEAnUt9nk2BVM/+m8OUR/nCeUPWcalWOdw9W24wye16KIEa7P3yQT2Rd0W02XhbmEQk/M+/aMv/M1+p3kxrxJkcBQJAJWqbepQ5zFemVk712b+u/MKcFvMH0jV5ILCacpNDbXKBwL2WQEyPxeb19jDOdqr17I/5etL3u/gTSjZUoEWuBQJBALM8zKWI5dSBAEypKTdYawpdz9WzumL+Lc29w/9GHlqWUg4mtPR5+m/0B2pr0FeRUpoOxq3ooyDxpdBEmDP33G0=";
        try {
            System.out.println(new String(RsaUtil.decrypt(Base64.decodeBase64("Qfx9gSalBqOmroj+55X/Ab4S+JwGVntL/m7NCdgqWBDNWtUTZJ2aXMfUGCH+A5B9WLgRPh/7uKElN7WZjl6YI46cL67WIV1Mv1XlbrBZpFZQ51Jbl8O6J0gpQ/QLRuooMBZgeio0qHB/fmafUmFAE4WkwpKnxQhQ06VxN8GDlt8="), privteKey)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
