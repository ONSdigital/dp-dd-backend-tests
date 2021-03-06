package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

	public String checkEndPoint(String param) {
		RestAssured.urlEncodingEnabled = true;
		RestAssured.baseURI = config.getEndPointReal();
		responseBody = given().cookies("splash", "y").expect().statusCode(200).when().get("/versions?" + param).body();
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
		try {
			JSONArray jsonObject1 = getItems("items");
			for (int i = 0; i < jsonObject1.size(); i++) {
				JSONObject jo = (JSONObject) jsonObject1.get(i);
				if (jo.get("title").toString().contains(title)) {
					exists = true;
					break;
				}
			}


		} catch (ParseException e) {
			e.printStackTrace();
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

	public int dataSetTotal() {
		int totalDataSets = 0;
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseString);
			totalDataSets = Integer.parseInt(jsonObject.get("total").toString());
		} catch (ParseException ee) {
			ee.printStackTrace();
		}
		return totalDataSets;
	}

	public String waitForApiToLoad(String csvFile, String title) {
		int counter = 0;
		try {
			while ((!titleExists(title) && (!titleExists(csvFile))) && counter < 100) {
				Thread.sleep(2000);
				checkEndPoint();
				counter++;
			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return responseString;
	}

	public String callTheLink(String url) {
		url = URLDecoder.decode(url);
		return given().cookies("splash", "y").expect().statusCode(200).when().get(url).body().asString();

	}

	public String getDatasetid(String csvFileName, String title) throws Exception {
		String datasetid = null;
		JSONArray itemsArray = getItems("items");
		for (int i = 0; i < itemsArray.size(); i++) {
			JSONObject jo = (JSONObject) itemsArray.get(i);
			if (jo.get("title").toString().contains(csvFileName) ||
					jo.get("title").toString().contains(title)) {
				datasetid = jo.get("id").toString();
				break;
			}
		}
		return datasetid;
	}

}
