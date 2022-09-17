package hemanthraja;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<String> listOfProducts = new ArrayList<>();
        listOfProducts.add("ZARA COAT 3");
        listOfProducts.add("ADIDAS ORIGINAL");

        //concatenating all the strings present in the list into one string
        String nameOfTheItemsInSingleString = String.join("$+",listOfProducts);

        //looping the add to cart based on the count of desired products need to be added to cart.
        //Streams concept is used to add the products in the cart instead for nested/multiple "for" loops
        List<WebElement> desiredProducts = products.stream()
                .filter(product->nameOfTheItemsInSingleString.contains(product.findElement(By.cssSelector("b")).getText()))
                .collect(Collectors.toList());
        desiredProducts.forEach(d->
        {d.findElement(By.cssSelector("button:last-of-type")).click();//clicks on add cart button
            WebElement pageLoading = driver.findElement(By.cssSelector(".la-ball-scale-multiple div:first-of-type"));
            wait.until(ExpectedConditions.invisibilityOf(pageLoading));//validates loading icon disappears
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#toast-container")));//validates toast message appears
        }
            );

        //click on the Cart button to go to cart page
        driver.findElement(By.cssSelector("button[routerlink*='cart']")).click();

        //Verify added products are in the cart page
        List<WebElement> productsInCartPage = driver.findElements(By.cssSelector(".cartSection h3"));
        for(int i=0;i<listOfProducts.size();i++){
            int finalI = i;
            boolean isProductPresent = productsInCartPage.stream().anyMatch(cartProduct->cartProduct.getText().equalsIgnoreCase(listOfProducts.get(finalI)));
            Assert.assertTrue(isProductPresent,"Product "+listOfProducts.get(i)+" is not present in the cart page");
        }

        //Click on Checkout button in the cart page
        driver.findElement(By.cssSelector("li.totalRow:last-of-type button")).click();

        //Enter the details in checkout page
        Actions a = new Actions(driver);
        a.sendKeys(driver.findElement(By.cssSelector("input[placeholder='Select Country']")),"india").build().perform();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".ta-results"))));
        a.click(driver.findElement(By.cssSelector(".ta-item:nth-of-type(2)"))).build().perform();

        //click on Place Order button
        driver.findElement(By.xpath("//a[contains(.,'Place Order')]")).click();

        //Verify thank you for the order text
        String thankYouMessage = driver.findElement(By.tagName("h1")).getText();
        Assert.assertEquals(thankYouMessage,"THANKYOU FOR THE ORDER.");

        //close the invoked browser
        driver.quit();
    }
}
