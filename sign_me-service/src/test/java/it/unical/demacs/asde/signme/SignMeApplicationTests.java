package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.LectureDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;
import it.unical.demacs.asde.signme.services.FaceRecognitionService;
import it.unical.demacs.asde.signme.services.UploadImageService;
import javassist.expr.NewArray;

@SpringBootTest
class SignMeApplicationTests {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CourseDAO courseDAO;

	@Autowired
	private LectureDAO lectureDAO;

	@Test
	public void attendacesWorks() {

		FaceRecognitionService uploadImageService = new FaceRecognitionService();

		User cris = new User();
		User kiello = new User();

		cris.setEmail("cris");
		kiello.setEmail("kiello");
		cris.setProfilePicture("res/profilePictures/cristian.jpeg");
		kiello.setProfilePicture("res/profilePictures/kiello.jpeg");

		userDAO.save(cris);
		userDAO.save(kiello);

		cris = userDAO.findById("cris").get();
		kiello = userDAO.findById("kiello").get();

		Set<User> students = new HashSet<>();

		students.add(cris);
		students.add(kiello);

		Course course = new Course();
		courseDAO.save(course);

		course = courseDAO.findById(1).get();

		cris.setFollowingCourses(new HashSet<Course>());
		cris.getFollowingCourses().add(course);

		kiello.setFollowingCourses(new HashSet<Course>());
		kiello.getFollowingCourses().add(course);

		userDAO.save(cris);
		userDAO.save(kiello);

		course.setStudents(students);

		courseDAO.save(course);

		assertEquals(2, course.getStudents().size());

		try {
			File file = new File("res/tmp/1.jpeg");
			BufferedImage bufferedImage = ImageIO.read(file);
			WritableRaster raster = bufferedImage.getRaster();
			DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
			byte[] bytes = data.getData();
			MultipartFile classPicture = new MockMultipartFile("1.jpeg", "1.jpeg", "jpeg", bytes);

			ArrayList<String> studentPictures = new ArrayList<>();
			for (User user : course.getStudents()) {
				studentPictures.add(user.getProfilePicture());
			}

			ArrayList<String> attendances = uploadImageService.getAttendances("res/tmp/1.jpeg", studentPictures);

		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
