package iospack;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class Switching_Apps
{
	public static void main(String[] args) throws Exception
	{
		//Start Appium server programmatically
		AppiumServiceBuilder sb=new AppiumServiceBuilder();
        sb.usingAnyFreePort();
		sb.usingDriverExecutable(new File("/usr/local/bin/node"));
		sb.withAppiumJS(new File("/usr/local/bin/appium"));
		HashMap<String,String> ev=new HashMap<>();
		ev.put("PATH","/usr/local/bin:"+System.getenv("PATH"));
		sb.withEnvironment(ev);
		AppiumDriverLocalService as=AppiumDriverLocalService.buildService(sb);
		as.start();		
		//Provide capabilities related to Simulator and AUT App
		DesiredCapabilities dc=new DesiredCapabilities();
		dc.setCapability("automationName","XCUITest");
		dc.setCapability("platformName","iOS");
		dc.setCapability("platformVersion","13.5");
		dc.setCapability("deviceName","iPhone 8"); 
		dc.setCapability("app","https://github.com/cloudgrey-io/the-app/releases/download/v1.2.1/TheApp-v1.2.1.app.zip");
		//Declare driver object to launch app via appium server
		IOSDriver driver=new IOSDriver(as.getUrl(),dc);
		Thread.sleep(10000);
		//Automation
		try
		{
			//Now launch Photos app
			HashMap<String,Object> hm=new HashMap<String,Object>();
            hm.put("bundleId","com.apple.mobileslideshow");
            driver.executeScript("mobile:launchApp",hm);
            Thread.sleep(1000);
            //Now back to our AUT
            hm.put("bundleId","io.cloudgrey.the-app");
            driver.executeScript("mobile:activateApp",hm);
            Thread.sleep(1000);
            //Now reactivate the Photos app and close that app
            hm.put("bundleId","com.apple.mobileslideshow");
            driver.executeScript("mobile:activateApp",hm);
            Thread.sleep(1000);
            driver.executeScript("mobile:terminateApp",hm);
            Thread.sleep(5000);
            //Now reactivate our AUT
            hm.put("bundleId","io.cloudgrey.the-app");
            driver.executeScript("mobile:activateApp",hm);
            Thread.sleep(1000);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		finally
		{
			//Close app
			driver.closeApp();
			as.stop();
		}
	}
}
