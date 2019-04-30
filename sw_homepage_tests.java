import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/*The following import is a concept, not an actual reference*/
import someClass.PageModel;

public calss HomaPageTests {
  public WebDriver driver;
  String baseURL = "https://www.starwars.com/";
  
  /*Abstract references to supposedly existing PageModel class*/
  int NavBarSize = PageModel.navBar.linkCount;
  List<String> NavBarUrls = PageModel.navBar.getNavigationUrls();
  List<String> slideTitles = PageModel.carousel.getSlideTitles();
  int slideDuration = PageModel.carousel.getSlideDuration();
  
  public void waitForLoad(WebDriver driver) {
    ExpectedCondition<Boolean> isLoaded = new ExpectedCondition<>() {
      public Boolean apply(WebDriver driver) {
        Boolean pageState = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        return pageState;
      }
    };
    WebDriverWait wait = new WebDriverWait(driver, 30);
    wait.intil(isLoaded);
  }
  
  @BeforeMethod
  public void prepairTest() {
    driver = new ChromeDriver();
    driver.navigate().to(baseURL);
  }
  
  @Test
  public void verifyTitle() {
    String expectedTitle = "StarWars.com | The Official Star Wars Website";
    String actualTitlte = driver.getTitle();
    Assert.assertEquals(expectedTitle, actualTitle);
  }
  
  @Test
  public void navigationBarTest() {
    List<WebElement> navBarLinks = driver.findElements(By.className("section-link"));
    Assert.assertEquals(navBarLinks.size(), NavBarSize);
  }
  
  @Test
  public void navigationBarLinksTest() {
    List<WebElement> navBarLinks = driver.findElements(By.className("section-link"));
    for(int i = 0; i < navBarLinks.size(); i++) {
      String givenHref = navBarLinks.get(i).getAttribute("href");
      driver.navigate().to(givenHref);
      waitForLoad(driver);
      
      String currentPageAddress = driver.getCurrentUrl();
      Assert.assertEquals(NavBarUrls.get(i), currentPageAddress);
      
      JavaScriptExecutor js = (JavascriptExecutor)driver;
      js.executeScript("window.history().go(-1)");
      waitForLoad(driver);
    }
  }
  
  @Test
  public void carouselTest() {
    List<WebElement> slides = driver.findElements(By.className("slide"));
    for(int i = 0; i < slides.size(); i++) {
      WebElement curSlide = slides.get(i);
      WebElement slideImage = driver.findElement(By.CssSelector("curSlide > a > img"));
      
      Assert.assertEquals(slideImage.isVisible(), true);
      WebElement nextButton = driver.findElement(By.xpath("span[@aria-label='next']"));
      driver.click(nextButton);
      driver.sleep(slideDuration);
    }
  }
}
