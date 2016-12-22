package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import scala.Array;
import scala.util.parsing.json.JSON;

import java.util.Arrays;
import java.util.Iterator;

import static io.restassured.RestAssured.expect;

public class EndPoint {
	Config config = new Config();
	RestAssured restAssured = new RestAssured();
	String responseString = null;
	ResponseBody responseBody;
	String jsonStr = "{\n" +
			"  \"items\": [\n" +
			"    {\n" +
			"      \"id\": \"88f28a15-66ae-4f2a-b397-52eed08988db\",\n" +
			"      \"title\": \"Open-Data-small.csv\",\n" +
			"      \"url\": \"http://localhost:8080/datasets/88f28a15-66ae-4f2a-b397-52eed08988db\",\n" +
			"      \"metadata\": {\n" +
			"        \"description\": \"No description available.\"\n" +
			"      },\n" +
			"      \"dimensionsUrl\": \"http://localhost:8080/datasets/88f28a15-66ae-4f2a-b397-52eed08988db/dimensions\"\n" +
			"    },\n" +
			"    {\n" +
			"      \"id\": \"36c3c45e-b26b-468f-9f0e-2edca8fea40a\",\n" +
			"      \"title\": \"small-example.csv\",\n" +
			"      \"url\": \"http://localhost:8080/datasets/36c3c45e-b26b-468f-9f0e-2edca8fea40a\",\n" +
			"      \"metadata\": {\n" +
			"        \"description\": \"No description available.\"\n" +
			"      },\n" +
			"      \"dimensionsUrl\": \"http://localhost:8080/datasets/36c3c45e-b26b-468f-9f0e-2edca8fea40a/dimensions\"\n" +
			"    }\n" +
			"  ],\n" +
			"  \"first\": \"http://localhost:8080/datasets?page=1&size=20\",\n" +
			"  \"last\": \"http://localhost:8080/datasets?page=0&size=20\",\n" +
			"  \"page\": 1,\n" +
			"  \"count\": 2,\n" +
			"  \"totalPages\": 0,\n" +
			"  \"itemsPerPage\": 20,\n" +
			"  \"startIndex\": 0,\n" +
			"  \"total\": 2\n" +
			"}";
	JSONParser parser = new JSONParser();

	public void checkEndPoint() {
		RestAssured.baseURI = config.getDatasetEndPoint();
		responseBody = expect().statusCode(200).when().get().body();
		responseString = responseBody.asString();
	}

	public boolean titleExists(String title) {
		boolean exists = false;
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseString);
			JSONArray jsonObject1 = (JSONArray) jsonObject.get("items");
			for (int i = 0; i < jsonObject1.size(); i++) {
				JSONObject jo = (JSONObject) jsonObject1.get(i);
				if (jo.get("title").equals(title)) {
					exists = true;
					break;
				}
				System.out.println(jo.get("title"));
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


}
