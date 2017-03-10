package com.ons.gov.uk.frontend.test;

import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.frontend.pages.BasePage;
import org.openqa.selenium.By;


public class BaseTest {
	BasePage basePage = new BasePage();
	Config config = new Config();

	public void openPage(By dataSet) {
		basePage.navigateToUrl(config.getBaseURL());
		basePage.click(dataSet);
		basePage.switchToLatestWindow();
		basePage.click(basePage.customise_data_set);
	}

	public void checkForDS(By link) throws Exception {
		basePage.getDriver();
		String baseUrl = basePage.getConfig().getBaseURL();
		basePage.navigateToUrl(baseUrl);
		basePage.click(link);
		basePage.switchToLatestWindow();
	}


}
