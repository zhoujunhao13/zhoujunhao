package com.lowyer.utils;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
 
import javax.servlet.http.HttpServletRequest;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
 
@RestController
@Component
public class FileUploadController {
 
	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);
	
	@Value("${imagePath}")
	String ROOT;
 
	private final ResourceLoader resourceLoader;
 
	@Autowired
	public FileUploadController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
 
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String provideUploadInfo(Model model) throws IOException {
 
		model.addAttribute("files", Files.walk(Paths.get(ROOT))
				.filter(path -> !path.equals(Paths.get(ROOT)))
				.map(path -> Paths.get(ROOT).relativize(path))
				.map(path -> linkTo(methodOn(FileUploadController.class).getFile(path.toString())).withRel(path.toString()))
				.collect(Collectors.toList()));
 
		return "uploadForm";
	}
	//显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png
		@RequestMapping(method = RequestMethod.GET, value = "/{filename:.+}")
		@ResponseBody
		public ResponseEntity<?> getFile(@PathVariable String filename) {
	 
			try {
				return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
			} catch (Exception e) {
				return ResponseEntity.notFound().build();
			}
		}
	 
		//上传的方法
		@RequestMapping(method = RequestMethod.POST, value = "/fileupload")
		public JSONObject handleFileUpload(@RequestParam("file") MultipartFile file,
									   RedirectAttributes redirectAttributes, HttpServletRequest request) {
			String fileName = file.getOriginalFilename();
			System.out.println(fileName);
			boolean flag = false;
			if (!file.isEmpty()) {
				try {
					Files.copy(file.getInputStream(), Paths.get(ROOT, file.getOriginalFilename()));
					redirectAttributes.addFlashAttribute("message",
							"You successfully uploaded " + file.getOriginalFilename() + "!");
					flag = true;
				} catch (IOException|RuntimeException e) {
					redirectAttributes.addFlashAttribute("message", "Failued to upload " + file.getOriginalFilename() + " => " + e.getMessage());
				}
			} else {
				redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " because it was empty");
			}
	 
			JSONObject json = new JSONObject();
			json.put("flag", flag);
			json.put("url", "/images/"+fileName);
			return json;
		}
	 
	}

