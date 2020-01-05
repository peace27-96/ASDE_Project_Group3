package it.unical.demacs.asde.signme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.unical.demacs.asde.signme.model.Course;
import it.unical.demacs.asde.signme.model.Lecture;
import it.unical.demacs.asde.signme.model.User;
import it.unical.demacs.asde.signme.repositories.CourseDAO;
import it.unical.demacs.asde.signme.repositories.LectureDAO;
import it.unical.demacs.asde.signme.repositories.UserDAO;

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

		// FaceRecognitionService uploadImageService = new FaceRecognitionService();

		User cris = new User();
		User kiello = new User();

		cris.setEmail("cris");
		kiello.setEmail("kiello");

		cris.setProfilePicture("src/main/resources/profilePictures/a@a.a.jpg");
		kiello.setProfilePicture("src/main/resources/profilePictures/a@a.a.jpg");

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

		Lecture lecture1 = new Lecture();
		Lecture lecture2 = new Lecture();

		course = courseDAO.findById(1).get();

		lecture1.setCourse(course);
		lecture2.setCourse(course);

		lectureDAO.save(lecture1);
		lectureDAO.save(lecture2);

		lecture1 = lectureDAO.findById(2).get();
		lecture2 = lectureDAO.findById(3).get();

		Set<Lecture> lectures = new HashSet<>();
		lectures.add(lecture1);
		lectures.add(lecture2);

		course.setLectures(lectures);

		courseDAO.save(course);

		course = courseDAO.findById(1).get();

		assertEquals(2, course.getStudents().size());

		assertEquals(2, course.getLectures().size());

//		try {
//			File file = new File("src/main/resources/static/tmp/");
//			BufferedImage bufferedImage = ImageIO.read(file);
//			WritableRaster raster = bufferedImage.getRaster();
//			DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
//			byte[] bytes = data.getData();
//			MultipartFile classPicture = new MockMultipartFile("1.jpeg", "1.jpeg", "jpeg", bytes);
//
//			ArrayList<String> studentPictures = new ArrayList<>();
//			for (User user : course.getStudents()) {
//				studentPictures.add(user.getProfilePicture());
//			}
//
//			ArrayList<String> attendances = uploadImageService.getAttendances("src/main/resources/static/tmp/", studentPictures);
//
//		} catch (IOException e) { // TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
