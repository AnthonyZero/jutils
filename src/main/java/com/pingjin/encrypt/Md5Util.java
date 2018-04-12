/**
 * 
 */
package com.pingjin.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

import com.pingjin.common.StringUtil;

/**
 * md5工具
 * @author pingjin create 2018年4月11日
 *
 */
public class Md5Util {
    private final static String ALGORITHM = "MD5";
    
    /**
     * 签名(JDK MD5加密)
     * 
     * @param buf
     * @return
     */
    public static byte[] digest(byte[] buf) {
        return getMessageDigest().digest(buf);
    }
    
    /**
     * 获取MD5
     * 
     * @param file
     * @return
     */
    public static String getMd5(File file) {
        InputStream in = null;
        
        MessageDigest digest = getMessageDigest();
        try {
            in = new FileInputStream(file);
            
            digest.reset();
            byte[] buf = new byte[1024 * 10];
            int count = 0;
            
            while ((count = in.read(buf)) > 0) {                
                digest.update(buf, 0, count);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
                        
        byte[] md5buf = digest.digest();
        String md5 = getMd5Hexs(md5buf);
        return md5;
    }
    
    /**
     * 获取Md5
     * 
     * @param md5buf
     * @return
     */
    public static String getMd5Hexs(byte[] md5buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < md5buf.length; i ++) {
            sb.append(StringUtil.toHexString(md5buf[i], false));
        }
        
        String s = sb.toString();
        return s.toLowerCase();
    }
    
    /**
     * 默认的信息摘要
     * 
     * @return
     * @throws IllegalArgumentException
     */
    public static final MessageDigest getMessageDigest() throws IllegalArgumentException {
        try {
            return MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm [" + ALGORITHM + "]");
        }
    }
    
    public static void main(String[] args) {
    	//默认JDK MD5加密
    	String s = "as1d354cas";
    	MessageDigest messageDigest = getMessageDigest();
    	byte[] value = messageDigest.digest(s.getBytes());
    	//字节数组（byte[]）转为十六进制（Hex）字符串
    	System.out.println(Hex.encodeHex(value));
    	System.out.println(StringUtil.toHexString(value));
    	
    	//本类实现
    	System.out.println(getMd5Hexs(digest(s.getBytes())));
    }
}
