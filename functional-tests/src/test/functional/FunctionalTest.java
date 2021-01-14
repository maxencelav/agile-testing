package test.functional;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.*;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;


public class FunctionalTest {

	private WebDriver driver;
    public static JavascriptExecutor executor;

    //harmcrest assert that could be usefull (for array, ...)
    //assertThat(someCollection, not(hasItem(someItem)))
    //softAssert.assertThat("Id not found", availableIds, not(hasItemInArray(userId)));
    //assertThat(new String[] {"foo", "bar"}, hasItemInArray(startsWith("ba")))


	//region helpers function

/*try to use differnet wait , for click issue. but need more time to work on it
    public void waitElement(String selector){
        Wait wait = new FluentWait(driver).withTimeout(15, TimeUnit.SECONDS).pollingEvery(3, TimeUnit.SECONDS).ignoring(Exception.class);

        WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement applyy(WebDriver driver) {
                return driver.findElement(By.cssSelector(selector));
            }
        });
    }

    public void waitElementVisibility(String selector){
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
    }

    public void waitElementClickable(String selector){
        WebDriverWait wait = new WebDriverWait(driver,10);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));
    }
*/

    /**
     * delete some html entities from a string and return the new string
     * @param string => string to modify
     * @return
     */
	private String deleteHtmlEntities(String string){
	    return string.replaceAll("<[^>]*>", "").replaceAll("&nbsp;", " ").replace("\\u00a0", " ");
    }

    /**
     * delete all special characters from a string and return the new string
     * @param string => string to modify
     * @return
     */
    private String deleteAllSpecialChar(String string){
        return string.replaceAll("[^a-zA-Z0-9]", "");
    }



    /**
     * get a WebElement's list found with css selector
     * @param selector	=> css selector
     * @return
     */
    public List<WebElement> getElementList(String selector){
        return driver.findElements(By.cssSelector(selector));
    }

    /**
     * get nb child of a WebElement found with css selector
     * @param selector	=> css selector
     * @return
     */
    public int getElementNbChild(String selector){
        return getElementList(selector).size();
    }

    /**
     * get WebElement's parent thanks to xpath.
     * can get the nth parent, thanks to given int parentNb
     * @param element => a WebElement
     * @param parentNb => nth parents (start to 1)
     * @return
     */
    private WebElement getParent(WebElement element, int parentNb){
        String path = "";
        for (int i=0; i<parentNb; i++){
            path += (i==0 ? ".." : "/..");
        }
        return element.findElement(By.xpath(path));
    }

    /**
     * get WebElement's sibbling thanks to xpath.
     * get the nth sibbling of a certain markup type, thanks to sibblingNb and sibblingMarkup
     * @param element       => a WebElement
     * @param sibblingMarkup => sibbling's markup
     * @param sibblingNb    => nth sibbling
     * @return
     */
    private WebElement getSibbling(WebElement element, String sibblingMarkup, int sibblingNb){
        return element.findElement(By.xpath("following-sibling::" + sibblingMarkup + "[" + sibblingNb +"]"));
    }

    /**
     * get WebElement's child thanks to xpath.
     * use a simple selector to get children
     * @param element   => a WebElement
     * @param selector  => a string with xpath selector
     * @return
     */
    private WebElement getChildByXpath(WebElement element, String selector){
        return element.findElement(By.xpath(selector));
    }

    /**
     * execute a WebElement click() function with JS.
     * @param selector  => a CSS selector
     * @return true if is clickable, else false
     */
    public Boolean clickJs(String selector){
        try{
            executor.executeScript("document.querySelector(\"" + selector + "\").click();" +
                    "setTimeout(function(){ console.log(\"Hello\"); }, 2000);");
        }catch (Exception e){
            System.out.println("------ Click fail ------\n\n"  + e);
            return false;
        }
        return true;
    }

    /**
     * Check if a WebElement is clickable.
     * @param selector  => a CSS selector
     */
    public void checkClickJs(String selector){
        assertThat(clickJs(selector), is(true));
    }

