package com.ons.gov.uk.core;


import com.ons.gov.uk.selenium.Browser;
import com.ons.gov.uk.util.CacheService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


public final class TestContext {
	public static Config configuration = new Config();

	public static WebDriver getDriver() {
		return Browser.initDriver(configuration);
	}

	public static Config getConfiguration() {
		return configuration;
	}

	public static CacheService getCacheService() {
		return CacheService.getInstance();
	}

	public static WebDriverWait getWebDriverWait() {
		return Browser.getWebDriverWait();
	}

}
