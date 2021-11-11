package guru.springframework;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ProductsSeleniumWebDriverTest {

	WebDriver driver;

	@BeforeSuite(description = "test ortamı hazırlandı.")
	public void beforeSuite() {
		WebDriverManager.chromedriver().setup();

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--allow-insecure-localhost");
		options.addArguments("acceptInsecureCert");
		options.addArguments("--ignore-certificate-errors");

		driver = new ChromeDriver(options);
	}

	@BeforeClass(description = "siteye giris yapilabildi mi?")
	public void beforeTest() {
		driver.get("http://localhost:8086/");
		Boolean pageControl = driver.findElement(By.linkText("Home")).isDisplayed();

		assertTrue(pageControl);
		System.out.println("test00");
	}

	@Test(description = "product list görülüyor mu?")
	public void seeTheProductList() {
		driver.findElement(By.linkText("Products")).click();

		WebElement productList = driver.findElement(By.xpath("/html/body/div/div[2]/h2"));

		assertEquals(productList.getText(), "Product List");
	}

	@Test(description = "product listesindeki bir product infolarini gösteriyor mu?", dependsOnMethods = "seeTheProductList")
	public void viewProduct() {
		driver.navigate().back();
		driver.findElement(By.cssSelector("a[href='/product/1']")).click();

		WebElement productInfo = driver.findElement(By.xpath("/html/body/div/h2"));
		assertEquals(productInfo.getText(), "Product Details");
	}

	@Test(description = "producta tikladiğinda dogru product bilgisini gösteriyor mu?", dependsOnMethods = "seeTheProductList")
	public void trueProductInfo() {
		String actualProductID = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[3]/td[1]")).getText();
		driver.findElement(By.cssSelector("a[href='/product/2']")).click();
		
		String expectedProductID = driver.findElement(By.className("form-control-static")).getText();

		assertEquals(actualProductID, expectedProductID);
	}
	
	@Test (description = "edit product info", dependsOnMethods = "viewProduct")
	public void editProductInfo() {
		driver.navigate().back();
		driver.findElement(By.linkText("Edit")).click();
		driver.findElement(By.className("form-control")).sendKeys("235268845711061996");
		
		driver.findElement(By.xpath("/html/body/div/div[2]/form/div[5]/button")).click();
		driver.navigate().back();
		driver.navigate().back();
		
		String newID = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[2]/td[2]")).getText();
		
		assertTrue(newID.endsWith("1996"));
	}
	
	@AfterSuite(description = "testler bitince browser pagelerini kapatır.")
	public void close() throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);
		driver.close();
	}

}