    /**
     * execute a WebElement click() function with Selenium WebElement methods.
     * @param selector  => a CSS selector
     */
    public Boolean clickSelenium(String selector){
        try{
            driver.findElement(By.cssSelector(selector)).click();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public void tryClickElement(String selector){
        Boolean isClickSeleniumWork = clickSelenium(selector);
        if (!isClickSeleniumWork){
            checkClickJs(selector);
        }
    }
    //endregion

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver","/Library/Java/JUNIT/chromedriver");
		driver = new ChromeDriver();
	    	// Seems no more working in last Chrome versions
		// driver.manage().window().maximize();
  		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        executor = (JavascriptExecutor) driver;

    }


    // Test de la Story #2-homepage (https://trello.com/c/glufGucb/45-homepage)
	@Test
    public void testHomepage() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/");
		//region title
        assertEquals("Partagez vos passions | Meetup", driver.getTitle());
        //endregion

        //region Description;  not Working without JS (because of special char maybe (ex: &nbsp!))
        //WebElement pageDescription = driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content");
        String description = executor.executeScript("return document.querySelector(\"meta[name=description]\").content").toString();
        assertEquals(deleteAllSpecialChar("Partagez vos passions et faites bouger votre ville ! Meetup vous aide à rencontrer des personnes près de chez vous, autour de vos centres d'intérêt."), deleteAllSpecialChar(description));
        //endregion

        //region h1 punchline
        WebElement h1 = driver.findElement(By.xpath("//h1"));
        assertEquals("Le monde vous tend les bras", h1.getText());
        //endregion

        // region sub title;
        assertEquals("Rejoignez un groupe local pour rencontrer du monde, tester une nouvelle activité ou partager vos passions.", getSibbling(getParent(h1, 1), "div", 1).getText());
        //endregion

        // region button join meetup;
        WebElement buttonJoin = driver.findElement(By.xpath("//a[@id='joinMeetupButton']"));
        assertEquals("Rejoindre Meetup", buttonJoin.getText());
        assertEquals("https://www.meetup.com/fr-FR/register/", buttonJoin.getAttribute("href"));
        //endregion
	}

    // Test de la Story #2-recherche (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testRecherche() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/find/outdoors-adventure/");

        //region page title & h1 contains *Nature et aventure*
        assertThat(driver.getTitle(), containsString("Nature et aventure"));
        WebElement h1 = driver.findElement(By.cssSelector("h1"));
        assertThat(h1.getText(), containsString("Nature et aventure"));
        //endregion

        //region search fields

        //region search banner
        WebElement searchForm = driver.findElement(By.cssSelector("#searchForm"));
        assertThat(searchForm, is(notNullValue()));
        //endregion

        //region search field
        WebElement searchField = driver.findElement(By.cssSelector("input#mainKeywords"));
        assertThat(searchField, is(notNullValue()));
        //endregion

        //region search radius field
        WebElement searchRadius = driver.findElement(By.cssSelector("ul#simple-radius"));
        assertThat(searchRadius, is(notNullValue()));
        //endregion

        //region city field
        WebElement searchCity = driver.findElement(By.cssSelector("div#simple-location"));
        assertThat(searchCity, is(notNullValue()));
        //endregion

        //region display Group or Calendar
        String calendarGroupSelector = "ul#simple-view-selector";
        WebElement calendarGroupDiv = driver.findElement(By.cssSelector(calendarGroupSelector));
        assertThat(calendarGroupDiv, is(notNullValue()));

        //region select group
        String selectGroupSelector = calendarGroupSelector + " a#simple-view-selector-group";
        WebElement selectGroup = driver.findElement(By.cssSelector(selectGroupSelector));
        assertThat(selectGroup, is(notNullValue()));
        assertEquals("Groupes", selectGroup.getText());
        //endregion

        //region select calendar
        String selectCalendarSelector = calendarGroupSelector + " a#simple-view-selector-event";
        WebElement selectCalendar = driver.findElement(By.cssSelector(selectCalendarSelector));
        assertThat(selectCalendar, is(notNullValue()));
        assertEquals("Calendrier", selectCalendar.getText());
        //endregion
        //endregion
        //endregion

        // region Le tri par défaut est le tri par pertinence.
        String triSelector = "#simple-find-order";
        WebElement triLink = driver.findElement(By.cssSelector( triSelector + " > a"));
        assertEquals("pertinence", triLink.getText());

        WebElement triLink2 = driver.findElement(By.cssSelector( triSelector + " > ul li:not(.display-none) a[data-value=default]"));
        assertEquals("pertinence", triLink2.getAttribute("data-copy"));
        //endregion

        //region 4 available sort : *pertinence, plus récents, nombre de membres, proximité*

