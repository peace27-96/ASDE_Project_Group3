package it.unical.demacs.asde.signme.services;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class FaceRecognitionService {

	// key1 8ac7a514e86048a087f9fca5180a0981
	// key2 833a3a83f8f140aa8c7ffba6ae59e143

	private static final String subscriptionKey = "03b1cdb4d8604750956d911f9c1f7af7";
	private static final String uriBaseDetect = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
	private static final String uriBaseVerify = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/verify";

	private HttpClient httpclient;
	private URIBuilder builder;
	private URI uri;
	private HttpPost request;
	private HttpResponse response;
	private HttpEntity entity;

	public FaceRecognitionService() {
		httpclient = HttpClientBuilder.create().build();
	}

	public ArrayList<String> getAttendances(String classPicture, ArrayList<String> studentPictures) {
		HashMap<String, String> studentFaceIDs = new HashMap<>();
		classPicture = detect(classPicture);
		ArrayList<String> classFaceIDs = getPictureIDs(classPicture);

		for (String face : studentPictures) {
			String studentFaceID = detect(face);
			studentFaceIDs.put(face, getStudentID(studentFaceID));
		}

		return compute(studentFaceIDs, classFaceIDs);
	}

	private ArrayList<String> compute(HashMap<String, String> students, ArrayList<String> pictureFaceIDs) {
		ArrayList<String> attendances = new ArrayList<>();
		for (String key : students.keySet()) {
			String foundStudentKey = "";
			for (String id : pictureFaceIDs) {
				if (compare(getJSONForCompare(students.get(key), id))) {
					foundStudentKey = key;
					break;
				}
			}
			if (!foundStudentKey.equals("")) {
				pictureFaceIDs.remove(foundStudentKey);
				attendances.add(foundStudentKey);
			}
		}
		return attendances;
	}

	private String detect(String image) {
		String jsonString = "";
		try {
			builder = new URIBuilder(uriBaseDetect);
			// Prepare the URI for the REST API call.
			uri = builder.build();
			request = new HttpPost(uri);
			// Request headers.
			request.setHeader("Content-Type", "application/octet-stream"); // da cambiare in base all'input
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			// Request body.
			File file = new File(image);
			FileEntity reqEntity = new FileEntity(file);
			request.setEntity(reqEntity);
			// Execute the REST API call and get the response entity.
			response = httpclient.execute(request);
			entity = response.getEntity();
			if (entity != null) {
				// Format and display the JSON response.
				jsonString = EntityUtils.toString(entity).trim();
			}

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return jsonString;
	}

	private boolean compare(String faces) {
		boolean found = false;
		try {
			builder = new URIBuilder(uriBaseVerify);
			// Prepare the URI for the REST API call.
			uri = builder.build();
			request = new HttpPost(uri);
			// Request headers.
			request.setHeader("Content-Type", "application/json");
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			// Request body
			StringEntity reqEntity = new StringEntity(faces);
			request.setEntity(reqEntity);
			// Execute the REST API call and get the response entity.
			response = httpclient.execute(request);
			entity = response.getEntity();
			if (entity != null) {
				String jsonString = EntityUtils.toString(entity).trim();
				if (jsonString.charAt(2) != 'e') {
					JSONObject obj = new JSONObject(jsonString);
					if (obj.getBoolean("isIdentical")) {
						found = true;
						System.out.println(obj.get("confidence"));
					}
				} else {
					System.out.println("Too many requests");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return found;
	}

	private ArrayList<String> getPictureIDs(String jsonString) {
		ArrayList<String> facesID = new ArrayList<String>();
		JSONArray jsonArray = new JSONArray(jsonString);
		for (int i = 0; i < jsonArray.length(); i++) {
			facesID.add(jsonArray.getJSONObject(i).getString("faceId"));
		}
		return facesID;
	}

	private String getStudentID(String jsonString) {
		String result = "";
		JSONArray jsonArray = new JSONArray(jsonString);
		result = jsonArray.getJSONObject(0).getString("faceId");
		return result;
	}

	private String getJSONForCompare(String faceId1, String faceId2) {
		return "{ \"faceId1\":\"" + faceId1 + "\", \"faceId2\": \"" + faceId2 + "\" }";
	}

}
