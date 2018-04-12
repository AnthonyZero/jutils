/**
 * 
 */
package com.pingjin.qrcode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * google 二维码编码解码工具
 * @author pingjin create 2018年4月11日
 *
 */
public class GoogleQrcodeUtil {
    private static final String CHARSET = "utf-8";
  
    /**
     * 生成二维码图片
     * @param content 内容
     * @param logoImgPath 二维码logo地址 null或者""直接返回二维码图片
     * @param size 大小
     * @param needCompress 是否压缩
     * @return
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String logoImgPath,int size,boolean needCompress) throws Exception {  
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  //容错率,分四个等级：H、L 、M、 Q,H为最高级别
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET); //编码
        hints.put(EncodeHintType.MARGIN, 2);//二维码边框宽度 1，2，3，4,数字越大，白色边框越大
        hints.put(EncodeHintType.MAX_SIZE, 350);  
        hints.put(EncodeHintType.MIN_SIZE, 150);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, size, size, hints);  
        int width = bitMatrix.getWidth();  
        int height = bitMatrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);  
            }  
        }
        
        if (logoImgPath == null || "".equals(logoImgPath)) {
            return image;  
        }
        
        //插入LOGO图片  
        insertImage(image, logoImgPath, size,needCompress);  
        
        return image;  
    }  
  
    /** 
     * 插入LOGO 
     *  
     * @param source 二维码图片 
     * @param logoImgPath LOGO图片地址
     * @param size 二维码尺寸
     * @param needCompress 是否压缩 
     * @throws Exception 
     */  
    private static void insertImage(BufferedImage source, String logoImgPath,int size,boolean needCompress) throws Exception {  
        File file = new File(logoImgPath);  
        if (!file.exists()) {  
            throw new RuntimeException(""+ logoImgPath +"   该文件不存在！");  
        }
        
        Image src = ImageIO.read(new File(logoImgPath));  
        int width = src.getWidth(null);  
        int height = src.getHeight(null);  
        if (needCompress) {//压缩LOGO  
            if (width > size*25 /100) {  
                width = size*25 /100;  
            }  
            if (height > size*25 /100) {  
                height = size*25 /100;  
            }
            
            Image image = src.getScaledInstance(width, height,Image.SCALE_SMOOTH);  
            BufferedImage tag = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            src = image;  
        }
        
        //插入LOGO
        Graphics2D graph = source.createGraphics();  
        int x = (size - width) / 2;  
        int y = (size - height) / 2;  
        graph.drawImage(src, x, y, width, height, null);  
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 15, 15);//logo的边框幅度,数字越大，幅度越大
        graph.setStroke(new BasicStroke(5f));//二维码边框粗细,数字越大,边框越粗
        graph.draw(shape); 
        graph.dispose();  
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 内容 
     * @param logoImgPath LOGO地址 
     * @param destPath 存放地址
     * @param imgType 图片类型
     * @param size 二维码尺寸
     * @param needCompress 是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String logoImgPath, String destPath,String imgType, int size,boolean needCompress) throws Exception {  
        BufferedImage image = createImage(content, logoImgPath,size,needCompress); 
        
		File file = new File(destPath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
        ImageIO.write(image, imgType, new File(destPath));  
    }
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 内容 
     * @param logoImgPath LOGO地址 
     * @param destPath 存储地址 
     * @param imgType 图片类型
     * @param size 二维码尺寸
     * @throws Exception 
     */  
    public static void encode(String content, String logoImgPath, String destPath,String imgType, int size) throws Exception {  
    	encode(content, logoImgPath, destPath, imgType,size,false);  
    }
  
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param destPath 存储地址 
     * @param needCompress 是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String destPath,String imgType,int size,boolean needCompress) throws Exception {  
    	encode(content, null, destPath, imgType,size,needCompress);  
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param destPath 存储地址
     * @param size 二维码尺寸
     * @param needCompress 是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String destPath, int size,boolean needCompress) throws Exception {  
    	encode(content, null, destPath, "png",size,needCompress);  
    }
  
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param destPath 存储地址
     * @param imgType 图片类型
     * @param size 二维码尺寸
     * @throws Exception 
     */  
    public static void encode(String content, String destPath,String imgType, int size) throws Exception {  
    	encode(content, null, destPath, imgType,size,false);  
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param destPath 存储地址 
     * @throws Exception 
     */  
    public static void encode(String content, String destPath, int size) throws Exception {  
    	encode(content, null, destPath, "png",size,false);  
    } 
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 内容 
     * @param logoImgPath LOGO地址 
     * @param output 输出流 
     * @param needCompress 是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String logoImgPath,OutputStream output, String imgType, int size,boolean needCompress) throws Exception {  
        BufferedImage image = createImage(content, logoImgPath,size,needCompress);  
        ImageIO.write(image, imgType, output);  
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param output 输出流 
     * @throws Exception 
     */  
    public static void encode(String content, String logoImgPath,OutputStream output,int size,boolean needCompress) throws Exception {  
    	encode(content, null, output, "png",size,needCompress);  
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param output 输出流 
     * @throws Exception 
     */  
    public static void encode(String content, OutputStream output,String imgType, int size,boolean needCompress) throws Exception {  
    	encode(content, null, output, imgType,size,needCompress);  
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param output 输出流 
     * @throws Exception 
     */  
    public static void encode(String content, OutputStream output,String imgType, int size) throws Exception {  
    	encode(content, null, output, imgType,size,false);  
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param output 输出流 
     * @throws Exception 
     */  
    public static void encode(String content, OutputStream output,int size) throws Exception {  
    	encode(content, null, output, "png",size,false);  
    }
    
    /** 
     * 生成二维码 
     *  
     * @param content 内容 
     * @param output 输出流 
     * @throws Exception 
     */  
    public static void encode(String content, OutputStream output,int size,boolean needCompress) throws Exception {  
    	encode(content, null, output, "png",size,needCompress);  
    }
  
    /** 
     * 解析二维码 
     *  
     * @param file 二维码图片 
     * @return 
     * @throws Exception 
     */  
    public static String decode(File file) throws Exception {  
        BufferedImage image;  
        image = ImageIO.read(file);  
        if (image == null) {  
            return null;  
        }
        
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);  
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
        Result result;  
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();  
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);  
        result = new MultiFormatReader().decode(bitmap, hints);  
        String resultStr = result.getText();
        
        return resultStr;  
    }  
  
    /** 
     * 解析二维码 
     *  
     * @param path 二维码图片地址 
     * @return 
     * @throws Exception 
     */  
    public static String decode(String path) throws Exception {  
        return decode(new File(path));  
    }  
  
    public static void main(String[] args) throws Exception {   
        
		String distPath = "D:/test/1/qrcode.png"; // 生成二维码图片存放的地址
		String encoderContent = "https://anthonyzero.github.io/"; // 二维码显示的内容
		String logoPath = "D:/a.png";
		
        encode(encoderContent, logoPath, distPath, "png", 300, true);
        encode("www.baidu.com", "D:/test/2/qrcode.jpg", 400, true);
        System.out.println("success");
    }  
}
