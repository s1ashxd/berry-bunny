package ru.compot.berrybunny;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.compot.berrybunny.data.GameData;
import ru.compot.berrybunny.screens.GameScreen;
import ru.compot.berrybunny.screens.MainMenuScreen;
import ru.compot.berrybunny.utils.TextureUtils;

public class BerryBunnyGame extends Game {

    public static final String GAME_TITLE = "Berry Bunny";
    public static final BerryBunnyGame INSTANCE = new BerryBunnyGame();

    private SpriteBatch batch;
    private BitmapFont headerFont;
    private BitmapFont font;
    private Texture background;
    private Music backgroundMusic;

    private OrthographicCamera camera;

    private BerryBunnyGame() {
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = TextureUtils.resize("textures/background.png", Gdx.graphics.getWidth(), -1);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background_music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.7f);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/fitfaks.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter headParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        headParam.size = 90;
        headerFont = generator.generateFont(headParam);
        FreeTypeFontGenerator.FreeTypeFontParameter plainParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        plainParam.size = 25;
        font = generator.generateFont(plainParam);

        GameData.INSTANCE.load();
        setScreen(MainMenuScreen.INSTANCE);
        backgroundMusic.play();
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getHeaderFont() {
        return headerFont;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Texture getBackground() {
        return background;
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

}
