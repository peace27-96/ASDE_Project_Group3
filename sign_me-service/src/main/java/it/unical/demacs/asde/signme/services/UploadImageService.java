package it.unical.demacs.asde.signme.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.unical.demacs.asde.signme.model.DTO.UserPictureUpdateDTO;



@Service
public class UploadImageService {

	// Save the uploaded file to this folder
	private static String FOLDER = "res/profilePictures/";

	public String updatePicture(MultipartFile file) {
		try {

			String extension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename()); 

			if(!(extension.equals("jpeg") || extension.equals("png")) )
				return "Failed";
			
			byte[] bytes = file.getBytes();
			Path path = Paths.get(FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			
			System.out.println(path);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Success";

	}

}
