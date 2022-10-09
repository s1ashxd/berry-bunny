package ru.compot.berrybunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import ru.compot.berrybunny.BerryBunnyGame;
import ru.compot.berrybunny.data.GameData;
import ru.compot.berrybunny.data.enums.Difficulty;
import ru.compot.berrybunny.data.spawn.Petal;
import ru.compot.berrybunny.utils.TextureUtils;

public class MainMenuScreen extends ScreenAdapter {

    private static final BerryBunnyGame BBG = BerryBunnyGame.INSTANCE;
    private static final GlyphLayout HEADER_LAYOUT = new GlyphLayout(BBG.getHeaderFont(), BerryBunnyGame.GAME_TITLE);
    public static final MainMenuScreen INSTANCE = new MainMenuScreen();

    private static final int PETAL1_TEX_WIDTH = 40,
            PETAL1_TEX_HEIGHT = 40;
    private static final int PETAL2_TEX_WIDTH = 50,
            PETAL2_TEX_HEIGHT = 50;
    private static final int PETAL3_TEX_WIDTH = 20,
            PETAL3_TEX_HEIGHT = 20;
    private static final int PETAL4_TEX_WIDTH = 30,
            PETAL4_TEX_HEIGHT = 30;

    private static final int BUTTON_TEX_WIDTH = 240,
            BUTTON_TEX_HEIGHT = 50;

    private static final int PETAL_VERT_FALL_SPEED = 400,
            PETAL_HOR_FALL_SPEED = 100;
    private static final long PETAL_SPAWN_SPEED = 100L;

    private final Texture petal1 = TextureUtils.resize("textures/menu/petals/petal1.png", PETAL1_TEX_WIDTH, PETAL1_TEX_HEIGHT);
    private final Texture petal2 = TextureUtils.resize("textures/menu/petals/petal2.png", PETAL2_TEX_WIDTH, PETAL2_TEX_HEIGHT);
    private final Texture petal3 = TextureUtils.resize("textures/menu/petals/petal3.png", PETAL3_TEX_WIDTH, PETAL3_TEX_HEIGHT);
    private final Texture petal4 = TextureUtils.resize("textures/menu/petals/petal4.png", PETAL4_TEX_WIDTH, PETAL4_TEX_HEIGHT);

