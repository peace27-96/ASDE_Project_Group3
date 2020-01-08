package it.unical.demacs.asde.signme.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.LectureDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

@Service
public class UploadImageService {

	// Save the uploaded file to this folder
	private static String FOLDER = "uploads/profilePictures/";
	private static String TMP = "uploads/tmp/";

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private LectureDAO lectureDAO;

	@Autowired
	private FaceRecognitionService faceRecognitionService;

	public String updateProfilePicture(MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			if (!isImage(file))
				return "Wrong extension";
			byte[] bytes = file.getBytes();
			Path path = Paths.get(FOLDER + fileName);
			File currFile = new File(FOLDER + fileName);
			currFile.delete();
			Files.write(path, bytes);

			System.out.println(fileName);

			String[] splitOnDot = fileName.split("\\.");

			String extension = splitOnDot[splitOnDot.length - 1];
			String email = fileName.replace("." + extension, "");

			System.out.println(email);

			User user = userDAO.findById(email).get();
			user.setProfilePicture(FOLDER + fileName);
			userDAO.save(user);
			return "uploads/profilePictures/" + fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed";
		}
	}

	public Set<User> uploadAttendacesPicture(MultipartFile file) {
		Set<User> attendingStudents = new HashSet<>();
		ArrayList<String> attendances = new ArrayList<String>();
		try {
			if (!isImage(file))
				return attendingStudents;
			byte[] bytes = file.getBytes();
			Path path = Paths.get(TMP + file.getOriginalFilename());
			String classPicture = path.toString();
			Files.write(path, bytes);
			String[] splittedName = file.getOriginalFilename().split("@");
			Integer courseId = Integer.parseInt(splittedName[0]);
			Integer lectureId = Integer.parseInt(splittedName[1].split("\\.")[0]);

			System.out.println("courseId " + courseId + " lectureId " + lectureId);

			Lecture lecture = lectureDAO.findById(lectureId).get();

			System.out.println(lecture.getLectureId());

			Course course = courseDAO.findById(courseId).get();

			ArrayList<String> studentPictures = new ArrayList<>();

			for (User user : course.getStudents()) {
				if(user.getProfilePicture() != null)
					studentPictures.add(user.getProfilePicture());
			}

			System.out.println(courseId);

			System.out.println("studenti iscritti al corso");
			for (String string : studentPictures) {
				System.out.println(string);
			}

			System.out.println(classPicture);

			attendances = faceRecognitionService.getAttendances(classPicture, studentPictures);

			System.out.println("studenti presenti");
			for (String attendance : attendances) {
				System.out.println(attendance);
			}

			for (User user : course.getStudents()) {
				if(user.getProfilePicture() != null )
					for (String attendance : attendances) {
						if (user.getProfilePicture().equals(attendance)) {
							Set<Lecture> lectures = user.getAttendedLectures();
							lectures.add(lecture);
							user.setAttendedLectures(lectures);
							userDAO.save(user);
							attendingStudents.add(user);
						}
					}
			}
			

			attendingStudents.addAll(lecture.getStudents());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return attendingStudents;
	}

	private boolean isImage(MultipartFile file) {
		String extension = com.google.common.io.Files.getFileExtension(file.getOriginalFilename());
		return ((extension.equals("jpeg") || extension.equals("png") || extension.equals("jpg")
				|| extension.equals("bmp")));
	}

}
