package it.unical.demacs.asde.signme.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class UploadImageService {

	// Save the uploaded file to this folder
	private static String FOLDER = "src/main/resources/static/profilePictures/";
	private static String TMP = "src/main/resources/static/tmp/";

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private FaceRecognitionService faceRecognitionService;

	public String updateProfilePicture(MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			if (!isImage(file))
				return "Wrong extension";
			byte[] bytes = file.getBytes();
			Path path = Paths.get(FOLDER + fileName);
			Files.write(path, bytes);

			System.out.println(fileName);

			String[] splitOnDot = fileName.split("\\.");

			String extension = splitOnDot[splitOnDot.length - 1];
			String email = fileName.replace("." + extension, "");

			System.out.println(email);

			User user = userDAO.findById(email).get();
			user.setProfilePicture("profilePictures/" + fileName);
			userDAO.save(user);
			return "profilePictures/" + fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed";
		}
	}

	public ArrayList<String> uploadAttendacesPicture(MultipartFile file) {
		ArrayList<String> attendances = new ArrayList<String>();
		try {
			if (isImage(file))
				return attendances;
			byte[] bytes = file.getBytes();
			Path path = Paths.get(TMP + file.getOriginalFilename());
			String classPicture = path.toString();
			Files.write(path, bytes);

			Integer courseId = Integer.parseInt(file.getOriginalFilename().split(".")[0]);
			Course course = courseDAO.findById(courseId).get();
			ArrayList<String> studentPictures = new ArrayList<>();
			for (User user : course.getStudents()) {
				studentPictures.add(user.getProfilePicture());
			}

			attendances = faceRecognitionService.getAttendances(classPicture, studentPictures);

			for (String attendance : attendances) {
				System.out.println(attendance);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return attendances;
	}

	private boolean isImage(MultipartFile file) {
		String extension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename());
		return ((extension.equals("jpeg") || extension.equals("png") || extension.equals("jpg")
				|| extension.equals("bmp")));
	}

}
