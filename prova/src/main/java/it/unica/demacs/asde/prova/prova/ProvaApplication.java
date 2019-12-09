package it.unica.demacs.asde.prova.prova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//This sample uses Apache HttpComponents:
//http://hc.apache.org/httpcomponents-core-ga/httpcore/apidocs/
//https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONObject;

@SpringBootApplication
public class ProvaApplication {

	// key1 8ac7a514e86048a087f9fca5180a0981
	// key2 833a3a83f8f140aa8c7ffba6ae59e143
	// endpoint https://westcentralus.api.cognitive.microsoft.com/face/v1.0

	// faceid kiello "faceId": "c38de3fc-66aa-4bb3-b5c9-c33efbb63c2d"
	// faceid cristian "faceId": "42877808-8211-4468-b675-39b0473aa91d"

	// Replace <Subscription Key> with your valid subscription key.
	private static final String subscriptionKey = "8ac7a514e86048a087f9fca5180a0981";
	private static final String uriBase = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";

	// private static final String imageWithFaces =
	// "{\"url\":\"https://upload.wikimedia.org/wikipedia/commons/c/c3/RH_Louise_Lillian_Gish.jpg\"}";

	// private ArrayList<String> images;

	private static final String imageWithFaces1 = "assets/kiello.jpeg";
	private static final String imageWithFaces2 = "assets/cristian.jpeg";
	private static final String imageWithFaces3 = "assets/classe.jpeg";

	private static final String faceAttributes = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

	public static void main(String[] args) {
		HttpClient httpclient = HttpClientBuilder.create().build();

		ArrayList<String> students = new ArrayList<String>();

		try {
			URIBuilder builder = new URIBuilder(uriBase);

			// Request parameters. All of them are optional.
			builder.setParameter("returnFaceId", "true");
			builder.setParameter("returnFaceLandmarks", "false");
			builder.setParameter("returnFaceAttributes", faceAttributes);

			// Prepare the URI for the REST API call.
			URI uri = builder.build();

			HttpPost request = new HttpPost(uri);

			// Request headers.
			request.setHeader("Content-Type", "application/octet-stream"); // da cambiare in base all'input
			request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
			// Request body.
			// StringEntity reqEntity = new StringEntity(imageWithFaces);
			File file = new File(imageWithFaces1);

			FileEntity reqEntity = new FileEntity(file);
			request.setEntity(reqEntity);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// Format and display the JSON response.
				System.out.println("REST Response:\n");

				String jsonString = EntityUtils.toString(entity).trim();

				if (jsonString.charAt(0) == '[') {
					JSONArray jsonArray = new JSONArray(jsonString);
					// System.out.println(jsonArray.getJSONObject(0).getString("faceId"));
					System.out.println(jsonArray.toString(2));

				} else if (jsonString.charAt(0) == '{') {
					JSONObject jsonObject = new JSONObject(jsonString);
					System.out.println(jsonObject.toString(2));
				} else {
					System.out.println(jsonString);
				}
			}
		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		SpringApplication.run(ProvaApplication.class, args);
	}
}