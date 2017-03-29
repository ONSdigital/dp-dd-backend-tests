package com.ons.gov.uk.frontend.filters;

import com.ons.gov.uk.frontend.pages.BasePage;
import com.ons.gov.uk.util.RandomStringGen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;


public class SummarySelector extends BasePage {

	public By addMore = getElementLocator("add_more_css");
	public By continue_selection = getElementLocator("save_selection_css");
	public By remove_all_selected = getElementLocator("remove_all_css");

	public void removeGroups() {
		for (WebElement removeGroup : getRemoveGroups()) {
			removeGroup.click();
		}
	}

	public ArrayList <String> selectedOptions(String filterText, boolean hierarchy) {
		getCustomiseLink(filterText).click();
		ArrayList <String> selectedValues = new ArrayList <>();
		if (hierarchy) {
			for (WebElement webTemp : getAllRangeOptions()) {
				selectedValues.add(webTemp.getText());
			}
		} else {
			for (WebElement webTemp : getAllSelectedChkBoxes()) {
				selectedValues.add(webTemp.getAttribute("id"));
			}
		}
		click(continue_selection);
		return selectedValues;

	}

	public void removeRandomGroup() {
		getRemoveGroups().get(RandomStringGen.getRandomInt(getRemoveGroups().size() - 1)).click();
	}

	public void removeRandomOption() throws Exception {
		getRemoveLinks().get(RandomStringGen.getRandomInt(getRemoveLinks().size() - 1)).click();
	}


}
