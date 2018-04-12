/**
 * 
 */
package com.pingjin.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.pingjin.common.StringUtil;


/**
 * 图像工具
 * @author pingjin create 2018年4月11日
 *
 */
public class ImageUtil {
	/**
	 * 下载远程图片到本地
	 * 
	 * @param urlString http://图片链接地址
	 * @param fileName 本地图片路径 D:/a.png
	 * @throws Exception
	 */
	public static void downloadImage(String urlString, String fileName) {
		InputStream is = null;
		OutputStream os = null;
		
		try{
		    // 构造URL
		    URL url = new URL(urlString);
		    // 打开连接
		    URLConnection con = url.openConnection();
		    con.setConnectTimeout(5 * 1000);
		    con.setReadTimeout(5 * 1000);
		    
		    // 输入流
		    is = con.getInputStream();
		    // 1K的数据缓冲
		    byte[] bs = new byte[1024];
		    // 读取到的数据长度
		    int len;
		    
		    File imgPath = new File(fileName); 
		    if (!imgPath.getParentFile().exists()) {
		    	imgPath.getParentFile().mkdirs();
	    	}
		    
		    // 输出的文件流
		    os = new FileOutputStream(fileName);
		    // 开始读取
		    while ((len = is.read(bs)) != -1) {
		      os.write(bs, 0, len);
		    }
		}catch(Exception e){
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 缩放图像（按比例缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param scale
	 *            缩放比例
	 * @param flag
	 *            缩放选择:true 放大; false 缩小;
	 */
	public final static void scale(String srcImageFile, String result,
			int scale, boolean flag) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
			int width = src.getWidth(); // 得到源图宽
			int height = src.getHeight(); // 得到源图长
			if (flag) {// 放大
				width = width * scale;
				height = height * scale;
			} else {// 缩小
				width = width / scale;
				height = height / scale;
			}
			Image image = src.getScaledInstance(width, height,
					Image.SCALE_DEFAULT);
			BufferedImage tag = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 缩小
	 * 
	 * @param imgsrc
	 * @param imgdist
	 * @param width
	 * @param height
	 */
	public static void reduceImg(String imgsrc, String imgdist, int width,
			int height) {
		FileOutputStream out = null;
		try {
			File srcfile = new File(imgsrc);
			if (!srcfile.exists()) {
				return;
			}

			BufferedImage bi = ImageIO.read(srcfile);
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				double hr = (new Integer(height)).doubleValue()
						/ bi.getHeight();
				double wr = (new Integer(width)).doubleValue() / bi.getWidth();

				double rad = (hr < wr ? hr : wr);

				width = (int) (bi.getWidth() * rad);
				height = (int) (bi.getHeight() * rad);
			} else {
				return;
			}

			Image src = javax.imageio.ImageIO.read(srcfile);

			BufferedImage tag = new BufferedImage((int) width, (int) height,
					BufferedImage.TYPE_INT_RGB);

			tag.getGraphics().drawImage(
					src.getScaledInstance(width, height, Image.SCALE_SMOOTH),
					0, 0, null);

			out = new FileOutputStream(imgdist);
			ImageIO.write(tag, "jpg", out);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 缩放图像（按高度和宽度缩放）
	 * 
	 * @param srcImageFile
	 *            源图像文件地址
	 * @param result
	 *            缩放后的图像地址
	 * @param height
	 *            缩放后的高度
	 * @param width
	 *            缩放后的宽度
	 * @param bb
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 */
	public final static void scale2(String srcImageFile, String result,
			int height, int width, boolean bb) {
		try {
			double ratio = 0.0; // 缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height,
					BufferedImage.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue()
							/ bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(
						AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}

			if (bb) {// 补白
				BufferedImage image = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null)) {
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				} else {
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
							itemp.getWidth(null), itemp.getHeight(null),
							Color.white, null);
				}
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, "JPEG", new File(result));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 先缩放，后居中切割图片
	 * 
	 * @param srcPath
	 *            源图路径
	 * @param desPath
	 *            目标图保存路径
	 * @param rectw
	 *            待切割在宽度
	 * @param recth
	 *            待切割在高度
	 * @param gmCmdSearchPath
	 *        gm命令查找路径
	 * @throws IM4JavaException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void cropImageCenter(String srcPath, String desPath,
			int rectw, int recth, String gmCmdSearchPath) {
		IMOperation op = new IMOperation();

		op.addImage();
		op.resize(rectw, recth, '^').gravity("center").extent(rectw, recth);
		op.addImage();

		ConvertCmd convert = new ConvertCmd(true);
		
		try {
			if (StringUtil.hasText(gmCmdSearchPath)) {
				convert.setSearchPath(gmCmdSearchPath);   
			}
			
			convert.run(op, srcPath, desPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据尺寸缩放图片
	 * 
	 * @param srcPath
	 * @param desPath
	 * @param rectw
	 * @param recth
	 * @param gmCmdSearchPath
	 */
	public static void cutImage(String srcPath, String desPath,
			int rectw, int recth, String gmCmdSearchPath) {
		IMOperation op = new IMOperation();

		op.addImage();
		op.resize(rectw, recth > 0 ? recth : null);
		op.addImage();

		ConvertCmd convert = new ConvertCmd(true);
		
		try {
			if (StringUtil.hasText(gmCmdSearchPath)) {
				convert.setSearchPath(gmCmdSearchPath);   
			}
			
			convert.run(op, srcPath, desPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据宽度缩放图片
	 * 
	 * @param srcPath
	 * @param desPath
	 * @param rectw
	 * @param gmCmdSearchPath
	 */
	public static void cutImage(String srcPath, String desPath,
			int rectw, String gmCmdSearchPath) {
		IMOperation op = new IMOperation();

		op.addImage();
		op.resize(rectw, null);
		op.addImage();

		ConvertCmd convert = new ConvertCmd(true);
		
		try {
			if (StringUtil.hasText(gmCmdSearchPath)) {
				convert.setSearchPath(gmCmdSearchPath);   
			}
			
			convert.run(op, srcPath, desPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		//下载图片
		downloadImage("http://img.zcool.cn/community/01690955496f930000019ae92f3a4e.jpg@2o.jpg", "D:/a.png");
		//图片按照比例缩放
		scale("D:/a.png", "D:/b.png", 2, true);
		//图片按照 指定宽高 缩放(补白)
		scale2("D:/a.png", "D:/c.png", 845, 562, false);
		/*cropImageCenter("F:\\Photos\\手持身份证2.jpg", "F:\\Photos\\b2.jpg", 164, 120,
				"C:/Program Files (x86)/GraphicsMagick-1.3.16-Q8");
		
		System.out.println("---end");*/
	}
}
