package com.ons.gov.uk;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.model.DataResource;
import com.ons.gov.uk.model.ItemsObj;
import com.ons.gov.uk.model.MetaDataEditorModel;
import com.ons.gov.uk.model.Metadata;
import com.ons.gov.uk.util.RandomStringGen;
import io.restassured.RestAssured;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class MetaDataSetUp {

	// check for data resource
	ObjectMapper mapper = new ObjectMapper();
	Config config = new Config();
	String jsonMetaData = "{\n" +
			"        \"description\": \"desc_to_replace\",\n" +
			"        \"contact\": {\n" +
			"          \"name\": \"contact_name_to_replace\",\n" +
			"          \"email\": \"email_to_replace@ons.gsi.gov.uk\",\n" +
			"          \"phone\": \"+44 (0)number_to_replace\"\n" +
			"        }}";
	String desc, contactName, email, phone;

	public static void main(String[] args) {

	}

	public boolean doesDataResourceExist(String dataResName) throws Exception {
		boolean dataResourceExists = false;
		String service = config.getMetadataEditor() + "/dataResources";
		ResponseBody responseBody = given().cookies("splash", "y").contentType("application/json")
				.expect().statusCode(200).when().get(service).body();
		ArrayList <DataResource> dataResourceArrayList = (ArrayList) mapper.readValue(String.valueOf(responseBody.asString()),
				new TypeReference <List <DataResource>>() {
				});

		for (DataResource dataResource : dataResourceArrayList) {
			if (dataResource.getDataResourceID().equalsIgnoreCase(dataResName)) {
				dataResourceExists = true;
				break;
			}
		}
		return dataResourceExists;
	}

	public void createDataResource(String dataResName, String title) throws Exception {
		DataResource dataResource = new DataResource();
		RestAssured.baseURI = config.getMetadataEditor();
		dataResource.setDataResourceID(dataResName);
		dataResource.setTitle(title);
		getJsonMetaData("Data Resource :" + dataResName + " Title " + title);
		dataResource.setMetadata(jsonMetaData);
		ResponseBody responseBody = given().cookies("splash", "y").contentType("application/json").accept("application/json")
				.body(mapper.writeValueAsString(dataResource)).post("/dataResource");
		Assert.assertTrue(responseBody.asString().contains("Success"), "DataResource : " + dataResource.getDataResourceID() + " was not created.\n Error Message: "
				+ responseBody.asString());
		Assert.assertTrue(responseBody.asString().contains(dataResource.getDataResourceID()), "The response foes not contain the dataresource ID : " + dataResource.getDataResourceID() +
				".\n Error Message: "
				+ responseBody.asString());

	}

	public boolean isTheMetaDataMapped(String title) throws Exception {
		boolean metadataMapped = false;
		DimensionalAPI dimensionalAPI = new DimensionalAPI();
		Metadata metaDataValues = null;
		JSONArray allItems = dimensionalAPI.getItems(dimensionalAPI.checkEndPoint(), "items");
		List <ItemsObj> itemsList = mapper.readValue(String.valueOf(allItems), new TypeReference <List <ItemsObj>>() {
		});
		for (ItemsObj item : itemsList) {
			Metadata metadata = item.getMetadata();
			try {
				if (metadata.getDescription().contains(title)) {
					metadataMapped = true;
					break;
				}
			} catch (Exception ee) {
				System.out.println("Metadata is blank. Not mapped");
			}
		}
		return metadataMapped;
	}

	public void updateMetaData(String filename, String title, String dataResName) throws Exception {
		DimensionalAPI dimensionalAPI = new DimensionalAPI();
		String datasetId = null;
		JSONArray allItems = dimensionalAPI.getItems(dimensionalAPI.checkEndPoint(), "items");
		List <ItemsObj> itemsList = mapper.readValue(String.valueOf(allItems), new TypeReference <List <ItemsObj>>() {
		});
		for (ItemsObj item : itemsList) {
			if (item.getTitle().contains(title) || item.getTitle().contains(filename)) {
				datasetId = item.getId();
				break;
			}
		}
		DataResource dataResource = new DataResource();
		dataResource.setTitle(title);
		dataResource.setDataResourceID(dataResName);
		getJsonMetaData(title);
		MetaDataEditorModel datasetMetadata = datasetMetadata(datasetId, title, "1", dataResource, "1", jsonMetaData, "2017");
		String service = config.getMetadataEditor() + "/metadata/" + datasetId;
		ResponseBody responseBody = given().cookies("splash", "y").accept("application/json")
				.contentType("application/json").body(mapper.writeValueAsString(datasetMetadata))
				.put(service);

		Assert.assertTrue(responseBody.asString().contains("Success"), "Metadata for Dataset : " + datasetMetadata.getDatasetId() + " was not created.\n Error Message: "
				+ responseBody.asString());
		Assert.assertTrue(responseBody.asString().contains(datasetMetadata.getDatasetId()), "Metadata for Dataset : " + datasetMetadata.getDatasetId() + " was not updated.\n Error Message: "
				+ responseBody.asString());
	}

	public MetaDataEditorModel datasetMetadata(String datasetId, String title, String majorVersion, DataResource dataResource, String minorVersion, String jsonMetaData,
	                                           String majorLabel) {
		MetaDataEditorModel datasetMetadata = new MetaDataEditorModel();
		datasetMetadata.setDatasetId(datasetId);
		datasetMetadata.setMajorVersion(majorVersion);
		datasetMetadata.setMinorVersion(minorVersion);
		datasetMetadata.setJsonMetadata(jsonMetaData);
		datasetMetadata.setDataResource(dataResource.getDataResourceID());
		datasetMetadata.setMajorLabel(majorLabel);
		datasetMetadata.setTitle(title);
		return datasetMetadata;
	}

	public void getJsonMetaData(String desc) {
		contactName = "Metadata_Test_" + RandomStringGen.getRandomString(10);
		email = RandomStringGen.getRandomString(8);
		phone = RandomStringGen.getRandomLongNumber(8);
		jsonMetaData = jsonMetaData.replace("desc_to_replace", desc).
				replace("contact_name_to_replace", contactName).
				replace("email_to_replace", email).
				replace("number_to_replace", phone);

	}
}
