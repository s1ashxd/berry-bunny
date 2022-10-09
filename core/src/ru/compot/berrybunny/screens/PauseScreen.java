package ru.compot.berrybunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import ru.compot.berrybunny.BerryBunnyGame;
import ru.compot.berrybunny.utils.TextureUtils;

public class PauseScreen extends ScreenAdapter {

    private static final BerryBunnyGame BBG = BerryBunnyGame.INSTANCE;
    private static final String HEADER_NAME = "Pause";
    private static final GlyphLayout HEADER_LAYOUT = new GlyphLayout(BBG.getHeaderFont(), HEADER_NAME);

    private static final int BUTTON_TEX_WIDTH = 240,
            BUTTON_TEX_HEIGHT = 70;

    private final Texture backgroundMask = TextureUtils.resize("textures/game/pause_background_mask.png", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private final Texture headerBackground = TextureUtils.resize("textures/header_background.png", -1, (int) (HEADER_LAYOUT.height + 70));

    private final Texture continueButton = TextureUtils.resize("textures/buttons/button_continue.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
    private final Texture toMenuButton = TextureUtils.resize("textures/buttons/button_menu.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);

    private final GameScreen gameScreen;

    private long pauseStart;

    public PauseScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void render(float delta) {
        SpriteBatch sb = BBG.getBatch();
        BitmapFont headerFont = BBG.getHeaderFont();
        gameScreen.renderWithoutUpdate();
        sb.begin();

        // background mask
        sb.draw(backgroundMask, 0, 0);

        // header
        sb.draw(headerBackground, Gdx.graphics.getWidth() / 2f - headerBackground.getWidth() / 2f, Gdx.graphics.getHeight() - 100 - headerBackground.getHeight() / 2f);
        headerFont.setColor(Color.WHITE);
        headerFont.draw(sb,
                HEADER_NAME,
                Gdx.graphics.getWidth() / 2f - HEADER_LAYOUT.width / 2f,
                Gdx.graphics.getHeight() - 100);

        // buttons
        sb.draw(continueButton, Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, Gdx.graphics.getHeight() / 2f - 20);
        sb.draw(toMenuButton, Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, Gdx.graphics.getHeight() / 2f - 120);

        sb.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameScreen.increaseTimers(System.currentTimeMillis() - pauseStart);
            BBG.setScreen(gameScreen);
            return;
        }

        if (Gdx.input.isButtonJustPressed(0)) {
            Vector3 touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            BBG.getCamera().unproject(touchPosition);
            Rectangle menuRect = new Rectangle(
                    Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f,
                    Gdx.graphics.getHeight() / 2f - 120,
                    BUTTON_TEX_WIDTH,
                    BUTTON_TEX_HEIGHT
            );
            if (menuRect.contains(touchPosition.x, touchPosition.y)) {
                gameScreen.endGame();
                BBG.setScreen(MainMenuScreen.INSTANCE);
                return;
            }
            Rectangle continueRect = new Rectangle(
                    Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f,
                    Gdx.graphics.getHeight() / 2f - 20,
                    BUTTON_TEX_WIDTH,
                    BUTTON_TEX_HEIGHT
            );
            if (continueRect.contains(touchPosition.x, touchPosition.y)) {
                BBG.setScreen(gameScreen);
                gameScreen.increaseTimers(System.currentTimeMillis() - pauseStart);
            }
        }
    }

    @Override
    public void show() {
        pauseStart = System.currentTimeMillis();
    }
}
