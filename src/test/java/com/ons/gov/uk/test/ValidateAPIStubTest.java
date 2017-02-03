package com.ons.gov.uk.test;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.DimensionalAPI;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.core.model.DataSet;
import com.ons.gov.uk.core.model.Dimension;
import com.ons.gov.uk.core.model.Items;
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class ValidateAPIStubTest {

	DimensionalAPI dimensionalAPI = new DimensionalAPI();
	String apiDimUrl = null, stubDimUrl = null;
	ObjectMapper mapper = new ObjectMapper();
	DataSet realDataset = null, stubDataSet = null;
	List <Items> itemsAPI = null, itemsStub = null;
	SoftAssert softAssert;
	String dataListAPI = null, dataListStub = null;
	String apiDimOptions = null, stubDimOptions = null;

	@BeforeTest
	public void init() throws Exception {
		dataListAPI = dimensionalAPI.checkEndPoint(new Config().getEndPointReal());
		dataListStub = dimensionalAPI.checkEndPoint(new Config().getEndPointStub());
		JSONArray itemRespApi = dimensionalAPI.getItems(dataListAPI, "items");
		JSONArray itemRespStub = dimensionalAPI.getItems(dataListStub, "items");
		apiDimUrl = dimensionalAPI.getValueForField(itemRespApi.toJSONString(), "url");
		stubDimUrl = dimensionalAPI.getValueForField(itemRespStub.toJSONString(), "url");
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		realDataset = mapper.readValue(dataListAPI, new TypeReference <DataSet>() {
		});
		itemsAPI = mapper.readValue(String.valueOf(itemRespApi), new TypeReference <List <Items>>() {
		});
		stubDataSet = mapper.readValue(dataListStub, new TypeReference <DataSet>() {
		});
		itemsStub = mapper.readValue(String.valueOf(itemRespStub), new TypeReference <List <Items>>() {
		});

	}


	@Test(groups = "dataset")
	public void validateDataSetLists() throws Exception {
		softAssert = new SoftAssert();
		//Real api assertions

		Assert.assertNotNull(String.valueOf(realDataset.getCount()), "\tReal API Field :  Count       :   is not set");
		Assert.assertNotNull(String.valueOf(realDataset.getTotal()), "\tReal API Count :  Total       :   is not set");
		Assert.assertNotNull(String.valueOf(realDataset.getFirst()), "\tReal API Field     :  First       :   is not set");
		Assert.assertNotNull(String.valueOf(realDataset.getLast()), "\tReal API Count      :   Last       :   is not set");
		Assert.assertNotNull(String.valueOf(realDataset.getPage()), "\tReal API Field       :   Page        :   is not set");
		Assert.assertNotNull(String.valueOf(realDataset.getTotalPages()), "\tReal API Count :  TotalPages   :   is not set");
		Assert.assertNotNull(String.valueOf(realDataset.getItemsPerPage()), "\tReal API Field :  ItemsPage  :   is not set");
		Assert.assertNotNull(String.valueOf(realDataset.getStartIndex()), "\tReal API Count :  StartIndex   :   is not set");

		// Stub API Assertions

		Assert.assertNotNull(String.valueOf(stubDataSet.getCount()), "\tStub API Field :  Count       :   is not set");
		Assert.assertNotNull(String.valueOf(stubDataSet.getTotal()), "\tStub API Count :  Total       :   is not set");
		Assert.assertNotNull(String.valueOf(stubDataSet.getFirst()), "\tStub API Field     :  First       :   is not set");
		Assert.assertNotNull(String.valueOf(stubDataSet.getLast()), "\tStub API Count      :   Last       :   is not set");
		Assert.assertNotNull(String.valueOf(stubDataSet.getPage()), "\tStub API Field       :   Page        :   is not set");
		Assert.assertNotNull(String.valueOf(stubDataSet.getTotalPages()), "\tStub API Count :  TotalPages   :   is not set");
		Assert.assertNotNull(String.valueOf(stubDataSet.getItemsPerPage()), "\tStub API Field :  ItemsPage  :   is not set");
		Assert.assertNotNull(String.valueOf(stubDataSet.getStartIndex()), "\tStub API Count :  StartIndex   :   is not set");


		Set <String> setOfStubKeys = itemsStub.get(0).keySet();
		Set <String> setOfAPIKeys = itemsAPI.get(0).keySet();
		validateFieldsUnderAnItem(setOfAPIKeys, itemsStub.get(0), "API", "STUB");
		validateFieldsUnderAnItem(setOfStubKeys, itemsAPI.get(0), "STUB", "API");
		softAssert.assertAll();
	}

	@Test(groups = "dimensions")
	public void validateDataSetData() throws Exception {
		softAssert = new SoftAssert();
		apiDimOptions = dimensionalAPI.callTheLink(apiDimUrl);
		stubDimOptions = dimensionalAPI.callTheLink(stubDimUrl);
		DataSet realDataset = null, stubDataSet = null;

		realDataset = mapper.readValue(apiDimOptions, new TypeReference <DataSet>() {
		});
		stubDataSet = mapper.readValue(stubDimOptions, new TypeReference <DataSet>() {
		});
		compareDataSetFields(realDataset, stubDataSet);
		validateFieldsUnderDimension(realDataset.getDimensions().iterator().next(), stubDataSet.getDimensions().iterator().next());
		printJSONStrings();
		softAssert.assertAll();
	}

	public void printJSONStrings() {
		softAssert.assertEquals(dataListAPI, dataListStub,
				"\n-----**********----------API DataSetLists-------********------ \n"
						+ dataListAPI + "\n \n" +
						"\n \n---------------STUB DataSetLists------------- \n"
						+ dataListStub + "\n" +
						"\n------********---------END OF DATALISTS ------*********------- \n \n");
		softAssert.assertEquals(apiDimOptions, stubDimOptions,
				"\n-----**********----------API DataSetLists-------********------ \n"
						+ apiDimOptions + "\n" +
						"\n---------------STUB DataSetLists------------- \n"
						+ stubDimOptions + "\n" +
						"\n------********---------END OF DATALISTS ------*********------- \n \n");
		softAssert.assertAll();
	}

	public void validateFieldsUnderDimension(Dimension realDim, Dimension stubDim) {
		HashMap <String, Object> apiDimValues = realDim.getObjectWithValues(realDim);
		HashMap <String, Object> stubDimValues = stubDim.getObjectWithValues(stubDim);
		Set <String> keyList = apiDimValues.keySet();
		assertDimensions(apiDimValues, stubDimValues, keyList);

	}

	public void assertDimensions(HashMap apiDimValues, HashMap stubDimValues, Set <String> keyList) {
		for (String key : keyList) {
			softAssert.assertTrue(apiDimValues.get(key) != null && stubDimValues.get(key) != null,
					"\n---------********-------------------FAILURE--------********----------------\n " +
							"\n\tMismatch in the field  :" + key.toUpperCase() + ". between stub and api.\n \n \n" +
							"\n\t VALUE IN STUB - " + key.toUpperCase() + "  : " + apiDimValues.get(key) + "\n" +
							"\n\t VALUE IN API-   " + key.toUpperCase() + "  : " + stubDimValues.get(key) + "\n" +
							"\n---------********---------  END OF FAILURE   --------********----------------\n \n \n");
		}
	}

	public void validateFieldsUnderAnItem(Set <String> setOfKeys, Items items, String stub, String api) {
		for (String key : setOfKeys) {
			softAssert.assertTrue(items.containsKey(key),
					"\n---------********-------------------FAILURE--------********----------------\n " +
							"\n\tMismatch in the field  :" + key.toUpperCase() + ". between stub and api.\n \n \n" +
							"\n\tThe field : " + key.toUpperCase() + " : NOT present in " + api + "\n \n" +
							"\n\t BUT exists in : " + stub + "\n" +
							"\n---------********---------  END OF FAILURE   --------********----------------\n \n \n");
		}
	}

	public void compareDataSetFields(DataSet apiDataSet, DataSet stubDataSet) {
		assertObjects(apiDataSet.getId(), stubDataSet.getId(), "id");
		assertObjects(apiDataSet.getS3URL(), stubDataSet.getS3URL(), "S3Url");
		assertObjects(apiDataSet.getId(), stubDataSet.getId(), "title");
		assertObjects(apiDataSet.getUrl(), stubDataSet.getUrl(), "url");
		assertObjects(apiDataSet.getCustomerFacingId(), stubDataSet.getCustomerFacingId(), "customerfacingId");
	}

	public void assertObjects(String apiField, String stubField, String field) {
		softAssert.assertTrue(apiField != null && stubField != null,
				"\n---------********-------------------FAILURE--------********----------------\n " +
						"\n\tMismatch in the field  :" + field.toUpperCase() + ". between stub and api.\n" +
						"           \n\t****VALUE IN STUB ***** : " + stubField + "\n" +
						"           \n\t****VALUE IN API ****** : " + apiField + "\n" +
						"\n---------********---------  END OF FAILURE   --------********----------------\n \n \n");

	}
}
