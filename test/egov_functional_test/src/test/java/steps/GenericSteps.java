package steps;

import com.testvagrant.stepdefs.exceptions.NoSuchEventException;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import pages.GenericPage;
import utils.StringExtract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.testvagrant.stepdefs.core.Tapster.tapster;

public class GenericSteps extends BaseSteps {

    public static List<String> dataTableStore = new ArrayList<>();
    public static int i = 0;
    private static Map<String, String> copyValues = new HashMap<>();
    private WebElement webElement;

    @Given("^(\\w+)\\s+on\\s+(\\w+)\\s+screen\\s+(\\w+)\\s+on\\s+(\\w+)\\s+value\\s+(.*)$")
    public void consumerOnScreenPerformsActionOnElementWithValue(String consumer, String screen, String action, String element, String value) throws NoSuchEventException, IOException, InterruptedException {

        TimeUnit.SECONDS.sleep(1);

        if (!value.equals("null")) {

            value = pageStore.get(GenericPage.class).findDataIsComingFromDataTable(value);
            if (copyValues.containsKey(value))
                value = copyValues.get(value);
            else
                value = String.valueOf(pageStore.get(GenericPage.class).checkValueCanBeAutoGeneratedOrNot(value));

            if (value.contains("/"))
                value = value.replaceAll("/", "");

            switch (action) {

                case "selects":
                    webElement = pageStore.get(GenericPage.class).buildElement(screen, element, value);
                    pageStore.get(GenericPage.class).clickOnDropdown(webElement, value);
                    break;

                case "uploads":
                    pageStore.get(GenericPage.class).tapsterServesAction(consumer, screen, element, action,
                            System.getProperty("user.dir") + "/src/test/resources/" + value);
                    break;

                default:
                    webElement = pageStore.get(GenericPage.class).buildElement(screen, element, value);
                    pageStore.get(GenericPage.class).tapsterServesActionWithElement(consumer, screen, action, value, webElement);
            }
        }
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\s+screen\\s+(\\w+)\\s+on\\s+(\\w+)$")
    public void consumerOnScreenPerformsActionOnElement(String consumer, String screen, String action, String element) throws NoSuchEventException, IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        webElement = pageStore.get(GenericPage.class).buildElement(screen, element, "");
        pageStore.get(GenericPage.class).tapsterServesActionWithElement(consumer, screen, action, "", webElement);
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen verifies\\s+(\\w+)\\s+is\\s+(.*)$")
    public void assertElement(String consumer, String screen, String element, String action) throws NoSuchEventException, IOException, InterruptedException {
        webElement = pageStore.get(GenericPage.class).buildElement(screen, element, "");
        pageStore.get(GenericPage.class).tapsterServesActionWithElement(consumer, screen, action, "", webElement);
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen verifies\\s+(\\w+)\\s+has\\s+(\\w+)\\s+value\\s+(.*)$")
    public void assertElementWithValue(String consumer, String screen, String element, String action, String value) throws NoSuchEventException, IOException, InterruptedException {
        if (copyValues.containsKey(value)) {
            value = copyValues.get(value);
            webElement = pageStore.get(GenericPage.class).buildElement(screen, element, value);
            pageStore.get(GenericPage.class).tapsterServesActionWithElement(consumer, screen, action, value, webElement);
        }
        else{
            webElement = pageStore.get(GenericPage.class).buildElement(screen, element, value);
            pageStore.get(GenericPage.class).tapsterServesActionWithElement(consumer, screen, action, value, webElement);
        }
    }

    @And("^(\\w+) on (\\w+) screen performs following actions$")
    public void userOnScreenPerformsFollowingActions(String consumer, String screen, DataTable dataTable) throws Throwable {
        List<List<String>> data = dataTable.raw();
        int x = 0;

        for (List<String> aData : data) {
            String action = aData.get(x++);
            String element = aData.get(x++);
            String value = aData.get(x);
            value = pageStore.get(GenericPage.class).checkValueCanBeAutoGeneratedOrNot(value);
            WebElement webElement = pageStore.get(GenericPage.class).buildElement(screen, element, value);
            tapster().useDriver(pageStore.getDriver())
                    .asConsumer(consumer)
                    .onScreen(screen)
                    .doAction(action)
                    .onElement(element)
                    .withValue(value)
                    .serveWithElement(webElement);
            x = 0;
        }
    }

    @Given("^(Intent):(.*)$")
    public void intent(String action, String intentId) throws Throwable {
        intent.run(intentId);
    }

    @Given("^(DataIntent):(.*)$")
    public void intentWithDataTable(String action, String intentId, DataTable dataTables) throws Throwable {
        List<List<String>> data = dataTables.raw();
        for (List<String> aData : data) {
            dataTableStore.addAll(aData);
        }
        intent.run(intentId);
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen\\s+(\\w+)\\sthe\\s+(\\w+)\\s+to\\s+(\\w+)$")
    public void userOnScreenCopiesTextToScenarioContext(String consumer, String screen, String action, String element, String placeHolder) throws Throwable {

        WebElement webElement = pageStore.get(GenericPage.class).buildElement(screen, element, "");

        switch (placeHolder) {
            case "applicationNumber":
                copyValues.put(placeHolder, pageStore.get(StringExtract.class).getComplaintNumber(webElement));
                break;
            case "user":
                copyValues.put(placeHolder, webElement.getText().split("::")[0]);
                break;
            default:
                if (!webElement.getText().equals(""))
                    copyValues.put(placeHolder, webElement.getText());
                else
                    copyValues.put(placeHolder, webElement.getAttribute("value"));
                break;
        }
    }

    @And("^(\\w+)\\s+on\\s+(\\w+)\\sscreen\\s+(\\w+)\\son\\s+(\\w+)\\s+with\\s+above\\s+(.*)$")
    public void userOnScreenTypesOnApplicationSearchWithAboveApplicationNumber(String consumer, String screen, String action, String element, String value) throws Throwable {
        if (copyValues.containsKey(value))
            value = copyValues.get(value);
        if (action.equals("opens")) {
            pageStore.get(GenericPage.class).tapsterServesAction(consumer, screen, element, "types", value);
            pageStore.get(GenericPage.class).openApplication(value).click();
        }
        else {
            webElement = pageStore.get(GenericPage.class).buildElement(screen, element, value);
            pageStore.get(GenericPage.class).tapsterServesActionWithElement(consumer, screen, action, value, webElement);
        }
    }

    @And("^(\\w+)\\s+on (\\w+) screen selects (\\w+) with value as (.*)$")
    public void selectsDropdownWithValue(String consumer, String screen, String element, String value) throws Throwable {
        if (copyValues.containsKey(value))
            value = copyValues.get(value);
        WebElement webElement = pageStore.get(GenericPage.class).buildElement(screen, element, value);
        pageStore.get(GenericPage.class).clickOnDropdown(webElement, value);
    }

    @And("^(\\w+)\\s+on (\\w+) screen types on (\\w+) suggestion box with value (.*)$")
    public void selectsSuggestionBoxWithValue(String consumer,String screen, String element, String value) throws Throwable {
        if (copyValues.containsKey(value))
            value = copyValues.get(value);
        WebElement webElement = pageStore.get(GenericPage.class).buildElement(screen, element, "");
        pageStore.get(GenericPage.class).actionOnSuggestionBox(webElement, value);
    }

    @And("^(\\w+) on (\\w+) screen will see the (.*)$")
    public void userWillSeeElement(String consumer, String screen, String element) throws Throwable {
        pageStore.get(GenericPage.class).buildElement(screen, element, "");
    }

    @And("^user on (\\w+) screen force clicks on (\\w+)$")
    public void userPerformsForceClicks(String screen, String element) throws Throwable {
        WebElement webElement = pageStore.get(GenericPage.class).buildElement(screen, element, "");
        ((JavascriptExecutor) pageStore.getDriver()).executeScript("arguments[0].click();", webElement);
    }

    @And("^user on (\\w+) screen clicks radio button or checkbox on (\\w+)")
    public void userClicksOnRadioButtonOrCheckBox(String screen, String element) throws Throwable {
        WebElement webElement = pageStore.get(GenericPage.class).buildElement(screen, element, "");
        webElement.click();
    }

    @And("^(\\w+) on (\\w+) screen refresh's the webpage$")
    public void userOnHomeScreenRefreshSTheWebpage(String user, String s) throws Throwable {
        pageStore.getDriver().navigate().refresh();
    }

    @And("^(\\w+) on (\\w+) screen will wait until the page loads$")
    public void userOnHomeScreenWillWaitUntilThePageLoads(String user, String screen) throws Throwable {
        pageStore.get(GenericPage.class).buildElement("Home", "background", "");
    }
}