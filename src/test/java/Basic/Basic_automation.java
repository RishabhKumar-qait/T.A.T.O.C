package Basic;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class Basic_automation {

	public WebDriver driver = null;

	@BeforeSuite
	public void start_chrome() {
		System.setProperty("webdriver.chrome.driver", "D:/selenium web driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	/**
	 * Click on Basic course at Home page
	 */
	@Test(priority = 1)
	public void Basic_test() {
		driver.get("http://10.0.1.86/tatoc");
		Assert.assertEquals("Welcome - T.A.T.O.C", driver.getTitle());
		System.out.println("Clicked on Basic Course");
		driver.findElement(By.linkText("Basic Course")).click();
		Assert.assertEquals("Grid Gate - Basic Course - T.A.T.O.C", driver.getTitle());
	}

	/**
	 * red box is clicked to check error page
	 */
	@Test(priority = 2)
	public void Grid_gate_click_redbox_test() {
		driver.get("http://10.0.1.86/tatoc/basic/grid/gate");
		System.out.println("Red box is clicked");
		driver.findElement(By.xpath(".//div[@class='redbox']")).click();
		Assert.assertEquals("Error - T.A.T.O.C", driver.getTitle());
		driver.get("http://10.0.1.86/tatoc/basic/grid/gate");
	}

	/**
	 * green box is clicked to go next page
	 * 
	 */
	@Test(priority = 3)
	public void Grid_gate_click_greenbox_test() { //
		driver.get("http://10.0.1.86/tatoc/basic/grid/gate");
		System.out.println("Green box is clicked");
		driver.findElement(By.xpath(".//div[@class='greenbox']")).click();
		Assert.assertEquals("Frame Dungeon - Basic Course - T.A.T.O.C", driver.getTitle());
	}

	/**
	 * Repaint box 2 to match color with box 1
	 *
	 * @throws InterruptedException
	 */
	@Test(priority = 4)
	public void Grid_gate_click_greenbox_frame_dungeon() throws InterruptedException {
		driver.get("http://10.0.1.86/tatoc/basic/frame/dungeon");
		int size = driver.findElements(By.tagName("iframe")).size(); //
		// Total frame
		// System.out.println("Total frame ..." + size);
		driver.switchTo().frame("main");
		String box_1_color = driver.findElement(By.xpath("//div[contains(text(), 'Box 1')]")).getAttribute("class");
		driver.switchTo().frame("child");
		String box_2_color = driver.findElement(By.xpath("//div[contains(text(), 'Box 2')]")).getAttribute("class");
		System.out.println("box 1 color..." + box_1_color);
		while (!box_2_color.equals(box_1_color)) {
			driver.switchTo().defaultContent();
			// Thread.sleep(10);
			driver.switchTo().frame("main");
			driver.findElement(By.xpath("//a[contains(text(), 'Repaint Box 2')]")).click();
			driver.switchTo().frame("child");
			System.out.println("Repaint Box 2 Link is clicked");
			box_2_color = driver.findElement(By.xpath("//div[contains(text(), 'Box 2')]")).getAttribute("class");
			System.out.println(box_2_color);
		}
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main");
		driver.findElement(By.xpath("//a[contains(text(), 'Proceed')]")).click();
		// Assert.assertEquals("Drag - Basic Course - T.A.T.O.C",
		// driver.getTitle());

	}

	/**
	 * DragBox is dragged on Dropbox then click proceed Assertion is checked
	 */
	@Test(priority = 5)
	public void tatoc_basic_drag() {
		driver.get("http://10.0.1.86/tatoc/basic/drag");
		Actions action = new Actions(driver);
		System.out.println("Drag Me is dragged and dropped to DropBox");
		WebElement dragbox = driver.findElement(By.xpath("//div[@id='dragbox']"));
		WebElement dropbox = driver.findElement(By.xpath("//div[@id='dropbox']"));
		action.moveToElement(dragbox).dragAndDrop(dragbox, dropbox).perform();
		System.out.println("Clicked on Proceed Link");
		driver.findElement(By.xpath("//a[contains(text(),'Proceed')]")).click();
		Assert.assertEquals("Windows - Basic Course - T.A.T.O.C", driver.getTitle());
	}

	/**
	 * Click on Launch Window Submit Name and click to close then click on
	 * proceed to generate the token token is used to add cookie the click
	 * proceed
	 */
	@Test(priority = 6)
	public void tatoc_basic_windows() throws InterruptedException {
		driver.get("http://10.0.1.86/tatoc/basic/windows");
		Assert.assertEquals("Windows - Basic Course - T.A.T.O.C", driver.getTitle());
		System.out.println("Clicked on Launch Popup Window");
		driver.findElement(By.xpath("//a[contains(text(),'Launch Popup Window')]")).click();

		String parentHandle = driver.getWindowHandle();
		for (String handle1 : driver.getWindowHandles()) {
			driver.switchTo().window(handle1);
		}
		System.out.println("User 1 is typed in Text field");
		driver.findElement(By.xpath("//input[@type='text']")).sendKeys("User 1");
		System.out.println("Submit is clicked");
		driver.findElement(By.xpath("//input[@id='submit']")).click();
		driver.getWindowHandles();
		driver.switchTo().window(parentHandle);
		System.out.println("Proceed is clicked");
		driver.findElement(By.xpath("//a[contains(text(),'Proceed')]")).click();
		Assert.assertEquals("Cookie Handling - Basic Course - T.A.T.O.C", driver.getTitle());
		System.out.println("Generate Token is clicked");
		driver.findElement(By.xpath("//a[contains(text(),'Generate Token')]")).click();
		Assert.assertEquals("Cookie Handling - Basic Course - T.A.T.O.C", driver.getTitle());
		String token = driver.findElement(By.xpath("//span[@id='token']")).getText();
		String token1 = token.substring(7);
		Cookie ck = new Cookie("Token", token1);
		System.out.println("Cookie is set.");
		driver.manage().addCookie(ck);
		driver.findElement(By.xpath("//a[contains(text(),'Proceed')]")).click();
		Assert.assertEquals("End - T.A.T.O.C", driver.getTitle());
		driver.close();

	}

}