        //region Js solution
        //Boolean isTriComplete = (Boolean) executor.executeScript(
        //        "let list = document.querySelectorAll('" + triSelector + " > ul li:not(.display-none) ');" +
        //                "let arrayFinal = ['pertinence', 'plus récents', 'nombre de membres', 'proximité'];" +
        //                "let array = [];" +
        //                "list.forEach(li =>" +
        //                    "{" +
        //                    "array.push(li.firstElementChild.getAttribute('data-copy'));" +
        //                    "}" +
        //                ");" +
        //        "return array.length === arrayFinal.length && array.every((value, index) => value === arrayFinal[index]);");
        //assertEquals(true, isTriComplete);
        //endregion

        //region Selenium solution
        List<WebElement> sortListElement = getElementList(triSelector + " > ul li:not(.display-none) ");

        ArrayList<String> sortExpected = new ArrayList<String>(Arrays.asList("pertinence", "plus récents", "nombre de membres", "proximité"));
        ArrayList<String> sortFound = new ArrayList<String>();
        for (WebElement element: sortListElement ) {
            sortFound.add(getChildByXpath(element, "a").getAttribute("data-copy"));
        }
        Collections.sort(sortExpected);
        Collections.sort(sortFound);

        assertEquals(sortExpected, sortFound);
        //endregion
        //endregion

        //region click on calendar
        // la liste se met à jour et affiche des événements jour par jour ainsi qu'un calendrier.

        //region before clicking calendar
        //region try get datepicker
        String datepickerSelector = "div#simple-event-filter-column";
        WebElement datepickerBefore = driver.findElement(By.cssSelector(datepickerSelector));
        assertThat(datepickerBefore.getAttribute("class"), containsString("display-none"));
        //endregion

        //region get event
        String eventsPageSelector = "div#C_pageBody";
        String eventsPageBeforeContent = driver.findElement(By.cssSelector(eventsPageSelector)).getText();
        //endregion
        //endregion

        //click on calendar
        tryClickElement(selectCalendarSelector);

        //region after clicking on calendar
        //region  get datepicker
        WebElement datepickerAfter = driver.findElement(By.cssSelector(datepickerSelector));
        assertThat(datepickerAfter.getAttribute("class"), not(containsString("display-none")));
        //endregion

        //region get event
        WebElement eventsPage = driver.findElement(By.cssSelector(eventsPageSelector));
        assertNotSame(eventsPage.getText(), eventsPageBeforeContent);
        //endregion
        //endregion

        //endregion


        //region on click on datepicker => events list

        //ensure to be back to calendar view
        tryClickElement(selectCalendarSelector);

        //region click on date from datepicker
        String dateSelector = datepickerSelector + " table.ui-datepicker-calendar .date_21";
        tryClickElement(dateSelector);
        //endregion

        //region after clicking dapicker, check date
        //region get events list, check first date
        String eventListSelector = "#simple-view > div ul >li";
        WebElement eventsList = driver.findElement(By.cssSelector(eventListSelector));
        ///date of first events are equals or greater than 21
        int firstEventDay = Integer.parseInt(eventsList.getAttribute("data-day"));
        assertThat(21 < firstEventDay, is(true));
        //endregion

        //region click on first event
        tryClickElement(eventListSelector + " + li > ul > li div.chunk > a");
        //endregion

        //region new page, with event details, check dates day
        WebElement dateFromOpenedPage = driver.findElement(By.cssSelector("time[datetime]"));

        Date time = new Date(Long.parseLong(dateFromOpenedPage.getAttribute("datetime")));
        assertEquals(String.valueOf(firstEventDay), String.valueOf(time.getDate()));
        //endregion

