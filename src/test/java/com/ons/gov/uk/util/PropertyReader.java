package com.ons.gov.uk.util;

import com.ons.gov.uk.core.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class PropertyReader {
	public static HashMap <String, String> elementMap = new HashMap <String, String>();
	Properties props = new Properties();

	public PropertyReader() {
		loadLocators();
	}

	public void loadLocators() {
		String propertyFile = "backend_locators.properties";
		InputStream input = Config.class.getResourceAsStream("/files/" + propertyFile);
		try {
			props.load(input);
			Set <Object> keys = props.keySet();
			for (Object key : keys) {
				elementMap.put((String) key, props.getProperty((String) key));
			}

		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}

	public HashMap <String, String> getElementMap() {
		return elementMap;

	}

	public String getValue(String locator) {
		return elementMap.get(locator);
	}

}
