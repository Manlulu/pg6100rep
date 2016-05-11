package selenium;


import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig; // Måtte copy/paste denne inn manuelt.

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class WebPageIT {

    private static WebDriver webDriver;
    private PageObject pageObject;

    private static WireMockServer wireMockServer;

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

        wireMockServer = new WireMockServer(
                wireMockConfig().port(8099).notifier(new ConsoleNotifier(true))
        );
        wireMockServer.start();

    }
    @AfterClass
    public static void tearDown() {
        webDriver.close();
        wireMockServer.stop();
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
        pageObject.clickButton("btnUSD");
        assertTrue(webDriver.getCurrentUrl().contains("http://localhost:8080/pg/rs/convertion?from=USD&to=NOK"));
    }

    @Test
    public void secondLinkTest() throws Exception {
        pageObject.clickButton("btnEUR");
        assertTrue(webDriver.getCurrentUrl().contains("http://localhost:8080/pg/rs/convertion?from=EUR&to=NOK"));
    }

    @Test
    public void thirdLinkTest() throws Exception {
        pageObject.clickButton("btnGBP");
        assertTrue(webDriver.getCurrentUrl().contains("http://localhost:8080/pg/rs/convertion?from=GBP&to=NOK"));
    }

    @Test
    public void xpathTest() throws Exception {
        pageObject.clickButton("btnGBP");
        assertTrue(pageObject.checkFromXmlElement("from"));
    }

    @Test
    public void xpathTest2() throws Exception {
        pageObject.clickButton("btnEUR");
        assertTrue(pageObject.checkFromXmlElement("to"));   // For å teste click uten wait for page.
    }

    @Ignore
    @Test
    public void wireMockTest() throws Exception {
        int eur = 42;

        String json = getAMockedJsonResponse(1, eur, 2);
        stubJsonResponse(json);

        PageObject pageObject = new PageObject(webDriver);
        pageObject.clickButton("btnEUR");

        String source = webDriver.getPageSource();
        assertTrue(source, source.contains("EUR"));
        assertTrue(source, source.contains("" + eur));
    }


    // -------------------------------------------

    private String getAMockedJsonResponse(int usd, int eur, int gbp){
        String json = "{";
        json += "'base': 'NOK' , ";
        json += "'date': '2016-04-29' , ";
        json += "'rates': {";
        json += "'USD': "+usd+", ";
        json += "'EUR': "+eur+", ";
        json += "'GBP': "+gbp+", ";
        json += "}";
        json += "}";
        return json;
    }
    private void stubJsonResponse(String json){
        wireMockServer.stubFor(
                get(urlMatching("/latest.*")).withQueryParam("base",matching("NOK"))
                        .willReturn(aResponse()
                                .withHeader("Content-Type","text/json; charset=utf-8")
                                .withHeader("Content-Length",""+json.length())
                                .withBody(json)));
    }
}
