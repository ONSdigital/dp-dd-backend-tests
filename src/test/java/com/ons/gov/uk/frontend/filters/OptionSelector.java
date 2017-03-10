package com.ons.gov.uk.frontend.filters;

import com.ons.gov.uk.frontend.pages.BasePage;
import org.testng.Assert;


public class OptionSelector extends BasePage {

	String selectedOption = null;

	public void optionJourney(String filterText) throws Exception {
		String defaultSelection = getoptionsText(filterText);
		getCustomiseLink(filterText).click();
		click(enable_all);
		click(disable_all);
		click(save_selection);
		Assert.assertEquals(getElementText(error_message), error_message_text,
				"Actual error message : " + getElementText(error_message)
						+ "\n Expected error message : " + error_message_text);
		click(cancel_button);
		Assert.assertEquals(getoptionsText(filterText), defaultSelection);
		getCustomiseLink(filterText).click();
		// ids need to be removed
		selectCheckBox(0);
		selectedOption = returnSelectedChkBox();
		click(save_selection);
		Assert.assertEquals(getoptionsText(filterText), selectedOption,
				"Actual  " + filterText + "  : "
						+ getoptionsText(filterText) + "\n" +
						"Expected  " + filterText + "  : " + selectedOption);
		getCustomiseLink(filterText).click();
		click(enable_all);
		click(cancel_button);
		Assert.assertTrue(isElementPresent(customise_links), "**********    Not in the customise filters page   *************");
		Assert.assertEquals(getoptionsText(filterText), selectedOption,
				"Actual  " + filterText + "  : "
						+ getoptionsText(filterText) + "\n" +
						"Expected  " + filterText + "  : " + selectedOption);
		getCustomiseLink(filterText).click();
		click(enable_all);
		selectedOption = returnSelectedChkBox();
		click(save_selection);
		Assert.assertEquals(getoptionsText(filterText), defaultSelection,
				"Actual  " + filterText + "  : "
						+ getoptionsText(filterText) + "\n" +
						"Expected  " + filterText + "  : " + defaultSelection);
		getCustomiseLink(filterText).click();
		click(disable_all);
		selectCheckBox(1);
		selectedOption = returnSelectedChkBox();
		click(save_selection);
		Assert.assertEquals(getoptionsText(filterText), selectedOption,
				"Actual  " + filterText + "  : "
						+ getoptionsText(filterText) + "\n" +
						"Expected  " + filterText + "  : " + selectedOption);
	}

	public String returnSelectedChkBox() throws Exception {
		String valuetoReturn = null;
		int totalNumChkBox = getAllCheckBoxes().size();
		int selectedChkBox = findElementsBy(selected_checkboxes_css).size();
		int diff = totalNumChkBox - selectedChkBox;
		switch (diff) {
			case 0:
				valuetoReturn = "Everything selected (" + totalNumChkBox + ")";
				break;
			default:
				valuetoReturn = "Selected options (" + selectedChkBox + ")";
				break;
		}
		return valuetoReturn;

	}
}
