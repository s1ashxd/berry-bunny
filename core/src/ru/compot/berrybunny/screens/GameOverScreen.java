package ru.compot.berrybunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import ru.compot.berrybunny.BerryBunnyGame;
import ru.compot.berrybunny.utils.TextureUtils;

public class GameOverScreen extends ScreenAdapter {

    private static final BerryBunnyGame BBG = BerryBunnyGame.INSTANCE;

    private static final int BUTTON_TEX_WIDTH = 240,
            BUTTON_TEX_HEIGHT = 70;
    private static final long ANIMATION_DURATION = 500L;

    private final Texture background = TextureUtils.resize("textures/game/game_over_background.png", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    private final Texture resetButton = TextureUtils.resize("textures/buttons/button_restart.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
    private final Texture menuButton = TextureUtils.resize("textures/buttons/button_menu.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);

    private final GameScreen gameScreen;

    private long startAnimation;
    private boolean animated;
    private float alpha;

    public GameOverScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void render(float delta) {
        SpriteBatch sb = BBG.getBatch();

        if (alpha < 1) gameScreen.renderWithoutUpdate();
        sb.begin();
        sb.setColor(1, 1, 1, alpha);

        sb.draw(background, 0, 0);
        sb.draw(resetButton,  Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, Gdx.graphics.getHeight() / 2f - 20);

        sb.draw(menuButton,  Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, Gdx.graphics.getHeight() / 2f - 120);

        sb.setColor(1, 1, 1, 1);
        sb.end();

        if (System.currentTimeMillis() - startAnimation < ANIMATION_DURATION)
            alpha = (float) ((System.currentTimeMillis() - startAnimation) / (double) ANIMATION_DURATION);
        else if (!animated) {
            animated = true;
            alpha = 1;
            gameScreen.endGame();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            BBG.setScreen(new GameScreen(gameScreen.getDifficulty()));
            BBG.getBackgroundMusic().play();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            BBG.setScreen(MainMenuScreen.INSTANCE);
            BBG.getBackgroundMusic().play();
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
                BBG.setScreen(MainMenuScreen.INSTANCE);
                BBG.getBackgroundMusic().play();
                return;
            }
            Rectangle continueRect = new Rectangle(
                    Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f,
                    Gdx.graphics.getHeight() / 2f - 20,
                    BUTTON_TEX_WIDTH,
                    BUTTON_TEX_HEIGHT
            );
            if (continueRect.contains(touchPosition.x, touchPosition.y)) {
                BBG.setScreen(new GameScreen(gameScreen.getDifficulty()));
                BBG.getBackgroundMusic().play();
            }
        }
    }

    @Override
    public void show() {
        startAnimation = System.currentTimeMillis();
        BBG.getBackgroundMusic().stop();
    }
}
