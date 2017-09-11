package org.daniel.slotmachine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.daniel.slotmachine.Selectors.*;
import static org.junit.Assert.*;

public class SlotMachineTest {

    private final static String URL = "http://slotmachinescript.com/";

    private WebDriver driver;

    @Before
    public void setUp() {
        driver = getWebdriver();
        driver.manage().window().maximize();
        driver.get(URL);
    }

    private WebDriver getWebdriver() {
        String browserStr = System.getProperty("browser");
        switch (browserStr) {
            case "firefox": return new FirefoxDriver();
            case "chrome": return new ChromeDriver();
            default: throw new RuntimeException("Unsupported browser: " + browserStr);
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void canSpin() {
        driver.findElement(SPIN_BUTTON).click();
        assertTrue(driver.findElement(SPIN_BUTTON).getAttribute("class").contains("disabled"));
    }

    @Test
    public void betUpButtonIncreasesBet() {
        driver.findElement(BET_UP_BUTTON).click();
        String betStr = driver.findElement(BET_BUTTON).getText();
        int bet = Integer.valueOf(betStr);
        assertEquals(2, bet);
    }

    @Test
    public void betDownButtonDecreasesBet() {
        driver.findElement(BET_UP_BUTTON).click();
        driver.findElement(BET_DOWN_BUTTON).click();
        String betStr = driver.findElement(BET_BUTTON).getText();
        int bet = Integer.valueOf(betStr);
        assertEquals(1, bet);
    }

    @Test
    public void betDownButtonCannotDecreaseBelowOne() {
        driver.findElement(BET_DOWN_BUTTON).click();
        driver.findElement(BET_DOWN_BUTTON).click();
        driver.findElement(BET_DOWN_BUTTON).click();
        String betStr = driver.findElement(BET_BUTTON).getText();
        int bet = Integer.valueOf(betStr);
        assertEquals(1, bet);
    }


    @Test
    public void betChangeUpdatesWinChart() {
        int blankWin = Integer.valueOf(driver.findElement(BLANK_WIN_SCORE).getText());
        assertEquals(4, blankWin);

        int fruitWin = Integer.valueOf(driver.findElement(FRUIT_WIN_SCORE).getText());
        assertEquals(7, fruitWin);

        driver.findElement(BET_UP_BUTTON).click();


        blankWin = Integer.valueOf(driver.findElement(BLANK_WIN_SCORE).getText());
        assertEquals(8, blankWin);

        fruitWin = Integer.valueOf(driver.findElement(FRUIT_WIN_SCORE).getText());
        assertEquals(14, fruitWin);
    }


    @Test
    public void spinUpdatesLastWinAndTotalSpins() {
        int creditsBeforeSpin = 0;
        String lastWinStr = ""; // spin until win to finish this test
        while (lastWinStr.equals("")) {
            creditsBeforeSpin  = Integer.valueOf(driver.findElement(CREDITS).getText());
            driver.findElement(SPIN_BUTTON).click();
            // wait until spin button is active again - means animation has finished
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    WebElement spinButton = driver.findElement(SPIN_BUTTON);
                    return !spinButton.getAttribute("class").equals("disabled");
                }
            });

            lastWinStr = driver.findElement(LAST_WIN).getText();
        }
        int lastWinAfterSpin  = Integer.valueOf(lastWinStr);
        int creditsAfterSpin  = Integer.valueOf(driver.findElement(CREDITS).getText());
        assertEquals(lastWinAfterSpin, creditsAfterSpin - creditsBeforeSpin + 1);
        // plus one because the winning spin costed 1 credit but wasn't subtracted yet when number was parsed
    }

    @Test
    public void backgroundChangesWhenButtonPressed() {
        // background 1 is visible, background 2 is hidden
        assertTrue(driver.findElement(BACKGROUND1).isDisplayed());
        assertFalse(driver.findElement(BACKGROUND2).isDisplayed());

        driver.findElement(CHANGE_BG_BUTTON).click();
        new WebDriverWait(driver, 10).until(
                ExpectedConditions.and(
                        ExpectedConditions.visibilityOfElementLocated(BACKGROUND2),
                        ExpectedConditions.invisibilityOfElementLocated(BACKGROUND1)));
        // background 1 is hidden, background 2 is visible
        assertFalse(driver.findElement(BACKGROUND1).isDisplayed());
        assertTrue(driver.findElement(BACKGROUND2).isDisplayed());
    }

    @Test
    public void reelIconsChangeWhenButtonPressed() {
        assertTrue(driver.findElement(SLOTS_SELECTOR).getAttribute("class").contains("reelSet1"));
        driver.findElement(CHANGE_REEL_ICONS_BUTTON).click();
        new WebDriverWait(driver, 10).until(hasCssClassCondition("reelSet2"));
        assertTrue(driver.findElement(SLOTS_SELECTOR).getAttribute("class").contains("reelSet2"));
    }

    @Test
    public void machineChangeWhenButtonPressed() {
        assertTrue(driver.findElement(SLOTS_SELECTOR).getAttribute("class").contains("slotMachine1"));
        driver.findElement(CHANGE_MACHINE_BUTTON).click();
        new WebDriverWait(driver, 10).until(hasCssClassCondition("slotMachine2"));
        assertTrue(driver.findElement(SLOTS_SELECTOR).getAttribute("class").contains("slotMachine2"));
    }

    private ExpectedCondition<Boolean> hasCssClassCondition(final String className) {
        return new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(SLOTS_SELECTOR).getAttribute("class").contains(className);
            }
        };
    }
}