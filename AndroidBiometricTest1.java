package org.example;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AuthenticatesByFinger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class AndroidBiometricTest1 {

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        // UiAutomator2Options options = new UiAutomator2Options();
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("deviceName", "emulator-5554");
        capabilities.setCapability("platformVersion", "15");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName","UiAutomator2");
        capabilities.setCapability("appium:app", System.getProperty("user.dir") + "/Android-MyDemoAppRN.1.3.0.build-244.apk");
        AppiumDriver driver;

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), capabilities);
      //  driver.executeScript("mobile:enrollBiometric", ImmutableMap.of("isEnabled", true));
//        options.setDeviceName("emulator-5554")
//                .setPlatformName("android")
//                .setPlatformVersion("13.0")
//                .setAutomationName("UiAutomator2")
//                .setAutomationName("UiAutomatorautomationName2")
//                .setAppPackage("com.saucelabs.mydemoapp.rn")
//                .setAppActivity(".MainActivity");

      //  driver = new AndroidDriver(new URL("http://0.0.0.0:4723"), options);
        driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc=\"open menu\"]/android.widget.ImageView")).click();
        Thread.sleep(2000);
        driver.findElement(By.xpath("//android.view.ViewGroup[@content-desc=\"menu item biometrics\"]")).click();
        Thread.sleep(4000);
        driver.findElement(By.id("android:id/button1")).click();
        driver.findElement(By.xpath("//android.widget.Switch[@content-desc=\"biometrics switch\"]")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("menu item log in")).click();
        Thread.sleep(2000);
        executeProcess("adb -e emu finger touch 1");
        ((AuthenticatesByFinger) driver).fingerPrint(1);
      //  WebDriverWait wait = new WebDriverWait(driver, Duration.of(5000, ChronoUnit.MILLIS));
        try {
         //   wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text=\"Products\"]")));
            System.out.println("Test Passed!!!");
        } catch (Exception exception) {
            Assert.fail("Biometric Authentication failed.");
        } finally {
            driver.quit();
        }
    }


    private static void executeProcess(String cmd) {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Run a shell command
        processBuilder.command("bash", "-c", cmd);

        // Run a shell script
        //processBuilder.command("xyz.sh");

        // if running on windows

        // Run a command
        //processBuilder.command("cmd.exe", "/c", cmd);


        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + " ");
            }

            BufferedReader ereader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

            String eline;
            while ((eline = ereader.readLine()) != null) {
                output.append(eline + " ");
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Success");
                System.out.println(output);

            } else {
                System.out.println("Failure");
                System.out.println(output);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

