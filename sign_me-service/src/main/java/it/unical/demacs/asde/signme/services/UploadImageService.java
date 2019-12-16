package it.unical.demacs.asde.signme.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadImageService {

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "res/profilePictures/";

	public void updatePicture(MultipartFile file) {
		if (file.isEmpty()) {
			System.out.println("ERROR");
		}

		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);

			System.out.println("message" + "You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
