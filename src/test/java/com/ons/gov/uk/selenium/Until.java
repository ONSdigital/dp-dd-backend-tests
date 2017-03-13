package com.ons.gov.uk.selenium;


import com.ons.gov.uk.core.TestContext;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;


public class Until {
	public static ExpectedCondition <Boolean> clicked(final By by) {

		return new ExpectedCondition <Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				try {
					WebElement element;

					try {
						element = TestContext.getDriver().findElement(by);
					} catch (Exception e) {
						return false;
					}

					if (element.isDisplayed() && element.isEnabled() && element.getAttribute("disabled") == null) {
						element.click();
						return true;
					}
				} catch (Exception ex) {
					TestContext.getDriver().findElement(By.tagName("body")).sendKeys(Keys.HOME);
					System.out.println(String.format("Could not click element %s %s, retrying", by, ex.getMessage()));
					return false;
				}

				return false;
			}

			@Override
			public String toString() {
				return String.format("Could not click element %s", by);
			}
		};
	}

	public static ExpectedCondition <WebElement> presenceOfChildElementLocated(final WebElement parentElement, final By childElementLocator) {

		return new ExpectedCondition <WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				try {
					return parentElement.findElement(childElementLocator);
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "visibility of element located by " + childElementLocator;
			}
		};
	}
}
