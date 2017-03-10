package com.ons.gov.uk.core.util;

import java.util.HashMap;
import java.util.Map;

public class CacheService {

	private static Map <String, AnyData> dataMap = new HashMap <>();
	private static CacheService cacheService;

	public static CacheService getInstance() {
		if (cacheService == null) {
			cacheService = new CacheService();
		}
		return cacheService;
	}

	public Map <String, AnyData> getDataMap() {
		return dataMap;
	}

	public void setDataMap(String key, AnyData data) {
		dataMap.put(key, data);
	}


}
