
package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.log4j.Logger;

@SuppressWarnings("unused")
@RestController
public class VisionController {

	final String ROOT_URI = "http://localhost:8080";
	Logger logger = Logger.getLogger(VisionController.class);
	JSONObject json = new JSONObject();
	JSONArray jsonarray = new JSONArray();
	String jsonString = new String();

	// String imageToAnalyze =
	// "https://upload.wikimedia.org/wikipedia/commons/1/12/Broadway_and_Times_Square_by_night.jpg";
	String imageToAnalyze = "C:\\Users\\admin\\Pictures\\error1.jpg";
	// String str1 = FileUtils.readFileToString(file);

	@SuppressWarnings("unchecked")
	@RequestMapping("/vision2")
	public MSVision vision(String imageToAnalyze) throws Exception {
		// String imageToAnalyze="C:\\Users\\admin\\Pictures\\error1.jpg";
		File file1 = new File("C:\\Users\\admin\\Documents\\MicrosoftVisionAPI\\subscriptionKey.txt");
		// String str1 = FileUtils.readFileToString(file);

		String uriBase = "https://westcentralus.api.cognitive.microsoft.com/vision/v2.0/analyze";
		String subscriptionKey = FileUtils.readFileToString(file1, "UTF-8");
		System.out.println(subscriptionKey);
		// imageToAnalyze =
		// "https://upload.wikimedia.org/wikipedia/commons/1/12/Broadway_and_Times_Square_by_night.jpg";
		imageToAnalyze = "C:\\Users\\admin\\Pictures\\error1.jpg";
		// public String uploadImage(String imageUrl) throws Exception {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		// try {
		URIBuilder builder = new URIBuilder(uriBase);

		// Request parameters. All of them are optional.
		builder.setParameter("visualFeatures", "Categories,Description,Color");
		builder.setParameter("language", "en");

		// Prepare the URI for the REST API call.
		URI uri = builder.build();
		HttpPost request = new HttpPost(uri);

		// Request headers.
		request.setHeader("Content-Type", "application/octet-stream"); // For Local File System Image
		// request.setHeader("Content-Type", "application/json");
		request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

		// Request body.
		File file = new File("C:\\Users\\admin\\Pictures\\error1.jpg");
		FileEntity reqEntity = new FileEntity(file);
		request.setEntity(reqEntity);

		// StringEntity requestEntity = new StringEntity("{\"url\":\"" + imageToAnalyze
		// + "\"}");
		// request.setEntity(requestEntity);

		// Make the REST API call and get the response entity.
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();

		// if (entity != null) {
		if (entity != null) {
			// Format and display the JSON response.
			// String jsonString = EntityUtils.toString(entity);
			jsonString = EntityUtils.toString(entity);
			// json = new JSONObject(jsonString);
			System.out.println("REST Response:\n");
			// System.out.println(json);
			System.out.println("JsonString " + jsonString);
			// op = json.toString();
		}
		logger.info("EntityResponse Displayed");
		System.out.println(jsonString);
		// } catch (Exception e) {
		// Display error message.
		// System.out.println("Exception is "+e.getMessage());
		// }

		// return new ComputerVision(1,json,imageToAnalyze);
		return new MSVision(91, imageToAnalyze, jsonString);

	}

	public MSVision vision() {
		return new MSVision(9, imageToAnalyze, jsonString);
	}

	/* AWS VisionAPI */
	String photo;
	List<Label> LabelDetection;

	@RequestMapping("/awsvision")
	public AWSModel awsvision(String photo, String LDetect) throws Exception {
		logger.info("AWS Controller is called");
		// List<Label> LabelDetection = null;
		photo = "multijeans.jpeg";
		String bucket = "instantweb2";
		AWSCredentials credentials;
		try {
			credentials = new ProfileCredentialsProvider("myProfile").getCredentials();
			logger.info("AWS Credentials loaded");
		} catch (Exception e) {
			logger.info("Credentials are not valid or not reachable");
			throw new AmazonClientException("Cannot load the credentials from thecredential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
		}

		AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

		// DetectTextRequest request = new DetectTextRequest().withImage(new
		// Image().withS3Object(new S3Object().withName(photo).withBucket(bucket)));

		DetectLabelsRequest request = new DetectLabelsRequest()
				.withImage(new Image().withS3Object(new S3Object().withName(photo).withBucket(bucket)));

		try {

			DetectLabelsResult result = rekognitionClient.detectLabels(request);
			List<Label> LabelDetection = result.getLabels();
			System.out.println("Detected lines and words for " + photo);
			System.out.println("LabelDetection " + LabelDetection);

			for (Label text : LabelDetection) {
				System.out.println("Name: " + text.getName());
				System.out.println("Confidence: " + text.getConfidence());
				System.out.println("Class: " + text.getClass());

			}
			LDetect = LabelDetection.toString();
			System.out.println("LDetect " + LDetect);
			logger.info("Image Details are displayed");
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

		// AWSVision obj = new AWSVision();
		// obj.aws(LabelDetection, photo);
		// System.out.println(obj);
		logger.info("AWS Controller return Json Output and PhotoId");
		return new AWSModel(photo, LDetect);

	}

}