        //endregion
        //endregion
    }

    // Test de la Story #2-fiche_meetup (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testFicheMeetup() throws Exception {
        driver.get("https://www.meetup.com/fr-FR/promenades-et-randonnees/");

        //region clickable title with meetup name, place, number of members, organisators, a button to join the event, and a picture
        //region h1 clickable
        WebElement h1 = driver.findElement(By.cssSelector("h1 > a"));
        assertThat(h1, is(notNullValue()));
        assertThat(driver.getTitle(), containsString(h1.getText()));

        tryClickElement("h1 > a");

        //endregion

        //region details

        //location
        assertThat(driver.findElement(By.cssSelector("a.groupHomeHeaderInfo-cityLink")), is(notNullValue()));
        //total member
        assertThat(driver.findElement(By.cssSelector("a.groupHomeHeaderInfo-memberLink")), is(notNullValue()));
        //organizer
        assertThat(driver.findElement(By.cssSelector("a.orgInfo-name-superGroup")), is(notNullValue()));
        //endregion

        //region button join group
        String selectorJoinButton = "a#actionButtonLink";
        WebElement joinButton = driver.findElement(By.cssSelector("a#actionButtonLink"));
        assertThat(joinButton, is(notNullValue()));
        assertEquals("Rejoindre ce groupe", joinButton.getText());
        //endregion

        //region introducing picture
        WebElement pictureDiv = driver.findElement(By.cssSelector(".bannerLockup > div > div.groupHomeHeader-banner"));
        assertThat(pictureDiv.getAttribute("style"), containsString("background-image:"));
        //endregion

        //endregion


        //region check if banner contains "À propos", "Événements", "Membres", "Photos", "Discussions", "Plus".
        //region JS solution
        //Boolean isBannerComplete = (Boolean) executor.executeScript(
        //        "let list = document.querySelectorAll('main >div:nth-child(4) >div>div>div>div>div ul > li span:not([class])');" +
        //                "let arrayFinal = ['À propos', 'Événements', 'Membres', 'Photos', 'Discussions', 'Plus'];" +
        //                "let array = [];" +
        //                "list.forEach(el =>" +
        //                    "{" +
        //                    "array.push(el.textContent);" +
        //                    "}" +
        //                ");" +
        //                "return array.length === arrayFinal.length && array.every((value, index) => value === arrayFinal[index]);");
        //assertEquals(true, isBannerComplete);
        //endregion

        //region Selenium solution
        List<WebElement> bannerElementList = getElementList("main >div:nth-child(4) >div>div>div>div>div ul > li span:not([class])");

        ArrayList<String> bannerExpectedList = new ArrayList<String>(Arrays.asList("À propos", "Événements", "Membres", "Photos", "Discussions", "Plus"));
        ArrayList<String> bannerFound = new ArrayList<String>();
        for (WebElement element: bannerElementList ) {
            bannerFound.add(element.getText());
        }
        Collections.sort(bannerExpectedList);
        Collections.sort(bannerFound);

        assertEquals(bannerExpectedList, bannerFound);
        //endregion
        //endregion

        //region You should be able to click on past events, upcoming events, and be able to see all the members, and all the photos of a group. (These 4 links available on the page)
        //region click on Évenements
        tryClickElement("a[href*=\"/events/\"]");

        //can go to past events
        tryClickElement("a[href*=\"/events/past/\"]");

        //can go to upcomming event; issue. probably because need time to click on this element
        //tryClickElement("a[href*=\"/events/\"]");

        //endregion

        //can see all members
        tryClickElement("a[href*=\"/members/\"]");

        //can see all photos
        tryClickElement("a[href*=\"/photos/\"]");

        //endregion


        //region if none upcoming meet-up, a card should appear with title : Aucun événement à venir
        //need to find url with a meet-up with empty events (current url not good anymore)

        driver.get("https://www.meetup.com/fr-FR/Paris-Drawing-Meetup/events/");
        //WebElement emptyBanner = driver.findElement(By.cssSelector("div.emptyEventCard"));
        //assertThat(emptyBanner, is(notNullValue()));
        //assertThat(emptyBanner.getText(), containsString("Aucun événement à venir"));

        //endregion

        //region join group, signin or signup
        //click on join group
        tryClickElement(selectorJoinButton);

        //region check signup modal exist (not null)
        String signupModalSelector = ".signUpModal.modal[role=dialog]";
        WebElement signupModal = driver.findElement(By.cssSelector(signupModalSelector));
        assertThat(signupModal, is(notNullValue()));
        //endregion

        //region check signup with facebook
        WebElement signupModalFacebook = driver.findElement(By.cssSelector(signupModalSelector + " a[href*=\"www.facebook.com\"]"));
        assertThat(signupModal, is(notNullValue()));
        //endregion

        //region check signup with google
        WebElement signupModalGoogle = driver.findElement(By.cssSelector(signupModalSelector + " a[href*=\"accounts.google.com\"]"));
        assertThat(signupModalGoogle, is(notNullValue()));
        //endregion

        //region check signup with site id
        WebElement signupModalSite = driver.findElement(By.cssSelector(signupModalSelector + " a[href*=\"www.meetup.com\"][href*=\"login\"]"));
        assertThat(signupModalSite, is(notNullValue()));
        //endregion

        //region register
        WebElement signupModalSignin = driver.findElement(By.cssSelector(signupModalSelector + " a[href*=\"www.meetup.com\"][href*=\"/register/?method=email\"]"));
        assertThat(signupModalSignin, is(notNullValue()));
        //endregion

        //endregion

        //region contact organizer
        //go to our first test url (because previous navigation)
        driver.get("https://www.meetup.com/fr-FR/promenades-et-randonnees/");
        //region check contact organizer
        String contactSelector = "a.orgInfo-message[href*=\"/messages/\"]";
        WebElement contactButton = driver.findElement(By.cssSelector(contactSelector));
        assertThat(contactButton, is(notNullValue()));

        tryClickElement(contactSelector);
        assertThat(driver.getCurrentUrl(), containsString("https://secure.meetup.com/fr-FR/login/"));

        //endregion


        // seems that this click solution doesn't work for Gwen,
        //WebElement contactLink = driver.findElement(By.cssSelector(".orgInfo-message"));
        //assertEquals(contactLink.getText(), "Contacter");
        //contactLink.click();
        //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //assertThat(driver.getCurrentUrl(), containsString("https://secure.meetup.com/fr-FR/login/"));

        //endregion

    }



    // Test de la Story #2-jobs (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testJobs() throws Exception {
        System.out.println("JOBS");
        driver.get("https://www.meetup.com/fr-FR/careers/");

        //region punchline
        String mainPunchlineSelector = "main > div > section > div ";
        assertEquals("Join our team, find your people", driver.findElement(By.cssSelector(mainPunchlineSelector + "> div")).getText());
        //endregion

        //region button link
        WebElement aElementExplore = driver.findElement(By.cssSelector(mainPunchlineSelector + "> div + div + a"));
        assertEquals("Explore Opportunities", aElementExplore.getText());

        //region click on aElement explore with JS (because link 'not clickable')
        tryClickElement(mainPunchlineSelector + " > div + div + a ");

        assertEquals(driver.getCurrentUrl(), "https://www.meetup.com/fr-FR/careers/#open-positions");
        //endregion
        //endregion

        //region perks & benefits
        String perksSelector = "main > div  + div  + div + div > section > div";
        //region perks punchline
        WebElement perksPunch = driver.findElement(By.cssSelector( perksSelector  + "  > div "));
        assertEquals("Perks and benefits", perksPunch.getText());
        //endregion

        //region check if all perks have img & a text for advantage description
        //get ul nb of children
        int perksUlChildsInt = getElementNbChild(perksSelector  + " > ul");

        //loop to check each child
        for (int i=1; i<=perksUlChildsInt; i++){
            Boolean isPerksCorrect = (Boolean) executor.executeScript(
                    "let selector = '" + perksSelector  + " > ul li:nth-child(" + i + ") > div > div > div' ;" +
                    "try{" +
                            "return document.querySelector(selector + ' > img ') !== null && document.querySelector(selector + ' + div ').textContent !==null;" +
                        "}" +
                    "catch(error){return false;}");
            assertEquals(isPerksCorrect, true);
        }
        //endregion
        //endregion

        //region explore section
        //region nb theme
        //get href from elementExplore (getAttribute("href") return complete URL)
        String href = aElementExplore.getAttribute("href").split("#")[1];
        String sectionNbChild = (String) executor.executeScript("return document.querySelector('#" + href + " > div + div +div > ul ').childElementCount.toString();");
        assertEquals("9", sectionNbChild); // supposed to contain 9 child on site
        //endregion

        //region click link more opportunities, go to naw page
        tryClickElement("#" + href + " + section > div > span > a ");

        assertEquals(driver.getCurrentUrl(), "https://www.meetup.com/fr-FR/careers/all-opportunities");
        //endregion
        //endregion

    }


    // Test de la Story #2-pixel_perfect (https://trello.com/c/glufGucb/45-homepage)
    @Test
    public void testPixelPerfect() throws Exception {
        System.out.println("PixelPerfect");
        driver.get("https://www.meetup.com/fr-FR/");
    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

}
