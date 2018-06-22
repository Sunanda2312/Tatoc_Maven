import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;


public class Tatoc {
	
	WebDriver driver;
	
	public Tatoc() {
		System.setProperty("webdriver.chrome.driver","C:\\Users\\sunanda1\\Downloads\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://10.0.1.86/tatoc/");
    }
	
		
	@Test
	public void BasicCourse(){
		Assert.assertEquals(this.driver.findElement(By.xpath("/html/body/div/div[2]/a[1]")).isDisplayed(), true);
		driver.findElement(By.xpath("/html/body/div/div[2]/a[1]")).click();
		Assert.assertEquals(this.driver.findElement(By.className("greenbox")).isDisplayed(), true);
	}
	
	@Test
	public void greenBox() {                                            //greebox
		driver.findElement(By.className("greenbox")).click();
		String expectedUrl = "http://10.0.1.86/tatoc/basic/frame/dungeon";
        Assert.assertEquals(expectedUrl, driver.getCurrentUrl(), "Didn't navigate to correct webpage");
	}
	
	@Test(dependsOnMethods = {"greenBox"})
    public void frameDungeon() {                                       // frames

        driver.switchTo().frame("main");
        Assert.assertEquals(this.driver.findElement(By.id("answer")).isDisplayed(), true); 

        String box1 = driver.findElement(By.id("answer")).getAttribute("class");
        int n=0;
        while (n==0) {

            driver.findElement(By.cssSelector("a")).click();
            driver.switchTo().frame("child"); 
            String box2 = driver.findElement(By.id("answer")).getAttribute("class");
            driver.switchTo().parentFrame(); 
            if (box1.equals(box2)) {
                driver.findElements(By.cssSelector("a")).get(1).click(); 
                n=1;
            }
        }

        String expectedUrl = "http://10.0.1.86/tatoc/basic/drag"; 
        Assert.assertEquals(expectedUrl, driver.getCurrentUrl(), "Didn't navigate to correct webpage");

    }
	
	 @Test(dependsOnMethods = {"frameDungeon"})
	    public void drag() {                                       //drag and drop
	        Actions actions = new Actions(driver);

	        Assert.assertEquals(this.driver.findElement(By.id("dropbox")).isDisplayed(), true); 
	        Assert.assertEquals(this.driver.findElement(By.id("dragbox")).isDisplayed(), true); 

	        WebElement drop = driver.findElement(By.id("dropbox")); 
	        WebElement drag = driver.findElement(By.id("dragbox")); 
	        actions.dragAndDrop(drag, drop); 
	        actions.build().perform();
	        driver.findElement(By.cssSelector("a")).click();
	        String expectedUrl = "http://10.0.1.86/tatoc/basic/windows"; 
	        Assert.assertEquals(expectedUrl, driver.getCurrentUrl(), "Didn't navigate to correct webpage");

	    }

	    @Test(dependsOnMethods = {"drag"})
	    public void popup() {                                 //popup

	        String parentWindowHandler = driver.getWindowHandle(); 
	        
	        driver.findElement(By.cssSelector("a")).click(); 
	        String subWindowHandler = null;

	        Set<String> handles = driver.getWindowHandles(); 
	        System.out.println(handles);

	        Iterator<String> iterator = handles.iterator(); 
	        while (iterator.hasNext()) {
	            subWindowHandler = iterator.next();
	        }
	        driver.switchTo().window(subWindowHandler);
	        driver.findElement(By.id("name")).sendKeys("sunanda");
	        driver.findElement(By.id("submit")).click();

	        driver.switchTo().window(parentWindowHandler);  
	        driver.findElements(By.cssSelector("a")).get(1).click(); 
	        String expectedUrl = "http://10.0.1.86/tatoc/basic/cookie"; 
	        Assert.assertEquals(expectedUrl, driver.getCurrentUrl(), "Didn't navigate to correct webpage");

	    }

	    @Test(dependsOnMethods = {"popup"})
	    public void cookie() {                         //cookie

	        int n = 1;
	        
	        while (n > 0) {
	            try {
	                driver.findElement(By.cssSelector("a")).click(); 
	                String value = driver.findElement(By.id("token")).getText().split("Token: ")[1];  
	                n = 0; 
	            } catch (Exception e) {
	                System.out.println(e);
	                driver.navigate().refresh(); 
	            }
	        }

	        String value = driver.findElement(By.id("token")).getText().split("Token: ")[1]; 
	        driver.manage().addCookie(new Cookie("Token", value, "/")); 
	        driver.findElements(By.cssSelector("a")).get(1).click(); 
	        String expectedUrl = "http://10.0.1.86/tatoc/end"; 
	        Assert.assertEquals(expectedUrl, driver.getCurrentUrl(), "Didn't navigate to correct webpage");

	    }

	
}
