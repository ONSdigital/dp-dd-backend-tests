package com.ons.gov.uk;

import com.ons.gov.uk.core.Config;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;

import static io.restassured.RestAssured.expect;

public class EndPoint {
	Config config = new Config();
	RestAssured restAssured = new RestAssured();
	public JsonPath checkEndPoint(){
		RestAssured.baseURI = config.getDatasetEndPoint();
		ResponseBody responseBody =  expect().statusCode(200).when().get().body();
		return responseBody.jsonPath();
	}

	public static void main(String[] args) {
		EndPoint ep = new EndPoint();
		ep.checkEndPoint().get();

	}

}
