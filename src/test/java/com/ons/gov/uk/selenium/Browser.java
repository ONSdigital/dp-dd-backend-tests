package com.ons.gov.uk.selenium;

import com.ons.gov.uk.core.Config;
import com.ons.gov.uk.util.Helper;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class Browser {

	private static Logger log = Logger.getLogger(Browser.class.getCanonicalName());
	private static WebDriver webDriver;
	private static WebDriverWait webDriverWait;

	public static WebDriver initDriver(Config configuration) {
		if (webDriver == null) {
			setDriver(configuration.getBrowser().toUpperCase());
			setWebDriverWait(webDriver);
		}
		return webDriver;
	}

	public static WebDriverWait getWebDriverWait() {
		return webDriverWait;
	}

	public static void setWebDriverWait(WebDriver driver) {
		webDriverWait = new WebDriverWait(driver, 20);
	}

	public static void setDriver(String browser) {
		switch (browser) {
			case "FIREFOX":
				System.setProperty("webdriver.gecko.driver", "/Applications/Firefox.app/Contents/MacOS/firefox");
				FirefoxProfile ffProfile = new FirefoxProfile();
				ffProfile.setEnableNativeEvents(false);
				DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE, ffProfile);
				webDriver = new FirefoxDriver(desiredCapabilities);


				break;
			case "HTMLUNITDRIVER":
				DesiredCapabilities caps = new DesiredCapabilities();
				caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/Cellar/phantomjs/2.1.1/bin/phantomjs");
				caps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
				caps.setJavascriptEnabled(true);
				webDriver = new PhantomJSDriver(caps);
				Dimension dim = new Dimension(1280, 1024);
				webDriver.manage().window().setSize(dim);
				break;
			case "CHROME":
				String resource = Helper.getChromeDriverFileLocation();
				File file = new File(resource);
				if (file.exists()) {
					log.info("CHROME DRIVER exists:" + file.getAbsolutePath());
					file.setExecutable(true);
				}

				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				System.out.println(file.getAbsolutePath());
				DesiredCapabilities desiredCapabilitiesChrome = DesiredCapabilities.chrome();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--start-maximized", "--silent");
				desiredCapabilitiesChrome.setCapability(ChromeOptions.CAPABILITY, options);
				webDriver = new ChromeDriver();
				if (webDriver instanceof JavascriptExecutor) {
					((JavascriptExecutor) webDriver).executeScript("window.resizeTo(1024, 768);");
				}
				webDriver.manage().window().maximize();
				break;
			case "BROWSERSTACK":
				// INIT browserstack class which will use the browser.json.
				// more work to be done here.

				DesiredCapabilities caps1 = new DesiredCapabilities();
				caps1.setCapability("browser", "chrome");
				caps1.setCapability("browser_version", "54.0");
				caps1.setCapability("os", "Windows");
				caps1.setCapability("os_version", "8");
				caps1.setCapability("resolution", "1024x768");
				caps1.setCapability("browserstack.debug", true);
				caps1.setCapability("browserstack.local", true);
				try {
					webDriver = new RemoteWebDriver(new URL("https://iankent4:xDSMJuAtGbyf3Gzgsg5q@hub-cloud.browserstack.com/wd/hub"),
							caps1);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.setProperty("webdriver.gecko.driver", "/home/giri/Downloads/firefox/browser/firefox");
				webDriver = new FirefoxDriver();
				if (webDriver instanceof JavascriptExecutor) {
					((JavascriptExecutor) webDriver).executeScript("window.resizeTo(1024, 768);");
				}
		}

		webDriver.manage().window().maximize();
		Dimension dim = new Dimension(1280, 1024);
		webDriver.manage().window().setSize(dim);

	}

	public static void closeBrowser() {
		webDriver.close();
	}

}
