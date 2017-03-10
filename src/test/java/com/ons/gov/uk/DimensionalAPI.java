package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLDecoder;

import static io.restassured.RestAssured.given;


public class DimensionalAPI {
	Config config = new Config();
	RestAssured restAssured = new RestAssured();

	String responseString = null;
	ResponseBody responseBody;
	JSONParser parser = new JSONParser();

	public String returnResponseAsString() {
		return responseString;
	}

	public String checkEndPoint() {
		RestAssured.urlEncodingEnabled = true;
		String endPoint = config.getEndPointReal();
		RestAssured.baseURI = endPoint;
		responseBody = given().cookies("splash", "y").expect().statusCode(200).when().get("/versions").body();
		responseString = responseBody.asString();
		return responseString;
	}

	public String checkEndPoint(String endPoint) {
		RestAssured.urlEncodingEnabled = true;
		RestAssured.baseURI = endPoint;
		responseBody = given().cookies("splash", "y").expect().statusCode(200).when().get("/versions").body();
		responseString = responseBody.asString();
		return responseString;
	}

	public JSONArray getItems(String keyArray) throws Exception {
		if (responseString == null) {
			checkEndPoint();
		}
		return (JSONArray) ((JSONObject) parser.parse(responseString)).get(keyArray);
	}

	//
	public JSONArray getItems(String url, String keyArray) throws Exception {
		if (url == null) {
			checkEndPoint();
			url = responseString;
		}
		return (JSONArray) ((JSONObject) parser.parse(url)).get(keyArray);
	}


	public JSONArray getValue(String jsonString) throws Exception {
		return (JSONArray) parser.parse(jsonString);
	}

	public String getValueForField(String jsonString, String field) throws Exception {
		return ((JSONObject) getValue(jsonString).get(0)).get(field).toString();
	}

	public boolean titleExists(String title) throws Exception {
		boolean exists = false;
			JSONArray jsonObject1 = getItems("items");
			for (int i = 0; i < jsonObject1.size(); i++) {
				JSONObject jo = (JSONObject) jsonObject1.get(i);
				if (jo.get("title").toString().contains(title)) {
					exists = true;
					break;
				}
			}
		return exists;
	}

	public String returnJson() {
		if (responseString != null) {
			return responseString;
		} else {
			return "Nothing to display. Call the API first";
		}
	}

	public int dataSetTotal() throws Exception {
		int totalDataSets = 0;
		JSONObject jsonObject = (JSONObject) parser.parse(responseString);
		totalDataSets = Integer.parseInt(jsonObject.get("total").toString());
		return totalDataSets;
	}

	public String waitForApiToLoad(String title) throws Exception {
		int counter = 0;
			while (!titleExists(title) && counter < 10) {
				Thread.sleep(2000);
				checkEndPoint();
				counter++;
			}
		return responseString;
	}

	public String callTheLink(String url) {
		url = URLDecoder.decode(url);
		return given().cookies("splash", "y").expect().statusCode(200).when().get(url).body().asString();

	}


}
