package com.ons.gov.uk;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class SmallExampleTest {
	Chopper chopper = new Chopper();
	DimensionalAPI dimAPI = new DimensionalAPI();
	JSONParser parser = new JSONParser();
	String responseFromAPI = null;
	String csvFile = "small-example.csv";
	String dimUrl = null;
	ArrayList <String> expectedDims = new ArrayList <String>();
	String naceOption = "08 - Other mining and quarrying";
	HashMap <String, String> expectedProdElementsMap = new HashMap <String, String>();
	String naceDimUrl = null;
	String prodElementsUrl = null;

	@BeforeTest
	public void init() {
		if (!dimAPI.waitForApiToLoad(csvFile).contains(csvFile)) {
			chopper.callChop(csvFile);
		}
		responseFromAPI = dimAPI.waitForApiToLoad(csvFile);
		getDimensionUrl();
		expectedDims.add("NACE");
		expectedDims.add("Prodcom Elements");
		expectedProdElementsMap.put("3", "All other income");
		expectedProdElementsMap.put("5", "Merchanted goods");
		expectedProdElementsMap.put("4", "Total UK manufacturer sales of products in this product group");
		expectedProdElementsMap.put("2", "UK manufacturer sales LABEL");
		expectedProdElementsMap.put("6", "Work done");

	}

	public void getDimensionUrl() {
		try {
			Assert.assertTrue(dimAPI.titleExists(csvFile));
			JSONArray itemsArray = dimAPI.getItems("items");
			for (int i = 0; i < itemsArray.size(); i++) {
				JSONObject jo = (JSONObject) itemsArray.get(i);
				if (jo.get("title").equals(csvFile)) {
					dimUrl = jo.get("dimensionsUrl").toString();
					break;
				}
			}


		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	@Test(groups = {"dimension"})
	public void getDimensions() {
		String jsonString = dimAPI.callTheLink(dimUrl);
		try {
			JSONArray dimArray = dimAPI.getValue(jsonString);
			for (int index = 0; index < dimArray.size(); index++) {
				Assert.assertTrue(expectedDims.contains(((JSONObject) dimArray.get(index)).get("name").toString()));
				Assert.assertTrue(expectedDims.contains(((JSONObject) dimArray.get(index)).get("id").toString()));
				if (((JSONObject) dimArray.get(index)).get("name").toString().equalsIgnoreCase("NACE")) {
					naceDimUrl = ((JSONObject) dimArray.get(index)).get("url").toString();
				} else {
					prodElementsUrl = ((JSONObject) dimArray.get(index)).get("url").toString();
				}

			}
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	@Test(groups = {"nace"}, dependsOnGroups = {"dimension"})
	public void testNaceDimOptions() {
		JSONArray dimArray = returnDimOptions(naceDimUrl);
		for (int index = 0; index < dimArray.size(); index++) {
			System.out.println(((JSONObject) dimArray.get(index)).get("name").toString());
			System.out.println(((JSONObject) dimArray.get(index)).get("id").toString());
			Assert.assertTrue(((JSONObject) dimArray.get(index)).get("name").toString().equalsIgnoreCase(naceOption));
			Assert.assertTrue(((JSONObject) dimArray.get(index)).get("id").toString().equalsIgnoreCase("1"));
		}
	}

	@Test(groups = {"prodelements"}, dependsOnGroups = {"dimension"})
	public void testprodElementOptions() {
		JSONArray dimArray = returnDimOptions(prodElementsUrl);
		for (int index = 0; index < dimArray.size(); index++) {
			Assert.assertTrue(expectedProdElementsMap.containsKey(((JSONObject) dimArray.get(index)).get("id").toString()));
			Assert.assertTrue(expectedProdElementsMap.get(((JSONObject) dimArray.get(index)).get("id").toString())
							.equals(((JSONObject) dimArray.get(index)).get("name").toString()),
					"Expected Value : " + expectedProdElementsMap.get(((JSONObject) dimArray.get(index)).get("id").toString())
							+ "\n Actual Value : " + ((JSONObject) dimArray.get(index)).get("name").toString());
		}
	}

	public JSONArray returnDimOptions(String url) {

		String jsonString = dimAPI.callTheLink(url);
		JSONArray optionArray = null;
		try {
			optionArray = dimAPI.getItems(jsonString, "options");
			//		optionArray = dimAPI.getItems("options");
		} catch (Exception ee) {
			ee.printStackTrace();
		}
		return optionArray;
	}

}
