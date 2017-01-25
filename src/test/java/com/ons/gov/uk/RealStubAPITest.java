package com.ons.gov.uk;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.core.model.DataSet;
import com.ons.gov.uk.core.model.Items;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RealStubAPITest {
	private static Logger logger = Logger.getLogger(RealStubAPITest.class);
	DimensionalAPI dimensionalAPI = new DimensionalAPI();

	@Test
	public void teststubapi() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map <String, Object> map;
		map = new HashMap <String, Object>();
		DataSet realDataset = null, stubDataSet = null;
		List <Items> itemsAPI = null, itemsStub = null;
		String jsonRespAPI = dimensionalAPI.checkEndPoint(new Config().getEndPointReal());
		String jsonRespStub = dimensionalAPI.checkEndPoint(new Config().getEndPointStub());
		JSONArray itemRespApi = dimensionalAPI.getItems(jsonRespAPI, "items");
		JSONArray itemRespStub = dimensionalAPI.getItems(jsonRespStub, "items");

		realDataset = mapper.readValue(jsonRespAPI, new TypeReference <DataSet>() {
		});
		itemsAPI = mapper.readValue(String.valueOf(itemRespApi), new TypeReference <List <Items>>() {
		});
		stubDataSet = mapper.readValue(jsonRespStub, new TypeReference <DataSet>() {
		});
		itemsStub = mapper.readValue(String.valueOf(itemRespStub), new TypeReference <List <Items>>() {
		});


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
		int stubFailCount = compareKeys(setOfAPIKeys, itemsStub.get(0), "API", "STUB");
		int apiFailCount = compareKeys(setOfStubKeys, itemsAPI.get(0), "STUB", "API");
		Assert.assertTrue(stubFailCount == 0 && apiFailCount == 0, "\t****There are mismatches between the STUB & API Items. ");
	}

	public int compareKeys(Set <String> setOfKeys, Items items, String stub, String api) {
		int failureCount = 0;
		for (String key : setOfKeys) {
			try {
				Assert.assertTrue(items.containsKey(key),
						"\tFAILURE---  The key : " + key + " : exists in " + stub + " *ITEMS* but not in : " + api);
			} catch (AssertionError ee) {
				failureCount++;
				System.out.println(ee.getMessage());
				logger.info(ee.getMessage());


			}
		}
		return failureCount;
	}

}
