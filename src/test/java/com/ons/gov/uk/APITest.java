package com.ons.gov.uk;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.Test;

public class APITest {
	DimensionalAPI dimensionalAPI = new DimensionalAPI();
	JSONParser parser = new JSONParser();
	String responseFromAPI = null;

	public APITest() {
		responseFromAPI = dimensionalAPI.checkEndPoint();
	}

	@Test
	public void assertNotNullFields() {
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseFromAPI);
			String first = jsonObject.get("first").toString();
			String last = jsonObject.get("last").toString();
			String page = jsonObject.get("page").toString();
			String count = jsonObject.get("count").toString();
			String totalPages = jsonObject.get("totalPages").toString();
			String itemsPerPage = jsonObject.get("itemsPerPage").toString();
			String startIndex = jsonObject.get("startIndex").toString();
			String total = jsonObject.get("total").toString();

			Assert.assertNotNull(first, message("first", first));
			Assert.assertNotNull(last, message("last", last));
			Assert.assertNotNull(page, message("page", page));
			Assert.assertNotNull(count, message("count", count));
			Assert.assertNotNull(total, message("total", total));
			Assert.assertNotNull(totalPages, message("totalPages", totalPages));
			Assert.assertNotNull(itemsPerPage, message("itemsPerPage", itemsPerPage));
			Assert.assertNotNull(startIndex, message("startIndex", startIndex));

			//		System.out.println(first +"****"+last+"****"+  page+"****"+count+"****"+total+"****"+totalPages+"****"+itemsPerPage+"****"+startIndex);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	@Test
	public void assertDataSetCount() {
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(responseFromAPI);
			JSONArray jsonObject1 = (JSONArray) jsonObject.get("items");
			String count = jsonObject.get("count").toString();
			String total = jsonObject.get("total").toString();
			int numberOfItems = jsonObject1.size();
			Assert.assertEquals(numberOfItems, Integer.parseInt(count), "Number of datasets :" +
					numberOfItems + "\n" + "Value of Count :" + count);
			Assert.assertEquals(numberOfItems, Integer.parseInt(count), "Number of datasets :" +
					numberOfItems + "\n" + "Value of Total :" + total);
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	public String message(String key, String value) {
		return "The value for the key : " + key + "  is :" + value;
	}

}
