package com.lowyer.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

@Component
@RestController
public class SaveUploadFile {
	
	@Value("${imagePath}")
	String imagePath;
	
	@RequestMapping(value = "/test.Upload.aa", method = RequestMethod.POST)
	public JSONObject testUploadFile(HttpServletRequest req, MultipartHttpServletRequest multiReq,@RequestParam("ImgWidth") Integer width,@RequestParam("ImgHeight") Integer height) throws IOException {
		File temFile = null;
		FileOutputStream fos = null;
		boolean flag = false;
		JSONObject json = new JSONObject();
		//获取文件名
		String fileName = multiReq.getFile("TmpImgUrl").getOriginalFilename();
		//获取文件后缀
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		//新文件名
		String imageName = UniqueID.getUniqueID()+suffixName;
		//文件保存路径
		String filePath = imagePath + imageName;
		try {
			//MultipartFile 转换为 File
			temFile = new File(filePath);
			FileUtils.copyInputStreamToFile(multiReq.getFile("TmpImgUrl").getInputStream(), temFile);
			//按传入的尺寸进行缩放处理后再上传并保存
			Image image = ImageIO.read(temFile);
			BufferedImage newImage = compressImage(image, width, height);
			fos = new FileOutputStream(new File(filePath));
			flag = ImageIO.write(newImage, suffixName.substring(1), fos);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(fos != null) {
				fos.close();
			}
		}
		json.put("ImgName", imageName);
		json.put("Result", flag);
		return json;
		/*FileOutputStream fos = new FileOutputStream(new File(filePath));
		FileInputStream fs = (FileInputStream) multiReq.getFile("file").getInputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fs.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		fs.close();*/
	}
	
	/**
	 * 压缩或者增大图片
	 * @param image 文件源
	 * @param w 指定宽
	 * @param h 指定高
	 * @return
	 */
	public BufferedImage compressImage(Image image , int w , int h ) {
		
		int width = image.getWidth(null);    // 得到源图宽  
	    int height = image.getHeight(null);  // 得到源图长 
	    //假如图片的长宽比例大于1，来判读以哪一个为标准压缩或者增大图片
	    if (width / height > w / h) {
	    	//以宽度为标准，等比例压缩图片
	    	 h = (int) (height * w / width);  
		}else {
			 w = (int) (width * h / height);  
		}
	    
	    // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
        BufferedImage bufferedimage = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );   
        bufferedimage.getGraphics().drawImage(image, 0, 0, w, h, null); // 绘制缩小后的图  
    
		return bufferedimage;
	}

}