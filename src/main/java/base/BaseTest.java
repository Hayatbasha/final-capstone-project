package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

public class BaseTest {

    // ThreadLocal for parallel testing
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, Method method, Object[] data) {
        WebDriver webDriver = null;

        // ---------- PRINT TEST HEADER ----------
        System.out.println("\n======================================================");
        System.out.println("🚀 Starting Test: " + method.getName() + " | Data: " + Arrays.toString(data));
        System.out.println("Browser: " + browser.toUpperCase());
        System.out.println("======================================================");

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                webDriver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                webDriver = new EdgeDriver();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver();
                break;
        }

        driver.set(webDriver);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // ---------- PRINT TEST FOOTER ----------
        System.out.println("======================================================");
        System.out.println("✅ Completed Test: " + result.getMethod().getMethodName() +
                " | Status: " + (result.isSuccess() ? "PASSED" : "FAILED"));
        System.out.println("======================================================\n");

        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    // Utility for screenshots
    public String takeScreenshot(String fileName) {
        File srcFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String destPath = System.getProperty("user.dir") + "/screenshots/" + fileName + ".png";
        try {
            FileUtils.copyFile(srcFile, new File(destPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destPath;
    }
}
