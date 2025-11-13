import base.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.NseHomePage;
import utils.ExcelUtil;
import utils.LoggerUtil;

public class StockInfoTest extends BaseTest {

    @Test(dataProvider = "excelData")
    public void verifyStockInformation(String stockName) throws InterruptedException {
        NseHomePage nsePage = new NseHomePage(getDriver());
        nsePage.openSite();
        LoggerUtil.info("Opened NSE India website.");

        nsePage.searchStock(stockName);
        LoggerUtil.info("Searched for stock: " + stockName);

        String high = nsePage.get52WeekHigh();
        String low = nsePage.get52WeekLow();

        LoggerUtil.info("52 Week High: " + high);
        LoggerUtil.info("52 Week Low: " + low);

        Assert.assertNotNull(stockName, "Company name is missing");
        Assert.assertFalse(stockName.trim().isEmpty(), "Company name is empty");
        Assert.assertTrue(high.matches("[\\d,]+(\\.\\d+)?"), "Invalid 52 week high value!");
        Assert.assertTrue(low.matches("[\\d,]+(\\.\\d+)?"), "Invalid 52 week low value!");

        Assert.assertNotNull(high, "52 Week High value is missing!");
        Assert.assertNotNull(low, "52 Week Low value is missing!");
    }

    @DataProvider(name = "excelData", parallel = true)
    public Object[][] getStockDataFromExcel() {
        String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/StockData.xlsx";
        return ExcelUtil.getStockData(filePath, "Sheet1");
    }
}

