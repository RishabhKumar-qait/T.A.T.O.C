package Advanced_Course;

import java.sql.Statement;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Scanner;

import com.mysql.jdbc.DatabaseMetaData;

public class Advance {

	public WebDriver driver = null;
	String session_id;

	/*
	 * Web Driver is invoked The download path of chrome driver set to
	 * "D:/Download_data/" Implicit Wait for 3 second
	 */
	@BeforeTest
	public void start_web_driver() {
		System.setProperty("webdriver.chrome.driver", "D:/selenium web driver/chromedriver.exe");
		String downloadFilepath = "D:/Download_data/";

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();

		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);

		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);

		driver = new ChromeDriver(cap);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	/**
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Test(priority = 1)
	public void Advance_cource_move_further() throws SQLException, ClassNotFoundException {
		driver.get("http://10.0.1.86/tatoc");
		driver.findElement(By.linkText("Advanced Course")).click();
		System.out.println("Clicked on Advanced course.");
		driver.findElement(By.xpath("//div[@class='menutop m2']")).click();
		System.out.println("Clicked on Menu 2 from Menu bar");
		driver.findElement(By.xpath("//span[contains(text(),'Go Next')]")).click();
		System.out.println("Clicked on 'Go Next' inside the Menu 2");
		Assert.assertEquals("Query Gate - Advanced Course - T.A.T.O.C", driver.getTitle());
	}
	/**
	 * database connection is done fetch the Name and PassKey for the symbol
	 */
	@Test(priority = 2)
	public void tatoc_database_connection_for_Name_and_PassKey() {
		driver.get("http://10.0.1.86/tatoc/advanced/query/gate");
		// symbol id get for the credentials and identity
		String symbol = driver.findElement(By.xpath("//div[@name='symboldisplay']")).getText().toLowerCase();
		String sbl = "'" + symbol + "'";
		String url = "jdbc:mysql://10.0.1.86/tatoc";
		String username = "tatocuser";
		String password = "tatoc01";
		String _Name = null;
		String _passkey = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, username, password);
			Statement stmt = (Statement) con.createStatement();
			String query3 = "select credentials.name,credentials.passkey from identity INNER JOIN credentials ON identity.id=credentials.id where identity.symbol="
					+ sbl;
			ResultSet rs4 = stmt.executeQuery(query3);
			while (rs4.next()) {
				_Name = rs4.getString(1);
				_passkey = rs4.getString(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.findElement(By.xpath("//input[@id='name']")).sendKeys(_Name);
		System.out.println("Name is entered in Name Text field");
		driver.findElement(By.xpath("//input[@id='passkey']")).sendKeys(_passkey);
		System.out.println("PassKey is typed in Passkey Text field");
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		System.out.println("Proced button is clicked");
		Assert.assertEquals("Video Player - Advanced Course - T.A.T.O.C", driver.getTitle());
	}
	/**
	 * Get session id Generate Token for current session id using rest service
	 * Register for access using generated token and register rest service
	 *
	 * @throws MalformedURLException
	 * @throws JSONException
	 */
	@Test(priority = 3)
	public void rest_service_to_generate_token() throws MalformedURLException, JSONException {
		driver.get("http://10.0.1.86/tatoc/advanced/rest");
		// get session id
		System.out.println("Getting the session id");
		String session_id1 = driver.findElement(By.xpath("//span[@id='session_id']")).getText();
		session_id = session_id1.substring(12);
		URL url = new URL("http://10.0.1.86/tatoc/advanced/rest/service/token/" + session_id);
		given().expect().statusCode(200).when().get(url);
		// token is retrieved
		String expires = given().expect().statusCode(200).when().get(url).then().extract().jsonPath()
				.getString("expires");
		String token = given().expect().statusCode(200).when().get(url).then().extract().jsonPath().getString("token");
		System.out.println("Session_id , Signature and Allow_access is post with rest assured");
		URL url1 = new URL("http://10.0.1.86/tatoc/advanced/rest/service/register?id=" + session_id + "&signature="
				+ token + "&allow_access=1");
		given().expect().statusCode(200).when().post(url1);
	}
	/**
	 * File downloading
	 * 
	 * @throws FileNotFoundException
	 */
	@Test(priority = 4)
	public void File_handle_dat_file_download() throws FileNotFoundException {

		driver.get("http://10.0.1.86/tatoc/advanced/file/handle");

		driver.findElement(By.linkText("Download File")).click();
		System.out.println("File is downloading...");
	}
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws InterruptedException
	 */
	@Test(priority = 5)
	public void File_handle_signature_submitted() throws FileNotFoundException, InterruptedException {
		driver.get("http://10.0.1.86/tatoc/advanced/file/handle");
		String signature = null;
		String signature1 = null;
		File file = new File("D:/Download_data/file_handle_test.dat");
		// Note: To avoid "java.io.FileNotFoundException"
		Thread.sleep(300);
		// FileInputStream fileinput = null;
		Scanner scnr = new Scanner(file);
		while (scnr.hasNextLine()) {
			signature1 = scnr.nextLine();
		}
		signature = signature1.substring(11, 17) + signature1.substring(19);
		System.out.println(signature);
		driver.findElement(By.xpath("//input[@id='signature']")).sendKeys(signature);
		System.out.println("Signature is types in Text");
		driver.findElement(By.xpath("//input[@value='Proceed']")).click();
		System.out.println("Clicked on proceed button");
		Assert.assertEquals("End - T.A.T.O.C", driver.getTitle());
		driver.close();

	}

}
