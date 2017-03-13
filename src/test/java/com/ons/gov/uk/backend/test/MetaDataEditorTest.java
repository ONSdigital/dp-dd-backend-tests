package com.ons.gov.uk.backend.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.DimensionValues;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.frontend.pages.BasePage;
import com.ons.gov.uk.model.*;
import com.ons.gov.uk.util.RandomStringGen;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.restassured.RestAssured.given;
import static java.util.Collections.singleton;

public class MetaDataEditorTest {
	public Config config = new Config();
	DimensionalAPI dimensionalAPI = new DimensionalAPI();
	ObjectMapper mapper = new ObjectMapper();
	DataResource dataResource = new DataResource();
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


	public static class FileUploader extends BasePage {
		public static final String OS_NAME = System.getProperty("os.name");
		private static String librariesFolder = "libraries/";

		private By fileUpload = By.id("file");
		private By upload = By.name("submit");


		public void uploadFile() {

			File fileToUpload = new File("src/test/resources/csvs/" + new Config().getFilepath());
			String filePath = fileToUpload.getAbsolutePath();
			System.out.println("File to be uploaded ***  " + filePath);
			getDriver().get(new Config().getFileuploader());
			getDriver().findElement(fileUpload).sendKeys(filePath);
			getDriver().findElement(upload).click();
			getDriver().close();
			getDriver().quit();
		}

	}

	public static class JobCreator {
		private final int SLEEP_TIMER = 1000;
		public String fileName = null;
		Config config = new Config();
		RestAssured restAssured = new RestAssured();
		int loopCounter = 60;

		public String request(String dataSetId, ConcurrentHashMap <String, ArrayList <DimensionValues>> filters) throws JsonProcessingException {
			CreateJob request = new CreateJob();
			List <DimensionFilter> dimensions = new ArrayList <>();
			request.setDataSetId(dataSetId);
			for (String key : filters.keySet()) {
				List <DimensionValues> dimensionValues = filters.get(key);
				ArrayList <String> codes = new ArrayList <>();
				for (DimensionValues dim : dimensionValues) {
					codes.add(dim.getCodeId());
				}
				dimensions.add(new DimensionFilter(key, codes));
			}

			Set <FileFormat> formats = singleton(FileFormat.CSV);
			request.setDimensions(dimensions);
			request.setFileFormats(formats);

			try {
				System.out.println(new ObjectMapper().writeValueAsString(request));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return new ObjectMapper().writeValueAsString(request);
		}

		public String getJobID(String jsonStr, boolean failFast) throws Exception {
			RestAssured.baseURI = config.getJobCreator();
			String dataSetId = null;
			Response response = given().cookies("splash", "y")
					.contentType("application/json").body(jsonStr).post("/job");
			if (failFast) {
				org.junit.Assert.assertTrue("Response should fail with an error " + response.getStatusCode(), response.getStatusCode() >= 400);
			}
			try {
				dataSetId = ((JSONObject) new JSONParser().parse(response.asString())).get("id").toString();
			} catch (Exception ee) {
				if (!failFast) {
					while (loopCounter != 0) {

						Thread.sleep(SLEEP_TIMER);
						loopCounter--;
						dataSetId = getJobID(jsonStr, false);
					}
				}
			}
			return dataSetId;
		}

		public String returnCSVUrl(String jobID) throws Exception {
			String urlToDownloadFile = null;
			RestAssured.baseURI = config.getJobCreator();
			Response response = given().cookies("splash", "y").contentType("application/json").get("/job/" + jobID);
			System.out.println("from returncsv " + response.asString());
			String status = ((JSONObject) new JSONParser().parse(response.asString())).get("status").toString();
			if (status.equalsIgnoreCase("Complete")) {
				JSONArray getFiles = (JSONArray) ((JSONObject) new JSONParser().parse(response.asString())).get("files");
				for (int i = 0; i < getFiles.size(); i++) {
					JSONObject jo = (JSONObject) getFiles.get(i);
					if (jo.get("url").toString() != null) {
						urlToDownloadFile = jo.get("url").toString();
						fileName = jo.get("name").toString();
						break;
					} else if (fileName == null) {
						System.out.println("Filename is null");
						urlToDownloadFile = waitForURL(jobID);
					}
				}

			} else {
				System.out.println("status is not complete");
				urlToDownloadFile = waitForURL(jobID);
			}

			return urlToDownloadFile;
		}

		public String waitForURL(String jobID) {
			try {
				while (loopCounter != 0) {
					loopCounter--;
					Thread.sleep(SLEEP_TIMER);
					return returnCSVUrl(jobID);

				}
			} catch (Exception ee) {
			}
			return null;
		}


	}
}
