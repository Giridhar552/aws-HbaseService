package hello;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
//import com.example.ComputerVision;
import com.example.Vision;

@SpringBootApplication
public class Application {

	static JSONArray JsonResp = null;
	static String imageUrl = null;
	static Logger logger = Logger.getLogger(Application.class);

	static String MSResp = new String();// Microsoft
	static String MSJsonResp = new String();// Microsoft
	static String imageToAnalyze = new String();// Microsoft

	static String photoId = new String();// aws
	static String LDetectResp = new String();// aws

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
		// putRequest();
		// putRequest2();
		putRequest3();
		HBaseConnection connection = new HBaseConnection(JsonResp);
		HbaseTemplate hbaseTemplate = connection.getHbaseTemplate();
		HBaseAdmin admin = new HBaseAdmin(hbaseTemplate.getConfiguration());
		//connection.createTable(admin);
		// connection.insertTable(admin);
		//connection.addData(hbaseTemplate, JsonResp, imageUrl);
		//connection.addMSData(hbaseTemplate, imageToAnalyze, MSJsonResp);
		connection.addAwsData(hbaseTemplate, photoId, LDetectResp);
		logger.info("AWS Data inserted into HBase");
		// User u = connection.getData(hbaseTemplate);
		// System.out.println("User : " + u);
		admin.close();

	}

	public static void putRequest() {
		// String url = "http://localhost:8080/user/{id}";
		String url = "http://localhost:8080/vision?imageUrl=C:\\Users\\admin\\Pictures\\error1.jpg";
		JSONObject json = new JSONObject();
		JSONArray jsonarray1 = new JSONArray();
		String ImageUrl = null;

		// Map<String, String> params = new HashMap<>(); params.put("id", "1");
		// json.put("Description", Description);

		json.put("Description", "Score");
		jsonarray1.add(json);

		// Greeting greeting = new Greeting(1,"content");

		Vision vision = new Vision(1, jsonarray1, ImageUrl);

		// User user = new User(1,"a","b");
		// user.setId(1);
		// user.setFirstName("John");
		// user.setLastName("Smith");

		RestTemplate restTemplate = new RestTemplate();
		System.out.println("user " + vision);
		System.out.println("RestTemplt " + restTemplate);

		// restTemplate.put(url, user, params);
		// greeting = restTemplate.getForObject(url, Greeting.class);
		// ResponseEntity<Greeting> getResponse = restTemplate.getForEntity(url,
		// Greeting.class);

		ResponseEntity<Vision> getResponse = restTemplate.getForEntity(url, Vision.class);
		logger.info("ResponseEntity Object Executed");
		System.out.println("GetResponse  " + getResponse);

		// ResponseEntity<String> getResponse1 =
		// restTemplate.getForEntity(url,String.class);

		System.out.println("RespBody: " + getResponse.getBody().getJsonarray());
		System.out.println("ImageUrl: " + getResponse.getBody().getImageUrl());
		JsonResp = getResponse.getBody().getJsonarray();
		imageUrl = getResponse.getBody().getImageUrl();

		// System.out.println("FName " +user.getFirstName()+" LName
		// "+user.getLastName());
	}

	public static void putRequest2() {
		// String url = "http://localhost:8080/user/{id}";
		String url = "http://localhost:8083/vision";
		// String url2 = "";
		JSONObject json = new JSONObject();
		JSONArray jsonarray1 = new JSONArray();
		String imageToAnalyze1 = new String();
		String jsonString = new String();

		// Map<String, String> params = new HashMap<>(); params.put("id", "1");
		// json.put("Description", Description);

		json.put("Description", "Score");
		jsonarray1.add(json);

		// Greeting greeting = new Greeting(1,"content");

		// Vision vision = new Vision(1, jsonarray1, ImageUrl);
		MSVision cvision = new MSVision(1, imageToAnalyze1, jsonString);

		// User user = new User(1,"a","b");
		// user.setId(1);
		// user.setFirstName("John");
		// user.setLastName("Smith");

		RestTemplate restTemplate = new RestTemplate();
		System.out.println("user " + cvision);
		System.out.println("RestTemplt " + restTemplate);

		// restTemplate.put(url, user, params);
		// greeting = restTemplate.getForObject(url, Greeting.class);
		// ResponseEntity<Greeting> getResponse = restTemplate.getForEntity(url,
		// Greeting.class);

		ResponseEntity<MSVision> getResponse = restTemplate.getForEntity(url, MSVision.class);
		// logger.info("ResponseEntity Object Executed");
		System.out.println("GetResponse  " + getResponse);

		// ResponseEntity<String> getResponse1 =
		// restTemplate.getForEntity(url,String.class);

		System.out.println("RespBody: " + getResponse.getBody());
		System.out.println("RespBody jsonString: " + getResponse.getBody().getJsonString());
		MSJsonResp = getResponse.getBody().getJsonString();
		imageToAnalyze = getResponse.getBody().getImageToAnalyze();
		// System.out.println("ImageUrl: " + getResponse.getBody().getImageUrl());
		// JsonResp = getResponse.getBody().getJsonarray();
		// imageUrl = getResponse.getBody().getImageUrl();

		// System.out.println("FName " +user.getFirstName()+" LName
		// "+user.getLastName());
	}

	public static void putRequest3() {
		System.out.println("Hello This is putRequest3");
		String url = "http://localhost:8083/awsvision";
		String photo = new String();
		String LDetect = new String();
		AWSModel awsModel = new AWSModel(photo, LDetect);

		RestTemplate restTemplate = new RestTemplate();
		System.out.println("user " + awsModel);

		ResponseEntity<AWSModel> getResponse = restTemplate.getForEntity(url, AWSModel.class);
		logger.info("ResponseEntity Object of AWS Executed");
		System.out.println("GetResponse  " + getResponse);

		LDetectResp = getResponse.getBody().getLDetect();
		System.out.println("RespBody String: " + LDetectResp);

		photoId = "aws_" + getResponse.getBody().getPhoto();
		System.out.println("RespBody Photo " + photoId);

	}

}
