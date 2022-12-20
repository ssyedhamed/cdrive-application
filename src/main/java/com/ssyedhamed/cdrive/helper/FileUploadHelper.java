package com.ssyedhamed.cdrive.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadHelper {
	public static boolean uploadFile(MultipartFile file) {
		try {
			File UPLOAD_DIR = new ClassPathResource("static/images").getFile().getAbsoluteFile();
			Files.copy(file.getInputStream(),Paths.get(UPLOAD_DIR+File.separator+file.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean deleteFile(String fileName) {
		try {
			File UPLOAD_DIR = new ClassPathResource("static/images").getFile().getAbsoluteFile();
			
			if(!fileName.equals("default.png")) {	
				System.out.println(UPLOAD_DIR+File.separator+fileName);
				Path path = Paths.get(UPLOAD_DIR+File.separator+fileName);
				System.out.println("Is "+fileName+" exists? "+Files.exists(path, LinkOption.NOFOLLOW_LINKS));
				boolean b=Files.deleteIfExists(path);
				System.out.println(b+" Is "+fileName+" exists? "+Files.exists(path, LinkOption.NOFOLLOW_LINKS));
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean isValidFile(MultipartFile file) {
		if(file.getContentType().equals("image/jpeg")||file.getContentType().trim().equals("image/png")) {
			return true;
		}
		return false;
	}
	
	
}
