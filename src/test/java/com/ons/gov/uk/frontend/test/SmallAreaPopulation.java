package com.ons.gov.uk.frontend.test;


import com.ons.gov.uk.frontend.filters.HierarchySelector;
import com.ons.gov.uk.frontend.filters.SummarySelector;
import com.ons.gov.uk.frontend.filters.TimeSelector;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class SmallAreaPopulation extends BaseTest {

	public By sape_link = basePage.getElementLocator("small_area_pop_estimates_linkText");
	public By cpi_spl_aggregate_link = basePage.getElementLocator("cpi_spl_aggregates_linkText");
	public String time_filter = basePage.getTextFromProperty("time_filter_text");
	public String spl_aggr_filter = basePage.getTextFromProperty("spl_aggregates_filter_text");
	public String search_terms = basePage.getTextFromProperty("searchKeys2_text");
	HierarchySelector hierarchySelector = new HierarchySelector();
	TimeSelector timeSelector = new TimeSelector();
	SummarySelector summarySelector = new SummarySelector();
	ArrayList <String> timeGroups = new ArrayList <>();
	ArrayList <String> selected_spl_agg = new ArrayList <>();

	@Test(groups = {"sapeTests"})
	public void sapeGeoTests() throws Exception {
		if (config.getBaseURL().contains("develop")) {
			System.out.println("************ Starting SAPE Journey Geography Hierarchy Test **********");
			checkForDS(sape_link);
			basePage.click(basePage.customise_data_set);
			hierarchySelector.compareGeoSorting("Geographic_Hierarchy", true, "");
			hierarchySelector.compareGeoSorting("Geographic_Hierarchy", true, "Unitary Authority");
//			hierarchySelector.compareGeoSorting("Geographic_Hierarchy", true, "Great Britain");
			hierarchySelector.compareGeoSorting("Geographic_Hierarchy", true, "Westminster Parliamentary Constituency");
			System.out.println("************ Completed SAPE Journey Geography Hierarchy Test **********");
		}
	}

	@Test(groups = {"opencpisplaggr"}, dependsOnGroups = {"sapeTests"})
	public void openCPISplAgg() throws Exception {
		if (config.getBaseURL().contains("develop")) {
			System.out.println("************    Starting CPI Special aggregate Test     **********");
			checkForDS(cpi_spl_aggregate_link);
			basePage.click(basePage.customise_data_set);
			System.out.println("************    Opened CPI Spl Aggregate customise page **********");
		}
	}

	@Test(groups = {"customiseMonth"}, dependsOnGroups = {"opencpisplaggr"})
	public void cpiMonth() throws Exception {
		if (config.getBaseURL().contains("develop")) {
			System.out.println("************    Start Time Selector Tests   **********");
			timeSelector.openTimeSelector(time_filter);
			System.out.println("************    Select Single Month, remove selected option and add a single month again  **********");
			timeSelector.singleMonthTimeSelector(time_filter);
			timeSelector.removeTimeGroups(time_filter);
			timeSelector.singleMonthTimeSelector(time_filter);
			timeSelector.openTimeSelector(time_filter);
			basePage.click(summarySelector.addMore);
			System.out.println("************    Select All Time and remove a random group   **********");
			timeSelector.selectAllTime(time_filter);
			timeSelector.removeRandomGroup(time_filter);
			timeSelector.openTimeSelector(time_filter);
			basePage.click(summarySelector.addMore);
			System.out.println("************    Select Range  and remove a random group **********");
			timeSelector.selectRange(time_filter);
			timeSelector.removeRandomGroup(time_filter);
			timeGroups = summarySelector.selectedOptions(time_filter, true);
			System.out.println("************  Completed Time Selector Tests  **********");

		}
	}

	@Test(groups = {"customiseSplAggr"}, dependsOnGroups = {"customiseMonth"})
	public void customiseSplAggr() throws Exception {
		if (config.getBaseURL().contains("develop")) {
			System.out.println("************  Start customising Spl Aggregates  **********");
			hierarchySelector.hierarchyJourney(spl_aggr_filter, search_terms, true, true);
			selected_spl_agg = summarySelector.selectedOptions(spl_aggr_filter, true);
			System.out.println("************  Finished customising Spl Aggregates  **********");
		}
	}

	@Test(groups = {"validateDownload"}, dependsOnGroups = {"customiseSplAggr"})
	public void downloadAndValidate() throws Exception {
		if (config.getBaseURL().contains("develop")) {
			System.out.println("************ Starting to download and validate the customised file  **********");
			basePage.selectDownloadCSV(true);
			basePage.checkDownloadedFile(selected_spl_agg, spl_aggr_filter, true);
			basePage.checkDownloadedFile(timeGroups, time_filter, true);
			System.out.println("************   Test for CPI TIME SELECTOR TESTS is now complete. **************");
		}
	}


}
