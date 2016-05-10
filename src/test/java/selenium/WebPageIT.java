package selenium;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class WebPageIT {

    private static WebDriver webDriver;
    private PageObject pageObject;

    @BeforeClass
    public static void init() throws InterruptedException {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.htmlUnit();
        desiredCapabilities.setBrowserName("firefox");
        desiredCapabilities.setJavascriptEnabled(true);

        webDriver = new FirefoxDriver(desiredCapabilities);

        for(int i=0; i<30; i++){
            boolean ready = JBossUtil.isJBossUpAndRunning();
            if(!ready){
                Thread.sleep(1_000); //check every second
                continue;
            } else {
                break;
            }
        }
    }
    @AfterClass
    public static void tearDown() {
        webDriver.close();
    }

    @Before
    public void startFromInitPage() {
        assumeTrue(JBossUtil.isJBossUpAndRunning());

        pageObject = new PageObject(webDriver);
        pageObject.toStartingPage();
        assertTrue(pageObject.isOnPage());
        assertEquals("http://localhost:8080/pg/", webDriver.getCurrentUrl());
    }

    @Test
    public void isServerUpTest() throws Exception {
        assertTrue(JBossUtil.isJBossUpAndRunning());
    }

    @Test
    public void firstLinkTest()throws Exception {
        pageObject.clickButton("btn1");
        assertTrue(webDriver.getCurrentUrl().contains("http://localhost:8080/pg/rs/convertion?from=USD&to=NOK"));
    }

    @Test
    public void secondLinkTest() throws Exception {
        pageObject.clickButton("btn2");
        assertTrue(webDriver.getCurrentUrl().contains("http://localhost:8080/pg/rs/convertion?from=EUR&to=NOK"));
    }

    @Test
    public void thirdLinkTest() throws Exception {
        pageObject.clickButton("btn3");
        assertTrue(webDriver.getCurrentUrl().contains("http://localhost:8080/pg/rs/convertion?from=GBP&to=NOK"));
    }

    @Test
    public void xpathTest() throws Exception {
        pageObject.clickButton("btn3");
        assertTrue(pageObject.checkFromXmlElement("from"));
    }

    @Test
    public void xpathTest2() throws Exception {
        pageObject.clickButton("btn2");
        assertTrue(pageObject.checkFromXmlElement("to"));
    }
}
