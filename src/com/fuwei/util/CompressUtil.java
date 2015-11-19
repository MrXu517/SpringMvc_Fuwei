package com.fuwei.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CompressUtil {
	private static boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

	// 图片处理
	public static String compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int outputWidth,
			int outputHeight) throws Exception {
		FileOutputStream out = null;
		try {
			// 获得源文件
			File mdfile = new File(outputDir);
			if (!mdfile.exists()) {
				mdfile.mkdirs();
			}
			File file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				return "";
			}
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				return "no";
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (CompressUtil.proportion == true) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null))
							/ (double) outputWidth + 0.1;
					double rate2 = ((double) img.getHeight(null))
							/ (double) outputHeight + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					newWidth = outputWidth; // 输出的图片宽度
					newHeight = outputHeight; // 输出的图片高度
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
				// File file=new File(outputDir + outputFileName);
				// if(!file.exists()){
				// file.createNewFile();
				// }
				out = new FileOutputStream(outputDir
						+ outputFileName);
				// JPEGImageEncoder可适用于其他图片类型的转换
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				encoder.encode(tag);
			}
		} catch (IOException ex) {
			throw ex;
		}finally{
			try{  
		        if (out != null) {
		        	out.close();
		        }
		      } catch (Exception e) {
		    	  out = null;
		      }
		}
		return outputFileName;
	}

	public static String compressPic(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int outputWidth,
			int outputHeight, String ext) throws Exception {
		try {
			String or_ext = outputFileName.substring(outputFileName
					.lastIndexOf("."), outputFileName.length());
			String h_filename = outputFileName.substring(0, outputFileName
					.lastIndexOf("."));
			outputFileName = h_filename + "." + ext;
			CompressUtil.compressPic(inputDir, outputDir, inputFileName,
					outputFileName, outputWidth, outputHeight);
			return outputFileName;
		} catch (Exception e) {
			throw e;
		}
	}
}
