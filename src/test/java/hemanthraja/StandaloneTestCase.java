package hemanthraja;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class StandaloneTestCase {

    public static void main(String[] args){

        //Setting up the WebDriver manager
        WebDriverManager.chromedriver().setup();

        //Initializing the driver object with chromedriver class
        WebDriver driver = new ChromeDriver();

        //Setting implicitly wait for 10 seconds
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //maximize the window
        driver.manage().window().maximize();

        //declaring explicit wait
        Wait wait = new WebDriverWait(driver,Duration.ofSeconds(5));

        //Navigating to the expected/application under test url
        driver.get("https://rahulshettyacademy.com/client");

        //do login to the account with the credentials
        driver.findElement(By.id("userEmail")).sendKeys("rajahemanth1998@gmail.com");
        driver.findElement(By.id("userPassword")).sendKeys("Hemanth123");
        driver.findElement(By.id("login")).click();

        //Add the desired product to the cart from the list of products displayed, here the desired product name is "zara coat 3"
        List<WebElement> products = driver.findElements(By.cssSelector(".mb-3"));
        WebElement desiredProduct = products.stream()
                .filter(product->product.findElement(By.cssSelector("b")).getText().equalsIgnoreCase("zara coat 3"))
                .findFirst().orElse(null);
        desiredProduct.findElement(By.cssSelector("button:last-of-type")).click();

        //wait till page loading icon disappears and verify Product added to cart toast message
        WebElement pageLoading = driver.findElement(By.cssSelector(".la-ball-scale-multiple div:first-of-type"));
        wait.until(ExpectedConditions.invisibilityOf(pageLoading));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));

        //click on the Cart button to go to cart page
        driver.findElement(By.cssSelector("button[routerlink*='cart']")).click();
    }
}
