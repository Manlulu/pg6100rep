package selenium;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageObject {

    private final WebDriver webDriver;

    public PageObject(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void toStartingPage(){
        String context = "/pg";  // Kanskjue jeg må endre inne i jboss-web.xml
        webDriver.get("localhost:8080" + context);
        waitForPageToLoad();
    }

    public boolean isOnPage() {
        return webDriver.getTitle().contains("Repetition");
    }

    public void clickNavBar(){
        WebElement webElement = webDriver.findElement(By.id("usersMenu"));
        webElement.click();
        waitForPageToLoad();
    }

    public void clickButton(String id){
        WebElement webElement = webDriver.findElement(By.id(id));
        webElement.click();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        waitForPageToLoad();
    }

    public boolean checkFromXmlElement(String from) {
        try {
            WebElement webElement = webDriver.findElement(By.xpath("//" + from));
            return true;
        } catch (Exception e){
            return false;
        }
    }

    //------------------------------------

    private Boolean waitForPageToLoad() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) webDriver;
        WebDriverWait wait = new WebDriverWait(webDriver, 10); //give up after 10 seconds

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }
}
