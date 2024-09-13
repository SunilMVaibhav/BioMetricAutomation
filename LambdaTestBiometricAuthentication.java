package org.example;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.net.MalformedURLException;
import java.net.URL;

public class LambdaTestBiometricAuthentication {

    private AppiumDriver driver;
    private static final String GRID_URL = "@mobile-hub.lambdatest.com/wd/hub";
    private static final String LT_USERNAME = "YOUR-USERNAME";
    private static final String LT_ACCESS_KEY = "YOUR_ACCESS_KEY";
    private static final String ANDROID_APP_URL = "Android-APP-ID";
    private static final String IOS_APP_URL = "iOS-APP-ID";

    public void setDriver(String platformName, String deviceName, String platformVersion) throws MalformedURLException {
        String url = "https://" + LT_USERNAME + ":" + LT_ACCESS_KEY + GRID_URL;
        if (platformName.equalsIgnoreCase("android")) {
            DesiredCapabilities capabilities = setCapabilities(platformName, deviceName, platformVersion, ANDROID_APP_URL);
            this.driver = new AndroidDriver(new URL(url), capabilities);
        } else if (platformName.equalsIgnoreCase("ios")) {
            DesiredCapabilities capabilities = setCapabilities(platformName, deviceName, platformVersion, IOS_APP_URL);
            this.driver = new IOSDriver(new URL(url), capabilities);
        } else {
            throw new IllegalArgumentException("Invalid platform name: " + platformName);
        }
    }

    public DesiredCapabilities setCapabilities(String platformName, String deviceName, String platformVersion, String appURL) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("build", "Finger Print POC");
        capabilities.setCapability("name", "Finger Print POC");
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("isRealMobile", true);
        capabilities.setCapability("app", appURL);
        capabilities.setCapability("devicelog", true);
        capabilities.setCapability("enableBiometricsAuthentication", true);
        return capabilities;
    }

    public void runTest(String platformName, String deviceName, String platformVersion, String fingerPrintStatus) throws MalformedURLException, InterruptedException {
        setDriver(platformName, deviceName, platformVersion);

        if (platformName.equalsIgnoreCase("android")) {
            runAndroidTest(fingerPrintStatus);
        } else if (platformName.equalsIgnoreCase("ios")) {
            runIOSTest(fingerPrintStatus);
        } else {
            throw new IllegalArgumentException("Invalid platform name: " + platformName);
        }
    }

    public void runIOSTest(String fingerPrintStatus) throws InterruptedException {
        this.driver.findElementByAccessibilityId("tab bar option menu").click();
        this.driver.findElementByAccessibilityId("menu item biometrics").click();
        Thread.sleep(2000);
        this.driver.findElementByAccessibilityId("biometrics switch").click();
        this.driver.findElementByAccessibilityId("tab bar option menu").click();
        Thread.sleep(2000);
        this.driver.findElementByAccessibilityId("menu item log in").click();
        Thread.sleep(2000);
        this.driver.executeScript("lambda-biometric-injection=" + fingerPrintStatus);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"Products\"`]")));
            System.out.println("Test Passed!!!");
        } catch (Exception exception) {
            Assert.fail("Biometric Authentication failed.");
        } finally {
            driver.quit();
        }
    }

    public void runAndroidTest(String fingerPrintStatus) throws InterruptedException {
        this.driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc=\"open menu\"]/android.widget.ImageView")).click();
        this.driver.findElementByAccessibilityId("menu item biometrics").click();
        Thread.sleep(2000);
        this.driver.findElement(By.xpath("//android.widget.Switch[@content-desc=\"biometrics switch\"]")).click();
        this.driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc=\"open menu\"]/android.widget.ImageView")).click();
        Thread.sleep(2000);
        this.driver.findElementByAccessibilityId("menu item log in").click();
        Thread.sleep(2000);
        this.driver.executeScript("lambda-biometric-injection=" + fingerPrintStatus);
        WebDriverWait wait = new WebDriverWait(driver, 5);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Products\"]")));
            System.out.println("Test Passed!!!");
        } catch (Exception exception) {
            Assert.fail("Biometric Authentication failed.");
        } finally {
            this.driver.quit();
        }
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        String fingerPrintStatus = "pass";
        LambdaTestBiometricAuthentication biometricAuthentication = new LambdaTestBiometricAuthentication();
        biometricAuthentication.runTest("android", "Moto G Stylus 5G (2022)", "12", fingerPrintStatus);
//        biometricAuthentication.runTest("ios", "iPhone 14", "16", fingerPrintStatus);
    }
}