    private final Texture easy = TextureUtils.resize("textures/menu/buttons/button_easy.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
    private final Texture normal = TextureUtils.resize("textures/menu/buttons/button_normal.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
    private final Texture difficult = TextureUtils.resize("textures/menu/buttons/button_difficult.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
    private final Texture hard = TextureUtils.resize("textures/menu/buttons/button_hard.png", BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);

    private final Texture headerBackground = TextureUtils.resize("textures/header_background.png", (int) (HEADER_LAYOUT.width + 70), (int) (HEADER_LAYOUT.height + 70));

    private final Array<Petal> petals = new Array<>();

    private int nextPetal;
    private long lastSpawnTime;

    private MainMenuScreen() {
    }

    @Override
    public void render(float delta) {
        GameData gd = GameData.INSTANCE;
        SpriteBatch sb = BBG.getBatch();
        BitmapFont font = BBG.getFont();
        BitmapFont headerFont = BBG.getHeaderFont();

        sb.begin();

        // background
        sb.draw(BBG.getBackground(), 0, -Gdx.graphics.getHeight() / 2f);

        // petals
        petals.forEach(petal -> sb.draw(petal.getTexture(), petal.getRectangle().x, petal.getRectangle().y));

        // header (logo)
        sb.draw(headerBackground, Gdx.graphics.getWidth() / 2f - headerBackground.getWidth() / 2f, Gdx.graphics.getHeight() - 100 - headerBackground.getHeight() / 2f);
        headerFont.draw(sb,
                BerryBunnyGame.GAME_TITLE,
                Gdx.graphics.getWidth() / 2f - HEADER_LAYOUT.width / 2f,
                Gdx.graphics.getHeight() - 100);

        // buttons
        sb.draw(easy, Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 260);
        sb.draw(normal, Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 190);
        sb.draw(difficult, Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 120);
        sb.draw(hard, Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 50);

        // records
        font.setColor(Color.PINK);
        font.draw(sb, "Easy record: " + gd.getRecord(Difficulty.EASY) + " pts", 30, 140);
        font.draw(sb, "Norm record: " + gd.getRecord(Difficulty.NORMAL) + " pts", 30, 115);
        font.draw(sb, "Diff record: " + gd.getRecord(Difficulty.DIFFICULT) + " pts", 30, 90);
        font.draw(sb, "Hard record: " + gd.getRecord(Difficulty.HARD) + " pts", 30, 65);

        sb.end();

        if (System.currentTimeMillis() - lastSpawnTime > PETAL_SPAWN_SPEED) {
            spawnPetals();
            lastSpawnTime = System.currentTimeMillis();
        }

        if (Gdx.input.isButtonJustPressed(0)) {
            Vector3 touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            BBG.getCamera().unproject(touchPosition);
            Rectangle easyRect = new Rectangle(Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 260, BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
            if (easyRect.contains(touchPosition.x, touchPosition.y)) {
                 BBG.setScreen(new GameScreen(Difficulty.EASY));
                 petals.clear();
                 return;
            }
            Rectangle normalRect = new Rectangle(Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 190, BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
            if (normalRect.contains(touchPosition.x, touchPosition.y)) {
                BBG.setScreen(new GameScreen(Difficulty.NORMAL));
                petals.clear();
                return;
            }
            Rectangle difficultRect = new Rectangle(Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 120, BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
            if (difficultRect.contains(touchPosition.x, touchPosition.y)) {
                BBG.setScreen(new GameScreen(Difficulty.DIFFICULT));
                petals.clear();
                return;
            }
            Rectangle hardRect = new Rectangle(Gdx.graphics.getWidth() / 2f - BUTTON_TEX_WIDTH / 2f, 50, BUTTON_TEX_WIDTH, BUTTON_TEX_HEIGHT);
            if (hardRect.contains(touchPosition.x, touchPosition.y)) {
                BBG.setScreen(new GameScreen(Difficulty.HARD));
                petals.clear();
                return;
            }
        }

        petals.forEach(petal -> {
            Rectangle rect = petal.getRectangle();
            rect.x -= PETAL_HOR_FALL_SPEED * delta;
            rect.y -= PETAL_VERT_FALL_SPEED * delta;
            if (rect.y + rect.width < 0) petals.removeValue(petal, true);
        });
    }

    @Override
    public void show() {
        spawnPetals();
    }

    private void spawnPetals() {
        Texture petalTexture;
        int petalWidth, petalHeight;
        switch (nextPetal) {
            case 0:
                petalTexture = petal1;
                petalWidth = PETAL1_TEX_WIDTH;
                petalHeight = PETAL1_TEX_HEIGHT;
                break;
            case 1:
                petalTexture = petal2;
                petalWidth = PETAL2_TEX_WIDTH;
                petalHeight = PETAL2_TEX_HEIGHT;
                break;
            case 2:
                petalTexture = petal3;
                petalWidth = PETAL3_TEX_WIDTH;
                petalHeight = PETAL3_TEX_HEIGHT;
                break;
            default:
                petalTexture = petal4;
                petalWidth = PETAL4_TEX_WIDTH;
                petalHeight = PETAL4_TEX_HEIGHT;
                break;
        }
        petals.add(new Petal(new Rectangle(
                MathUtils.random(0, Gdx.graphics.getWidth() + petalWidth * 2),
                Gdx.graphics.getWidth() + petalHeight,
                petalWidth,
                petalHeight
        ), petalTexture));
        nextPetal = (nextPetal + 1) % 4;
        lastSpawnTime = System.currentTimeMillis();
    }
}
