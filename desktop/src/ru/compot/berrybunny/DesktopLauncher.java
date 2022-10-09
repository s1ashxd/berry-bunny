package ru.compot.berrybunny;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import ru.compot.berrybunny.BerryBunnyGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

	private static final int SCREEN_WIDTH = 840, SCREEN_HEIGHT = 500;

	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle(BerryBunnyGame.GAME_TITLE);
		config.setWindowIcon("icon.png");
		config.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
		config.setWindowSizeLimits(SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
		new Lwjgl3Application(BerryBunnyGame.INSTANCE, config);
	}
}
