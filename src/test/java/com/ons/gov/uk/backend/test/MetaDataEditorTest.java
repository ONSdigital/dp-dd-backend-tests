package com.ons.gov.uk.backend.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.DimensionalAPI;
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
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class MetaDataEditorTest {
	Config config = new Config();
	DimensionalAPI dimensionalAPI = new DimensionalAPI();
	ObjectMapper mapper = new ObjectMapper();
	com.ons.gov.uk.model.DataResource dataResource = new DataResource();
	MetaDataEditorModel datasetMetadata = new MetaDataEditorModel();
	String dataResourceName = "TEST_Resource_E2E_Open_data";

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


	@Test(groups = {"createDR"})
	public void createDataResource() throws Exception {
//		RestAssured.baseURI = config.getMetadataEditor();
//		dataResource.setDataResourceID("TEST_" + RandomStringGen.getRandomString(10));
//		dataResource.setTitle("TEST_" + RandomStringGen.getRandomString(8));
//		dataResource.setMetadata(jsonMetaData);
//		ResponseBody responseBody = given().cookies("splash", "y").contentType("application/json").accept("application/json")
//				.body(mapper.writeValueAsString(dataResource)).post("/dataResource");
//		Assert.assertTrue(responseBody.asString().contains("Success"), "DataResource : " + dataResource.getDataResourceID() + " was not created.\n Error Message: "
//				+ responseBody.asString());
//		Assert.assertTrue(responseBody.asString().contains(dataResource.getDataResourceID()), "The response foes not contain the dataresource ID : " + dataResource.getDataResourceID() +
//				".\n Error Message: "
//				+ responseBody.asString());

	}

	@Test(groups = {"getAllDataResource"}, dependsOnGroups = {"createDR"}, enabled = false)
	public void getAllDataResource() throws Exception {
		RestAssured.baseURI = config.getMetadataEditor();
		boolean exists = false;
		ResponseBody responseBody = given().cookies("splash", "y").contentType("application/json").accept("application/json").expect()
				.statusCode(200).when()
				.get("/dataResources").body();
			ArrayList <DataResource> dataResources = (ArrayList) mapper.readValue(String.valueOf(responseBody.asString()),
					new TypeReference <List <DataResource>>() {
					});

			for (DataResource temp : dataResources) {
				if (temp.getDataResourceID().equals(dataResource.getDataResourceID())) {
					exists = true;
				}
			}
			Assert.assertTrue(exists, "DataResource with ID: " + dataResource.getDataResourceID()
					+ "that was created exists in the data resources list");
	}


	@Test(groups = {"findDataset"}, dependsOnGroups = {"getAllDataResource"}, enabled = false)
	public void findMyDataSetId() throws Exception {
		String service = config.getMetadataEditor() + "/metadatas";
		ResponseBody responseBody = given().cookies("splash", "y").contentType("application/json")
				.expect().statusCode(200).when().get(service).body();
			ArrayList <MetaDataEditorModel> metaDataEditorModels = (ArrayList) mapper.readValue(String.valueOf(responseBody.asString()),
					new TypeReference <List <MetaDataEditorModel>>() {
					});
			JSONArray itemsArray = dimensionalAPI.getItems("items");
			ArrayList <ItemsObj> itemsList = (ArrayList) mapper.readValue(String.valueOf(itemsArray),
					new TypeReference <List <ItemsObj>>() {
					});
			Assert.assertTrue(metaDataEditorModels.size() >= itemsList.size(), "FAILURE----  The number of Datasets in the API do not match with MetadataEditor");
			int counter = 0;
			for (MetaDataEditorModel metadata : metaDataEditorModels) {
				for (int index = 0; index < itemsList.size(); index++) {
					if (itemsList.get(index).getId().equals(metadata.getDatasetId())) {
						counter++;
					}

					if (itemsList.get(index).getTitle().equals(config.getFilepath())) {
						datasetId = itemsList.get(index).getId();
						minorVersion = Integer.toString(Integer.parseInt(metadata.getMinorVersion()) + 1);
						majorVersion = Integer.toString(Integer.parseInt(metadata.getMajorVersion()) + 1);
					}
				}
			}
			Assert.assertEquals(metaDataEditorModels.size(), counter, "Mismatch in dataset id between Metadata API and Metadata Editor ");

	}

	@Test(groups = {"setUpMetadata"}, dependsOnGroups = {"findDataset"}, enabled = false)
	public void setUpFormParams() throws Exception {
		desc = "Metadata_Test_" + RandomStringGen.getRandomString(25);
		contactName = "Metadata_Test_" + RandomStringGen.getRandomString(10);
		email = RandomStringGen.getRandomString(8);
		phone = RandomStringGen.getRandomLongNumber(8);
		jsonMetaData = jsonMetaData.replace("desc_to_replace", desc).
				replace("contact_name_to_replace", contactName).
				replace("email_to_replace", email).
				replace("number_to_replace", phone);

	}

	@Test(groups = "updatemetadata", dependsOnGroups = {"setUpMetadata"}, enabled = false)
	public void updateMetaData() throws Exception {
		datasetMetadata(datasetId, majorVersion, dataResource, minorVersion, jsonMetaData, "20" + RandomStringGen.getRandomString(17));
			String service = config.getMetadataEditor() + "/metadata/" + datasetId;
			ResponseBody responseBody = given().cookies("splash", "y").accept("application/json")
					.contentType("application/json").body(mapper.writeValueAsString(datasetMetadata))
					.put(service);

			Assert.assertTrue(responseBody.asString().contains("Success"), "Metadata for Dataset : " + datasetMetadata.getDatasetId() + " was not created.\n Error Message: "
					+ responseBody.asString());
			Assert.assertTrue(responseBody.asString().contains(datasetMetadata.getDatasetId()), "Metadata for Dataset : " + datasetMetadata.getDatasetId() + " was not updated.\n Error Message: "
					+ responseBody.asString());

	}

	public MetaDataEditorModel datasetMetadata(String datasetId, String majorVersion, DataResource dataResource, String minorVersion, String jsonMetaData,
	                                           String majorLabel) {
		datasetMetadata.setDatasetId(datasetId);
		datasetMetadata.setMajorVersion(majorVersion);
		datasetMetadata.setMinorVersion(minorVersion);
		datasetMetadata.setJsonMetadata(jsonMetaData);
		datasetMetadata.setDataResource(dataResource.getDataResourceID());
		datasetMetadata.setMajorLabel(majorLabel);
		return datasetMetadata;
	}

	@Test(groups = "verifymetadata", dependsOnGroups = "updatemetadata", enabled = false)
	public void verifyMetaDatainAPI(String title) throws Exception {
		Metadata metaDataValues = null;
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
