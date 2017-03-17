//package com.ons.gov.uk.frontend.pages;
//
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.restassured.RestAssured;
//import io.restassured.response.ResponseBody;
//import org.testng.Assert;
//import uk.gov.ons.dd.frontend.core.Configuration;
//import uk.gov.ons.dd.frontend.model.DataResource;
//import uk.gov.ons.dd.frontend.model.MetaDataEditorModel;
//import uk.gov.ons.dd.frontend.util.RandomStringGen;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static io.restassured.RestAssured.given;
//
//public class MetaDataEditor {
//	public Co config = new Configuration();
//	ObjectMapper mapper = new ObjectMapper();
//	DataResource dataResource = new DataResource();
//	MetaDataEditorModel datasetMetadata = new MetaDataEditorModel();
//
//	String majorVersion = "1";
//	String minorVersion = "1";
//
//	String jsonMetaData = "{\n" +
//			"        \"description\": \"desc_to_replace\",\n" +
//			"        \"contact\": {\n" +
//			"          \"name\": \"contact_name_to_replace\",\n" +
//			"          \"email\": \"email_to_replace@ons.gsi.gov.uk\",\n" +
//			"          \"phone\": \"+44 (0)number_to_replace\"\n" +
//			"        }}";
//	String desc, contactName, email, phone;
//
//
//	public void createDataResource(String title) {
//		RestAssured.baseURI = config.getMetadataEditor();
//		dataResource.setDataResourceID("TEST_Resource_" + title);
//		dataResource.setTitle(title);
//		desc = "This is the sample dataset for " + title;
//		contactName = "Develop Test";
//		email = "email" + title;
//		phone = "020" + String.valueOf(RandomStringGen.getRandomInt(6));
//		jsonMetaData = jsonMetaData.replace("desc_to_replace", desc).replace("contact_name_to_replace", contactName)
//				.replace("email_to_replace", email).replace("number_to_replace", phone);
//		dataResource.setMetadata(jsonMetaData);
//		ResponseBody responseBody = null;
//		try {
//			responseBody = given().cookies("splash", "y").contentType("application/json").accept("application/json")
//					.body(mapper.writeValueAsString(dataResource)).post("/dataResource");
//		} catch (Exception ee) {
//		}
//		Assert.assertTrue(responseBody.asString().contains("Success"), "DataResource : " + dataResource.getDataResourceID() + " was not created.\n Error Message: "
//				+ responseBody.asString());
//		Assert.assertTrue(responseBody.asString().contains(dataResource.getDataResourceID()), "The response does not contain the dataresource ID : " + dataResource.getDataResourceID() +
//				".\n Error Message: "
//				+ responseBody.asString());
//
//	}
//
//
//	public DataResource findMyDataResource(String resourceId) {
//		RestAssured.baseURI = config.getMetadataEditor();
//		DataResource dataResource = null;
//		ResponseBody responseBody = given().cookies("splash", "y").contentType("application/json").accept("application/json").expect()
//				.statusCode(200).when()
//				.get("/dataResources").body();
//		try {
//			ArrayList <DataResource> dataResources = (ArrayList) mapper.readValue(String.valueOf(responseBody.asString()),
//					new TypeReference <List <DataResource>>() {
//					});
//
//			for (DataResource temp : dataResources) {
//				if (temp.getDataResourceID().equals(resourceId)) {
//					dataResource = temp;
//				}
//			}
//
//		} catch (Exception ee) {
//			ee.printStackTrace();
//			Assert.fail();
//		}
//		return dataResource;
//	}
//
//
//	public MetaDataEditorModel verifyDataSetExists(String filename) {
//		MetaDataEditorModel metadataModel = null;
//		String service = config.getMetadataEditor() + "/metadatas";
//		ResponseBody responseBody = given().cookies("splash", "y").contentType("application/json")
//				.expect().statusCode(200).when().get(service).body();
//		try {
//			ArrayList <MetaDataEditorModel> metaDataEditorModels = (ArrayList) mapper.readValue(String.valueOf(responseBody.asString()),
//					new TypeReference <List <MetaDataEditorModel>>() {
//					});
//			for (MetaDataEditorModel metaDataEditorModel : metaDataEditorModels) {
//				if (metaDataEditorModel.getTitle().equals(filename)) {
//					metadataModel = metaDataEditorModel;
//					break;
//				}
//			}
//		} catch (Exception ee) {
//		}
//		return metadataModel;
//	}
//
//
//	public boolean mapMetaData(String datasetId, String title) {
//		desc = "This is the sample dataset for " + title;
//		contactName = "Develop Test";
//		email = "email" + title;
//		phone = "020" + String.valueOf(RandomStringGen.getRandomInt(6));
//		jsonMetaData = jsonMetaData.replace("desc_to_replace", desc).replace("contact_name_to_replace", contactName)
//				.replace("email_to_replace", email).replace("number_to_replace", phone);
//		datasetMetadata(datasetId, majorVersion, "TEST_Resource_" + title, minorVersion, jsonMetaData, "2016", title);
//		ResponseBody responseBody = null;
//		try {
//			String service = config.getMetadataEditor() + "/metadata/" + datasetId;
//			responseBody = given().cookies("splash", "y").accept("application/json")
//					.contentType("application/json").body(mapper.writeValueAsString(datasetMetadata))
//					.put(service);
//		} catch (Exception ee) {
//		}
//		return responseBody.asString().contains("Success");
//
//	}
//
//	public MetaDataEditorModel datasetMetadata(String datasetId, String majorVersion, String resourceId, String minorVersion, String jsonMetaData,
//	                                           String majorLabel, String title) {
//		datasetMetadata.setDatasetId(datasetId);
//		datasetMetadata.setMajorVersion(majorVersion);
//		datasetMetadata.setMinorVersion(minorVersion);
//		datasetMetadata.setJsonMetadata(jsonMetaData);
//		datasetMetadata.setDataResource(resourceId);
//		datasetMetadata.setMajorLabel(majorLabel);
//		datasetMetadata.setTitle(title);
//		return datasetMetadata;
//	}
//
//
//}
