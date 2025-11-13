package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NseHomePage {
    WebDriver driver;
    private WebDriverWait wait ;

    @FindBy(xpath = "//input[@type='text']")
    WebElement searchBox;

    @FindBy(id = "async-navbar-search-item-0")
    WebElement firstSuggestion; // first dropdown result


    @FindBy(xpath = "//span[contains(@class, 'symbol-page-header')]")
    WebElement stockNameElement;

    public NseHomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void openSite() {
        driver.get("https://www.nseindia.com/");
    }

    public void searchStock(String stockName) throws InterruptedException {
        searchBox.sendKeys(stockName);
        Thread.sleep(2000);
        searchBox.sendKeys(Keys.ENTER);

        // Wait for dropdown and click the first suggestion
       // WebElement firstOption = wait.until(ExpectedConditions.visibilityOfElementLocated(firstSuggestion));
        firstSuggestion.click();
    }

    public String get52WeekHigh() {
        By highLocator = By.id("week52highVal");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Wait until text is non-empty
        return wait.until(d -> {
            String text = d.findElement(highLocator).getText().trim();
            return !text.isEmpty() && !text.equals("-") ? text : null;
        });
    }

    public String get52WeekLow() {
        By lowLocator = By.id("week52lowVal");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        return wait.until(d -> {
            String text = d.findElement(lowLocator).getText().trim();
            return !text.isEmpty() && !text.equals("-") ? text : null;
        });
    }


}

