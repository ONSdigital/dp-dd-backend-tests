package com.ons.gov.uk.frontend.pages;

import org.openqa.selenium.By;

public class CPI {
	BasePage basePage = new BasePage();

	// ***** CPI dataset
	public By cpi_link = basePage.getElementLocator("cpi_linkText");
	// ********** Filter options
	public String time_filter = basePage.getTextFromProperty("time_filter_text");

	public String spl_aggregate_filter = basePage.getTextFromProperty("spl_aggregate_filter_text");

	public By single_month = basePage.getElementLocator("single_month_css");
	public By range = basePage.getElementLocator("range_css");
	public By select_month = basePage.getElementLocator("month_css");
	public By select_year = basePage.getElementLocator("year_css");
	public By add_selection = basePage.getElementLocator("add_selection_linkText");

	// for spl aggregates. can move to base page for geography too

	public By search_aggregates = basePage.getElementLocator("search_linkText");
	public By browse_aggregates = basePage.getElementLocator("browse_linkText");
	public By add_all_aggregates = basePage.getElementLocator("add_all_linkText");
	public By search_textBox = basePage.getElementLocator("search_textbox_css");
	public By search_button = basePage.getElementLocator("search_button_css");
	public By add_all_link_button = basePage.getElementLocator("browse_linkText");
	public By customise_hierarchies = basePage.getElementLocator("customise_hierarchies_css");


	public String searchKeys1 = basePage.getTextFromProperty("searchKeys1_text");
	public String searchKeys2 = basePage.getTextFromProperty("searchKeys2_text");

//	Add more

}
