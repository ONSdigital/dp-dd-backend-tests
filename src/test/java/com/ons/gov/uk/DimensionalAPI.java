package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import static io.restassured.RestAssured.expect;

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
		RestAssured.baseURI = config.getDatasetEndPoint();
		responseBody = expect().statusCode(200).when().get().body();
		responseString = responseBody.asString();
		return responseString;
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
