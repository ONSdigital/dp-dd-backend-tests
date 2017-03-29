package com.ons.gov.uk.frontend.filters;

import com.ons.gov.uk.frontend.pages.BasePage;
import com.ons.gov.uk.util.RandomStringGen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.ArrayList;

public class TimeSelector extends BasePage {
	public By single_month = getElementLocator("single_month_css");
	public By select_month = getElementLocator("month_css");
	public By select_year = getElementLocator("year_css");
	public By range = getElementLocator("range_css");
	public By select_all = getElementLocator("select_all_css");
	String selected_year = null, selected_month = null, defaultSelection = null;
	ArrayList <String> selectedYear = new ArrayList <>();
	ArrayList <String> selectedMonth = new ArrayList <>();
	SummarySelector summarySelector = new SummarySelector();


	public void openTimeSelector(String time_filter) {
		defaultSelection = getoptionsText(time_filter);
		getCustomiseLink(time_filter).click();
	}

	public void selectRange(String time_filter) {
		singleOrRange(range);
		setSelections();
		selectYearMonth(select_year);
		selectYearMonth(select_month);
		click(save_selection);
		String selectedOptions = returnSelectedOptionText();
		click(save_selection);
		Assert.assertEquals(getoptionsText(time_filter), selectedOptions,
				"Actual Time filters : "
						+ getoptionsText(time_filter) + "\n" +
						"Expected Time filters : " + selectedOptions);
	}


	public void singleMonthTimeSelector(String time_filter) {
		singleOrRange(single_month);
		setSelections();
		selected_year = selectYearMonth(select_year);
		Assert.assertTrue(!selected_year.equals(""));
		selected_month = selectYearMonth(select_month);
		Assert.assertTrue(!selected_month.equals(""));
		click(save_selection);
		String selectedOptions = returnSelectedOptionText();
		selectedYear.add(selected_year);
		selectedMonth.add(selected_month);
		assertSelection(selectedYear, getAllRangeHeaders());
		assertSelection(selectedMonth, getAllRangeOptions());
		int selectedOptionsSize = getAllRangeOptions().size();
		click(save_selection);
		Assert.assertEquals(getoptionsText(time_filter), selectedOptions,
				"Actual Time filters : "
						+ getoptionsText(time_filter) + "\n" +
						"Expected Time filters : " + selectedOptions);
		getCustomiseLink(time_filter).click();
		selectedOptions = returnSelectedOptionText();
		click(save_selection);
		Assert.assertEquals(getoptionsText(time_filter), selectedOptions,
				"Actual Time filters : "
						+ getoptionsText(time_filter) + "\n" +
						"Expected Time filters : " + selectedOptions);

	}

	public void removeTimeGroups(String time_filter) {
		getCustomiseLink(time_filter).click();
		summarySelector.removeGroups();
		click(save_selection);
	}

	public void removeRandomTime(String time_filter) throws Exception {
		getCustomiseLink(time_filter).click();
		summarySelector.removeRandomOption();
		click(save_selection);
	}

	public void removeRandomGroup(String time_filter) {
		getCustomiseLink(time_filter).click();
		summarySelector.removeRandomGroup();
		click(save_selection);
	}

	public void selectAllTime(String time_filter) throws Exception {
		click(select_all);
		click(save_selection);
		int totalOptions = getAllRangeOptions().size();
		click(save_selection);
		Assert.assertEquals(getoptionsText(time_filter), "Everything selected (" + totalOptions + ")",
				"Actual Time filters : "
						+ getoptionsText(time_filter) + "\n" +
						"Expected Time filters : " + "Everything selected (" + totalOptions + ")");

		Assert.assertTrue(getoptionsText(time_filter).contains(String.valueOf(totalOptions)),
				"Actual Time filters : "
						+ getoptionsText(time_filter) + "\n" +
						"Total Expected Time Options  :   " + totalOptions);

	}


	public void singleOrRange(By selectType) {
		if (!getElement(selectType).isSelected()) {
			click(selectType);
		}
	}

	public String selectYearMonth(By cpiElement) {
		String toRet = null;
		Select dropdownSelect = null;
		int index = 0;
		try {
			dropdownSelect = new Select(getElement(cpiElement));
			ArrayList <WebElement> dropDownOption = (ArrayList <WebElement>) dropdownSelect.getOptions();
			index = RandomStringGen.getRandomInt(dropDownOption.size() - 1) + 1;
			dropdownSelect.selectByIndex(index);
			toRet = dropdownSelect.getOptions().get(index).getAttribute("label");

		} catch (Exception ee) {
			ee.printStackTrace();
		}

		return toRet;
	}

	public void setSelections() {
		selected_month = null;
		selected_year = null;
	}

	public void selectFromDropDown(By cpiElement) {
		String valueSelected = null;
		ArrayList <WebElement> selects = (ArrayList <WebElement>) findElementsBy(cpiElement);
		for (WebElement select : selects) {
			Select dropdownSelect = new Select(select);
			ArrayList <WebElement> dropDownMonths = (ArrayList <WebElement>) dropdownSelect.getOptions();
			int index = RandomStringGen.getRandomInt(dropDownMonths.size() - 1) + 1;
			dropdownSelect.selectByIndex(index);
		}
	}
}
