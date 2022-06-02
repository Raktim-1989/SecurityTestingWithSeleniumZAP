package ZAPSecurity.Selenium;

import org.junit.Assert;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ZAPSecurityTest {
	
	static final String ZAP_PROXY_ADDRESS = "localhost";
	static final int ZAP_PROXY_PORT = 8080;
	static final String ZAP_API_KEY = "hfehghhr39v8famo022nmkuq84";
	
	//concept is like we are running one proxy server in localhost:8080 ans we are integrating selenium 
	//with this proxy server, so when traffic comes in our application , it will redirect the traffic towards this proxy
	//server will scan those traffic and will prepare a .html report 
	
	private WebDriver driver;
	private ClientApi api;
	
	@BeforeMethod
	public void setUp()
	{
		String proxyServerURL = ZAP_PROXY_ADDRESS + ":" + ZAP_PROXY_PORT;
		
		Proxy proxy = new Proxy();
		proxy.setHttpProxy(proxyServerURL);
		proxy.setSslProxy(proxyServerURL);
		
		ChromeOptions co = new ChromeOptions();
		co.setAcceptInsecureCerts(true);
		co.setProxy(proxy);
		
		
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(co);
		
		api = new ClientApi(ZAP_PROXY_ADDRESS, ZAP_PROXY_PORT ,ZAP_API_KEY );
		
	}
	
	
	@Test
	public void amazonSecurityTest()
	{
		driver.get("https://www.amazon.com");
		Assert.assertTrue(driver.getTitle().contains("Amazon"));
		
	}
	
	@AfterMethod
	public void tearDown()
	{
		if(api != null)
		{
			String title = "Amazon ZAP Security Report"; 
			String template = "traditional-html";
			String description = "This is Amazon ZAP Security Test Report";
			String reportfilename = "amazon-zap-report.html";
			String targetFolder =  System.getProperty("user.dir") ;
			
			
			try {
				ApiResponse response = api.reports.generate(title, template, null, description, null, null, null, null
						, null, reportfilename, null, targetFolder, null);
			System.out.println("ZAP REPORT GENERATED AT THIS LOCATION ***  " + response.toString());
			
			
			} catch (ClientApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		driver.quit();
		
	}
	
}
