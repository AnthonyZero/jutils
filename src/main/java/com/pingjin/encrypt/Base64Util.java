/**
  *
 *
 */
package com.pingjin.encrypt;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Base64编解码工具
 * @author pingjin create 2018年4月11日
 *
 */
public class Base64Util {
    private static final char[] S_BASE64CHAR = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
        'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
        'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', 
        '8', '9', '+', '/'
    };
    private static final char S_BASE64PAD = '=';
    private static final byte[] S_DECODETABLE = new byte[128];
    static {
        for (int i = 0;  i < S_DECODETABLE.length;  i ++)
            S_DECODETABLE[i] = Byte.MAX_VALUE;  // 127
        for (int i = 0;  i < S_BASE64CHAR.length;  i ++) // 0 to 63
            S_DECODETABLE[S_BASE64CHAR[i]] = (byte)i;
    }

    private static int decode0(char[] ibuf, byte[] obuf, int wp) {
        int outlen = 3;
        if (ibuf[3] == S_BASE64PAD)  outlen = 2;
        if (ibuf[2] == S_BASE64PAD)  outlen = 1;
        int b0 = S_DECODETABLE[ibuf[0]];
        int b1 = S_DECODETABLE[ibuf[1]];
        int b2 = S_DECODETABLE[ibuf[2]];
        int b3 = S_DECODETABLE[ibuf[3]];
        switch (outlen) {
        case 1:
            obuf[wp] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 0x3);
            return 1;
        case 2:
            obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 0x3);
            obuf[wp] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
            return 2;
        case 3:
            obuf[wp++] = (byte)(b0 << 2 & 0xfc | b1 >> 4 & 0x3);
            obuf[wp++] = (byte)(b1 << 4 & 0xf0 | b2 >> 2 & 0xf);
            obuf[wp] = (byte)(b2 << 6 & 0xc0 | b3 & 0x3f);
            return 3;
        default:
            throw new RuntimeException("internalError00");
        }
    }

    /**
     * 解码
     * 
     * @param data
     * @param off
     * @param len
     * @return
     */
    public static byte[] decode(char[] data, int off, int len) {
        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[len/4*3+3];
        int obufcount = 0;
        for (int i = off;  i < off+len;  i ++) {
            char ch = data[i];
            if (ch == S_BASE64PAD
                || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != Byte.MAX_VALUE) {
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }
        }
        if (obufcount == obuf.length)
            return obuf;
        byte[] ret = new byte[obufcount];
        System.arraycopy(obuf, 0, ret, 0, obufcount);
        return ret;
    }

    /**
     * 解码
     * 
     * @param data
     * @return
     */
    public static byte[] decode(String data) {
    	data = data.replace(" ", "+");
    	
        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[data.length()/4*3+3];
        int obufcount = 0;
        for (int i = 0;  i < data.length();  i ++) {
            char ch = data.charAt(i);
            if (ch == S_BASE64PAD
                || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != Byte.MAX_VALUE) {
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    obufcount += decode0(ibuf, obuf, obufcount);
                }
            }
        }
        if (obufcount == obuf.length)
            return obuf;
        byte[] ret = new byte[obufcount];
        System.arraycopy(obuf, 0, ret, 0, obufcount);
        return ret;
    }

    /**
     * 解码
     * 
     * @param data
     * @param off
     * @param len
     * @param ostream
     * @throws IOException
     */
    public static void decode(char[] data, int off, int len, OutputStream ostream) throws IOException {
        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[3];
        for (int i = off;  i < off+len;  i ++) {
            char ch = data[i];
            if (ch == S_BASE64PAD
                || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != Byte.MAX_VALUE) {
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }
        }
    }

    /**
     * 解码
     * 
     * @param data
     * @param ostream
     * @throws IOException
     */
    public static void decode(String data, OutputStream ostream) throws IOException {
    	data = data.replace(" ", "+");
    	
        char[] ibuf = new char[4];
        int ibufcount = 0;
        byte[] obuf = new byte[3];
        for (int i = 0;  i < data.length();  i ++) {
            char ch = data.charAt(i);
            if (ch == S_BASE64PAD
                || ch < S_DECODETABLE.length && S_DECODETABLE[ch] != Byte.MAX_VALUE) {
                ibuf[ibufcount++] = ch;
                if (ibufcount == ibuf.length) {
                    ibufcount = 0;
                    int obufcount = decode0(ibuf, obuf, 0);
                    ostream.write(obuf, 0, obufcount);
                }
            }
        }
    }

    /**
     * Returns base64 representation of specified byte array.
     * 编码
     * 
     * @param data
     * @return
     */
    public static String encode(byte[] data) {
        return encode(data, 0, data.length);
    }

    /**
     * Returns base64 representation of specified byte array.
     * 编码
     * 
     * @param data
     * @param off
     * @param len
     * @return
     */
    public static String encode(byte[] data, int off, int len) {
        if (len <= 0)  return "";
        char[] out = new char[len/3*4+4];
        int rindex = off;
        int windex = 0;
        int rest = len-off;
        while (rest >= 3) {
            int i = ((data[rindex]&0xff)<<16)
                    +((data[rindex+1]&0xff)<<8)
                    +(data[rindex+2]&0xff);
            out[windex++] = S_BASE64CHAR[i>>18];
            out[windex++] = S_BASE64CHAR[(i>>12)&0x3f];
            out[windex++] = S_BASE64CHAR[(i>>6)&0x3f];
            out[windex++] = S_BASE64CHAR[i&0x3f];
            rindex += 3;
            rest -= 3;
        }
        if (rest == 1) {
            int i = data[rindex]&0xff;
            out[windex++] = S_BASE64CHAR[i>>2];
            out[windex++] = S_BASE64CHAR[(i<<4)&0x3f];
            out[windex++] = S_BASE64PAD;
            out[windex++] = S_BASE64PAD;
        } else if (rest == 2) {
            int i = ((data[rindex]&0xff)<<8)+(data[rindex+1]&0xff);
            out[windex++] = S_BASE64CHAR[i>>10];
            out[windex++] = S_BASE64CHAR[(i>>4)&0x3f];
            out[windex++] = S_BASE64CHAR[(i<<2)&0x3f];
            out[windex++] = S_BASE64PAD;
        }
        return new String(out, 0, windex);
    }

    /**
     * Outputs base64 representation of the specified byte array to a byte stream.
     * 编码
     * 
     * @param data
     * @param off
     * @param len
     * @param ostream
     * @throws IOException
     */
    public static void encode(byte[] data, int off, int len, OutputStream ostream) throws IOException {
        if (len <= 0)  return;
        byte[] out = new byte[4];
        int rindex = off;
        int rest = len-off;
        while (rest >= 3) {
            int i = ((data[rindex]&0xff)<<16)
                    +((data[rindex+1]&0xff)<<8)
                    +(data[rindex+2]&0xff);
            out[0] = (byte)S_BASE64CHAR[i>>18];
            out[1] = (byte)S_BASE64CHAR[(i>>12)&0x3f];
            out[2] = (byte)S_BASE64CHAR[(i>>6)&0x3f];
            out[3] = (byte)S_BASE64CHAR[i&0x3f];
            ostream.write(out, 0, 4);
            rindex += 3;
            rest -= 3;
        }
        if (rest == 1) {
            int i = data[rindex]&0xff;
            out[0] = (byte)S_BASE64CHAR[i>>2];
            out[1] = (byte)S_BASE64CHAR[(i<<4)&0x3f];
            out[2] = (byte)S_BASE64PAD;
            out[3] = (byte)S_BASE64PAD;
            ostream.write(out, 0, 4);
        } else if (rest == 2) {
            int i = ((data[rindex]&0xff)<<8)+(data[rindex+1]&0xff);
            out[0] = (byte)S_BASE64CHAR[i>>10];
            out[1] = (byte)S_BASE64CHAR[(i>>4)&0x3f];
            out[2] = (byte)S_BASE64CHAR[(i<<2)&0x3f];
            out[3] = (byte)S_BASE64PAD;
            ostream.write(out, 0, 4);
        }
    }

    /**
     * Outputs base64 representation of the specified byte array to a character stream.
     * 编码
     * 
     * @param data
     * @param off
     * @param len
     * @param writer
     * @throws IOException
     */
    public static void encode(byte[] data, int off, int len, Writer writer) throws IOException {
        if (len <= 0)  return;
        char[] out = new char[4];
        int rindex = off;
        int rest = len-off;
        int output = 0;
        while (rest >= 3) {
            int i = ((data[rindex]&0xff)<<16)
                    +((data[rindex+1]&0xff)<<8)
                    +(data[rindex+2]&0xff);
            out[0] = S_BASE64CHAR[i>>18];
            out[1] = S_BASE64CHAR[(i>>12)&0x3f];
            out[2] = S_BASE64CHAR[(i>>6)&0x3f];
            out[3] = S_BASE64CHAR[i&0x3f];
            writer.write(out, 0, 4);
            rindex += 3;
            rest -= 3;
            output += 4;
            if (output % 76 == 0)
                writer.write("\n");
        }
        if (rest == 1) {
            int i = data[rindex]&0xff;
            out[0] = S_BASE64CHAR[i>>2];
            out[1] = S_BASE64CHAR[(i<<4)&0x3f];
            out[2] = S_BASE64PAD;
            out[3] = S_BASE64PAD;
            writer.write(out, 0, 4);
        } else if (rest == 2) {
            int i = ((data[rindex]&0xff)<<8)+(data[rindex+1]&0xff);
            out[0] = S_BASE64CHAR[i>>10];
            out[1] = S_BASE64CHAR[(i>>4)&0x3f];
            out[2] = S_BASE64CHAR[(i<<2)&0x3f];
            out[3] = S_BASE64PAD;
            writer.write(out, 0, 4);
        }
    }
    
    public static void main(String[] args) {
    	String s = "askjbkcnkaubbjko";
    	String encodeStr = encode(s.getBytes());
    	System.out.println(encodeStr);
    	String decodeStr = new String(decode(encodeStr));
    	System.out.println(decodeStr.equals(s));

        /**
         * 文件/图片 base64 测试   如果文件过大 会抛出OutOfMemoryError 不适用大文件（几百M） 尽量在50M以下
         */
        try {
            byte[] b = Files.readAllBytes(Paths.get("D:/work/数据.zip"));
            String endcodeData = encode(b);
            System.out.println(endcodeData);
            Files.write(Paths.get("D:/work/data.zip"), decode(endcodeData), StandardOpenOption.CREATE); //创建覆盖
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
