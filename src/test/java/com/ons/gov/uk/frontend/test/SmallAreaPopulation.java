package com.ons.gov.uk.frontend.test;


import com.ons.gov.uk.frontend.filters.HierarchySelector;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class SmallAreaPopulation extends BaseTest {
	public By sape_link = basePage.getElementLocator("small_area_pop_estimates_linkText");
	HierarchySelector hierarchySelector = new HierarchySelector();

	@Test(groups = {"opensape"})
	public void openSape() throws Exception {
		if (!config.getEnv().equals("local")) {
			checkForDS(sape_link);
			basePage.click(basePage.customise_data_set);
			System.out.println("openSAPE");
		}
	}

	@Test(groups = {"orderedGeo"}, dependsOnGroups = {"opensape"})
	public void validateGeoOrdering() throws Exception {
		if (!config.getEnv().equals("local")) {
			hierarchySelector.compareGeoSorting("Geographic_Hierarchy", true);
		}
	}


}
