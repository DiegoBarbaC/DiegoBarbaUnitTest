package com.mayab.quality.functional;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.time.Duration;

public class SeleniumTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
	WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    js = (JavascriptExecutor) driver;
  }

  @Test
  public void testUntitledTestCase() throws Exception {
    driver.get("https://anahuac.brightspace.com/d2l/lms/dropbox/user/folder_submit_files.d2l?db=1725228&grpid=0&isprv=0&bp=0&ou=1494180");
    driver.close();
    driver.get("https://www.google.com/search?q=brightspace+an%C3%A1huac+mayab&rlz=1C1CHBF_esMX916MX916&oq=b&gs_lcrp=EgZjaHJvbWUqDggAEEUYJxg7GIAEGIoFMg4IABBFGCcYOxiABBiKBTIGCAEQRRhAMgYIAhBFGDkyDggDEEUYJxg7GIAEGIoFMgYIBBBFGDsyDAgFEAAYQxiABBiKBTINCAYQABiDARixAxiABDIQCAcQABiDARixAxiABBiKBdIBCTE4MjJqMGoxNagCCLACAQ&sourceid=chrome&ie=UTF-8");
    driver.findElement(By.xpath("//div[@id='rso']/div/div/div/div/div/div/div/span/a/h3")).click();
    driver.get("https://merida.anahuac.mx/herramientas-para-alumnos");
    driver.findElement(By.xpath("//div[@id='hs_cos_wrapper_widget_1699403857408']/section/div/div/div[4]/div/a")).click();
    driver.get("https://login.microsoftonline.com/0d8d6b97-e05d-460c-b6b5-19b7d020f47a/saml2?SAMLRequest=jdE7a8MwEADgvdD%2fYLTbetqxhR0I7RJIl6Tt0KXIihQbbMnVyaU%2fv05DSqFLtntw8N1dvZlj5%2fbmYzYQk%2b1jg0CNQ7jk76RS7JiXgllLREtIaW1eFkTRiirOW4uSVxOg965BLCMo2QLMZusgKheXEmEipTSl%2fJkQSUrJikywKq8Ye0PJBsCEuMw%2beAfzaMLBhM9em5f9rkFdjBNIjJVT3ax01ob%2b1EWYlDaZ9iM%2bsgEPE1YLHg%2f%2b1Dt8Zu%2fOUbb0UPI1Dg4aNAcnvYIepFOjARm1PGyednLRyin46LUf0Pr%2bLknqH3u4ZVBd5Wh9dfKVqBRtV2luizIVleFpW%2bR5yqnV7Cgo4dpm0bjlLvBvmV96jS%2bIBVTjv49ZfwM%3d");
    driver.get("https://anahuac.brightspace.com/d2l/home");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Diego Barba Cerezo'])[1]/preceding::div[8]")).click();
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Diego Barba Cerezo'])[1]/preceding::d2l-navigation-dropdown-button-icon[1]")).click();
    driver.findElement(By.xpath("//div[3]/d2l-navigation-dropdown-button-icon")).click();
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

