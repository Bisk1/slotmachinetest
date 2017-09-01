package org.daniel.slotmachine;

import org.openqa.selenium.By;

public class Selectors {
    public static final By BLANK_WIN_SCORE = By.cssSelector("#trPrize_45 > span");
    public static final By FRUIT_WIN_SCORE = By.cssSelector("#trPrize_41 > span");

    public static final By LAST_WIN = By.id("lastWin");
    public static final By CREDITS = By.id("credits");
    public static final By BET_BUTTON = By.id("bet");

    public static final By BET_UP_BUTTON = By.id("betSpinUp");
    public static final By BET_DOWN_BUTTON = By.id("betSpinDown");
    public static final By SPIN_BUTTON = By.id("spinButton");

    public static final By BACKGROUND1 = By.id("changeable_background_1");
    public static final By BACKGROUND2 = By.id("changeable_background_2");

    public static final By SLOTS_SELECTOR = By.id("slotsSelectorWrapper");

    public static final By CHANGE_BG_BUTTON = By.className("btnChangeBackground");
    public static final By CHANGE_REEL_ICONS_BUTTON = By.className("btnChangeReels");
    public static final By CHANGE_MACHINE_BUTTON = By.className("btnChangeMachine");
}
