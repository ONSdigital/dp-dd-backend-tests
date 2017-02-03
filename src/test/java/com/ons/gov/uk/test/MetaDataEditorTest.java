package com.ons.gov.uk.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.MetaDataEditor;
import com.ons.gov.uk.core.model.Items;
import com.ons.gov.uk.core.model.ItemsObj;
import com.ons.gov.uk.core.model.Metadata;
import com.ons.gov.uk.core.util.RandomStringGen;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class MetaDataEditorTest {
	MetaDataEditor metaDataEditor = new MetaDataEditor();
	DimensionalAPI dimensionalAPI = new DimensionalAPI();
	ObjectMapper mapper = new ObjectMapper();
	String majorVersion = "1";
	String minorVersion = "1";
	String datasetId = null;
	String jsonMetaData = "{\n" +
			"        \"description\": \"desc_to_replace\",\n" +
			"        \"contact\": {\n" +
			"          \"name\": \"contact_name_to_replace\",\n" +
			"          \"email\": \"email_to_replace@ons.gsi.gov.uk\",\n" +
			"          \"phone\": \"+44 (0)number_to_replace\"\n" +
			"        }}";
	String desc, contactName, email, phone;

	@BeforeTest
	public void setUpFormParams() throws Exception {
		JSONArray items = dimensionalAPI.getItems(dimensionalAPI.checkEndPoint(), "items");
		ItemsObj itemValue0 = mapper.readValue(String.valueOf(items.get(0)), new TypeReference <ItemsObj>() {
		});
		// just get the first item
		datasetId = (String) itemValue0.getId();
		desc = RandomStringGen.getRandomString(25);
		contactName = RandomStringGen.getRandomString(10);
		email = RandomStringGen.getRandomString(8);
		phone = RandomStringGen.getRandomLongNumber(8);
		jsonMetaData = jsonMetaData.replace("desc_to_replace", desc).
				replace("contact_name_to_replace", contactName).
				replace("email_to_replace", email).
				replace("number_to_replace", phone);

	}

	@Test(groups = "updatemetadata")
	public void updateMetaData() {
		metaDataEditor.datasetMetadata(datasetId, majorVersion, minorVersion, jsonMetaData);
		ResponseBody responseBody = metaDataEditor.callMetaDataEditor();
		Assert.assertTrue(((RestAssuredResponseImpl) responseBody).getStatusCode() == 201,
				"\n Expected Response Status for MetadataEditor :  201\n" +
						"\nActual Response Status from MetadataEditor :  " + ((RestAssuredResponseImpl) responseBody).getStatusCode());

	}

	@Test(groups = "verifymetadata", dependsOnGroups = "updatemetadata")
	public void verifyMetaDatainAPI() throws Exception {
		Metadata metaDataValues = new Metadata();
		Items dataSetItem = null;
		JSONArray allItems = dimensionalAPI.getItems(dimensionalAPI.checkEndPoint(), "items");
		List <ItemsObj> itemsList = mapper.readValue(String.valueOf(allItems), new TypeReference <List <ItemsObj>>() {
		});
		for (ItemsObj item : itemsList) {
			if (item.getId().equalsIgnoreCase(datasetId)) {
				metaDataValues = item.getMetadata();
				Assert.assertTrue(metaDataValues.getContact().getName().equalsIgnoreCase(contactName));
				Assert.assertTrue(metaDataValues.getContact().getEmail().contains(email));
				Assert.assertTrue(metaDataValues.getContact().getPhone().contains(phone));
				Assert.assertTrue(metaDataValues.getDescription().equalsIgnoreCase(desc));
			}
		}


	}


}
