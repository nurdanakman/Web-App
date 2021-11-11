package guru.springframework;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class CreateSeleniumWebDriverTest {

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

	@BeforeTest(description = "siteye giriş yapılabildi mi?")
	public void beforeTest() {
		driver.get("http://localhost:8086/");
		Boolean pageControl = driver.findElement(By.cssSelector("img[src='/images/NewBannerBOOTS_2.png\']")).isDisplayed();

		assertTrue(pageControl);
		System.out.println("test00");  
	}
	

	@Test(description = "product eklenebiliyor mu?", enabled = true)
	public void addNewProduct() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div/nav/div/div/ul/li[2]/a")).click();
		driver.findElement(By.id("productId")).sendKeys("3");
		driver.findElement(By.id("description")).sendKeys("Spring Framework Guru Shoe");
		driver.findElement(By.id("price")).sendKeys("470.00");
		driver.findElement(By.id("imageUrl")).sendKeys("https://www.superstep.com.tr/urun/converse-chuck-taylor-all-star-hi-unisex-siyah-sneaker/m9160c-001/");


		driver.findElement(By.className("btn-default")).click();
		driver.findElement(By.xpath("/html/body/div/nav/div/div/ul/li[1]/a")).click();
		
		WebElement productInfo = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[4]/td[3]"));
		AssertJUnit.assertTrue(productInfo.isDisplayed());
		
		System.out.println("Test10");
	}
	@Test(description = "product editlenebiliyor mu?", enabled = true,  dependsOnMethods = {"addNewProduct" })
	public void editProduct() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[4]/td[6]/a")).click();
		driver.findElement(By.id("price")).clear();
		driver.findElement(By.id("price")).sendKeys("500.00");
		driver.findElement(By.id("description")).clear();

		driver.findElement(By.className("btn-default")).click();
		driver.findElement(By.xpath("/html/body/div/nav/div/div/ul/li[1]/a")).click();
		
		
		TimeUnit.SECONDS.sleep(5);
		String productInfo = driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[4]/td[4]")).getText();
		assertNotNull(productInfo);
		
		System.out.println("Test11");
	}
	@Test(description = "editlenen product görüntüleniyor mu?", enabled = true, dependsOnMethods = {"editProduct"})
	public void viewProduct() throws InterruptedException {
		driver.findElement(By.xpath("/html/body/div/nav/div/div/ul/li[1]/a")).click(); 
		driver.findElement(By.xpath("/html/body/div/div[2]/table/tbody/tr[4]/td[5]/a")).click(); 

		
		WebElement productInfo = driver.findElement(By.xpath("/html/body/div/div[2]/form/div[3]/div/p"));
		assertTrue(productInfo.isDisplayed());
		
		System.out.println("Test12");
	}	
	@AfterSuite(description = "testler bitince browser pagelerini kapatır.")
	public void close() throws InterruptedException {
		TimeUnit.SECONDS.sleep(2);
		driver.close();
	}

}