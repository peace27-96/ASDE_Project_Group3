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

	// Chiave 1: 9cef08b64840442ab13b62d399dc1035

	// Chiave 2: bd98635d10f04842962d64dae184fe51

	private static final String subscriptionKey = "9cef08b64840442ab13b62d399dc1035";
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

	public HashMap<String, Double> getAttendances(String classPicture, ArrayList<String> studentPictures) {
		HashMap<String, String> studentFaceIDs = new HashMap<>();
		classPicture = detect(classPicture);
		ArrayList<String> classFaceIDs = getPictureIDs(classPicture);

		for (String face : studentPictures) {
			String studentFaceID = detect(face);
			studentFaceIDs.put(face, getStudentID(studentFaceID));
		}

		return compute(studentFaceIDs, classFaceIDs);
	}

	private HashMap<String, Double> compute(HashMap<String, String> students, ArrayList<String> pictureFaceIDs) {
		HashMap<String, Double> attendances = new HashMap<>();
		for (String key : students.keySet()) {
			Double accuracy = -1.0;
			String foundStudentKey = "";
			for (String id : pictureFaceIDs) {
				accuracy = compare(getJSONForCompare(students.get(key), id));
				if (accuracy > 0.0) {
					foundStudentKey = key;
					break;
				}
			}
			if (!foundStudentKey.equals("")) {
				pictureFaceIDs.remove(foundStudentKey);
				attendances.put(foundStudentKey, accuracy);
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

	private Double compare(String faces) {
		Double accuracy = -1.0;
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
						accuracy = Double.parseDouble(obj.get("confidence").toString());
						System.out.println("accuracy " + accuracy);
					}
				} else {
					System.out.println("Too many requests or student not found");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return accuracy;
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
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			result = jsonArray.getJSONObject(0).getString("faceId");
		} catch (org.json.JSONException e) {
			result = "";
		}
		return result;
	}

	private String getJSONForCompare(String faceId1, String faceId2) {
		return "{ \"faceId1\":\"" + faceId1 + "\", \"faceId2\": \"" + faceId2 + "\" }";
	}

}
