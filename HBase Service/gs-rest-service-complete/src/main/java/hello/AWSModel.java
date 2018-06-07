package hello;

import java.util.List;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.rekognition.model.Label;

@SuppressWarnings("unused")
public class AWSModel {

	private String photo;
	String LDetect;
	//@SuppressWarnings("rawtypes")
	//private List<Label> LabelDetection;

	@Autowired
	public AWSModel() {

	}

	@SuppressWarnings("rawtypes")
	//public AWSModel(String photo, List<Label> LabelDetection) {
	public AWSModel(String photo, String LDetect) {
		this.photo = photo;
		this.LDetect = LDetect;
	}

	public String getPhoto() {
		return photo;
	}

	public String getLDetect() {
		return LDetect;
	}

	public void setLDetect(String lDetect) {
		LDetect = lDetect;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

//	public List<Label> getLabelDetection() {
//		return LabelDetection;
//	}
//
//	public void setLabelDetection(List<Label> labelDetection) {
//		LabelDetection = labelDetection;
//	}

}
