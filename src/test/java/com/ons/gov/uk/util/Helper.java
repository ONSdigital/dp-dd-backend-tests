package com.ons.gov.uk.util;

public class Helper {
	public static final String OS_NAME = System.getProperty("os.name");
	private static String librariesFolder = "libraries/";

	public static void pause(long millisec) {
		try {
			Thread.sleep(millisec);
		} catch (Exception e) {
			//
		}
	}

	public static void pause() {
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			//
		}
	}

	public static String getChromeDriverFileLocation() {
		if (OS_NAME.toLowerCase().contains("mac os x")) {
			return librariesFolder + "chromedriver/" + "chromedriver_mac";
		} else if (OS_NAME.toLowerCase().contains("windows")) {
			return librariesFolder + "chromedriver/" + "chromedriver_win.exe";
		} else {
			String arch = System.getProperty("os.arch");
			if (arch.contains("64")) {
				return librariesFolder + "chromedriver/" + "chromedriver_linux_64";
			} else {
				return librariesFolder + "chromedriver/" + "chromedriver_linux_32";
			}
		}
	}

	public static String getIEDriverFileLocation() {
		String windowsArchitecture = System.getProperty("os.arch");
		if (windowsArchitecture.contains("64")) {
			return librariesFolder + "iedriver/" + "IEDriverServer_x64_2.38.0.exe";
		} else {  //32 bit
			return librariesFolder + "iedriver/" + "IEDriverServer_Win32_2.38.0.exe";
		}
	}

	public static String getSetting(String key) {
		String result = System.getenv(key);
		if (result == null) {
			result = System.getProperty(key);
		}
		return result;
	}

	public static boolean isJenkins() {
		return isEnvSet() && getSetting("TEST_BROWSER") != null;
	}

	public static boolean isEnvSet() {
		return getSetting("PROTOTYPES_DISCOVERY") != null || getSetting("TEST_FLORENCE_DEV") != null;
	}


}
