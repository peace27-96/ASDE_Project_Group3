package it.unical.demacs.asde.signme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//This sample uses Apache HttpComponents:
//http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/
//https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;

@SpringBootApplication
public class SignMeApplication {

	// key1 8ac7a514e86048a087f9fca5180a0981
	// key2 833a3a83f8f140aa8c7ffba6ae59e143
	// endpoint https://westcentralus.api.cognitive.microsoft.com/face/v1.0

	// Replace <Subscription Key> with your valid subscription key.
	private static final String subscriptionKey = "8ac7a514e86048a087f9fca5180a0981";
	private static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
	private static final String uriBaseVerify = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/verify";

	// private static final String imageWithFaces =
	// "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}";

	private static final String imageWithFaces1 = "res/kiello.jpeg";
	private static final String imageWithFaces2 = "res/cristian.jpeg";
	private static final String imageWithFaces3 = "res/nuccia.jpeg";
	private static final String imageWithFaces4 = "res/domenico.jpeg";
	private static final String imageWithFaces5 = "res/walter.jpeg";
	private static final String imageWithFaces6 = "res/classe.jpeg";
	// private static final String imageWithFaces7 = "res/classe1.jpeg";

	// private static final String faceAttributes =
	// "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

	public static void main(String[] args) {

		String studentsPicture = detect(imageWithFaces6);
		ArrayList<String> studentsFaces = new ArrayList<>();
		ArrayList<String> pictureFaceIDs = getPictureIDs(studentsPicture);

		HashMap<String, String> students = new HashMap<>();

		studentsFaces.add(imageWithFaces1);
		studentsFaces.add(imageWithFaces2);
		studentsFaces.add(imageWithFaces3);
		studentsFaces.add(imageWithFaces4);
		studentsFaces.add(imageWithFaces5);

		for (String face : studentsFaces) {
			String json = detect(face);
			students.put(face, getStudentID(json));
		}

		for (String key : students.keySet()) {
			String foundStudentKey = "";
			System.out.println("size pictureFaceIDs: " + pictureFaceIDs.size());
			for (String id : pictureFaceIDs) {
				if (compare(getJSONForCompare(students.get(key), id))) {
					System.out.println(key);
					foundStudentKey = id;
					break;
				}
			}
			if (!foundStudentKey.equals("")) {
				pictureFaceIDs.remove(foundStudentKey);
			}
		}

		SpringApplication.run(SignMeApplication.class, args);
	}

	private static String detect(String image) {

		HttpClient httpclient = HttpClientBuilder.create().build();
		String jsonString = "";

		try {
			URIBuilder builder = new URIBuilder(uriBase);

			// Prepare the URI for the REST API call.
			URI uri = builder.build();

			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/octet-stream"); // da cambiare in base all'input
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body.
			// StringEntity reqEntity = new StringEntity(imageWithFaces);
			File file = new File(image);

			FileEntity reqEntity = new FileEntity(file);
			request.setEntity(reqEntity);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {

				// Format and display the JSON response.
				// System.out.println("REST Response:\n");

				jsonString = EntityUtils.toString(entity).trim();
			}

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}

		return jsonString;
	}

	private static ArrayList<String> getPictureIDs(String jsonString) {

		ArrayList<String> facesID = new ArrayList<String>();

		JSONArray jsonArray = new JSONArray(jsonString);
		// System.out.println(jsonArray.getJSONObject(0).getString("faceId"));
		for (int i = 0; i < jsonArray.length(); i++) {
			facesID.add(jsonArray.getJSONObject(i).getString("faceId"));
		}
		// System.out.println(jsonArray.toString(2));

		return facesID;
	}

	private static String getStudentID(String jsonString) {

		String result = "";

		JSONArray jsonArray = new JSONArray(jsonString);

		// System.out.println(jsonArray.getJSONObject(0).getString("faceId"));
		result = jsonArray.getJSONObject(0).getString("faceId");
		// System.out.println(jsonArray.toString(2));

		return result;
	}

	private static String getJSONForCompare(String faceId1, String faceId2) {
		return "{ \"faceId1\":\"" + faceId1 + "\", \"faceId2\": \"" + faceId2 + "\" }";
	}

	private static boolean compare(String faces) {
		boolean found = false;
		HttpClient httpclient = HttpClients.createDefault();

		try {
			URIBuilder builder = new URIBuilder(uriBaseVerify);

			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

			// Request body
			StringEntity reqEntity = new StringEntity(faces);
			request.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				String result = EntityUtils.toString(entity);
				System.out.println(result);
				JSONObject obj = new JSONObject(result);
				if (obj.getBoolean("isIdentical")) {
					found = true;
				}

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return found;
	}
}