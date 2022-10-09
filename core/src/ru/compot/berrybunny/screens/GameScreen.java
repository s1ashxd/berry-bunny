package ru.compot.berrybunny.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import ru.compot.berrybunny.BerryBunnyGame;
import ru.compot.berrybunny.data.enums.EntityType;
import ru.compot.berrybunny.data.enums.Difficulty;
import ru.compot.berrybunny.data.spawn.Entity;
import ru.compot.berrybunny.utils.TextureUtils;

public class GameScreen extends ScreenAdapter {

    private static final BerryBunnyGame BBG = BerryBunnyGame.INSTANCE;

    // textures size
    private static final int CLEAN_BUNNY_TEX_WIDTH = 99,
            CLEAN_BUNNY_TEX_HEIGHT = 90;
    private static final int BLUEBERRY_BUNNY_TEX_WIDTH = 99,
            BLUEBERRY_BUNNY_TEX_HEIGHT = 90;
    private static final int STRAWBERRY_BUNNY_TEX_WIDTH = 99,
            STRAWBERRY_BUNNY_TEX_HEIGHT = 90;
    private static final int BLUEBERRY_TEX_WIDTH = 43,
            BLUEBERRY_TEX_HEIGHT = 41;
    private static final int STRAWBERRY_TEX_WIDTH = 60,
            STRAWBERRY_TEX_HEIGHT = 56;
    private static final int CARROT_TEX_WIDTH = 60,
            CARROT_TEX_HEIGHT = 65;
    private static final int PEPPER_TEX_WIDTH = 60,
            PEPPER_TEX_HEIGHT = 62;
    private static final int HOT_BAR_TEX_HEIGHT = 70;
    private static final int HEART_TEX_WIDTH = 37,
            HEART_TEX_HEIGHT = 33;

    // hit boxes size
    private static final int BUNNY_HIT_BOX_WIDTH = 99;
    private static final int ENTITY_HIT_BOX_WIDTH = 64,
            ENTITY_HIT_BOX_HEIGHT = 64;

    // speeds and spawn rate
    private static final int ENTITY_FALL_SPEED = 400;
    private static final int BUNNY_MOUSE_SPEED = 400;
    private static final int BUNNY_ARROWS_SPEED = 400;
    private static final long ENTITY_SPAWN_SPEED = 200L;

    // hot bar textures
    private final Texture hotBar = TextureUtils.resize("textures/game/hot_bar.png", Gdx.graphics.getWidth(), HOT_BAR_TEX_HEIGHT);
    private final Texture heart = TextureUtils.resize("textures/game/heart.png", HEART_TEX_WIDTH, HEART_TEX_HEIGHT);

    // entity textures
    private final Texture blueberry = TextureUtils.resize("textures/game/entities/blueberry.png", BLUEBERRY_TEX_WIDTH, BLUEBERRY_TEX_HEIGHT);
    private final Texture strawberry = TextureUtils.resize("textures/game/entities/strawberry.png", STRAWBERRY_TEX_WIDTH, STRAWBERRY_TEX_HEIGHT);
    private final Texture carrot = TextureUtils.resize("textures/game/entities/carrot.png", CARROT_TEX_WIDTH, CARROT_TEX_HEIGHT);
    private final Texture pepper = TextureUtils.resize("textures/game/entities/pepper.png", PEPPER_TEX_WIDTH, PEPPER_TEX_HEIGHT);

    // bunny textures
    private final Texture clearBunny = TextureUtils.resize("textures/game/bunnies/clean_bunny.png", CLEAN_BUNNY_TEX_WIDTH, CLEAN_BUNNY_TEX_HEIGHT);
    private final Texture blueberryBunny = TextureUtils.resize("textures/game/bunnies/blueberry_bunny.png", BLUEBERRY_BUNNY_TEX_WIDTH, BLUEBERRY_BUNNY_TEX_HEIGHT);
    private final Texture strawberryBunny = TextureUtils.resize("textures/game/bunnies/strawberry_bunny.png", STRAWBERRY_BUNNY_TEX_WIDTH, STRAWBERRY_BUNNY_TEX_HEIGHT);

    private Sound eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat_sound.mp3"));

    // current game state
    private final Vector2 bunnyPosition = new Vector2(Gdx.graphics.getWidth() / 2f + BUNNY_HIT_BOX_WIDTH / 2f, 0);
    private final Array<Entity> entities = new Array<>();

    private final Difficulty difficulty;

    private EntityType mainBerry = EntityType.NONE;
    private int lives;
    private int points;

    // entity spawn data
    private long lastSpawnTime;

    public GameScreen(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.lives = difficulty.getDefaultLives();
    }

    @Override
    public void render(float delta) {
        renderWithoutUpdate();

        if (Gdx.input.isTouched()) {
            Vector3 touchPosition = new Vector3();
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            BBG.getCamera().unproject(touchPosition);
            float diff = (touchPosition.x - BUNNY_HIT_BOX_WIDTH / 2f) - bunnyPosition.x;
            float absDiff = Math.abs(diff);
            if (absDiff < 5f)
                bunnyPosition.x += diff;
            else {
                float step = BUNNY_MOUSE_SPEED * delta * (diff < 0 ? -1f : 1f);
                bunnyPosition.x += Math.min(absDiff, step);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bunnyPosition.x -= BUNNY_ARROWS_SPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bunnyPosition.x += BUNNY_ARROWS_SPEED * delta;

        if (bunnyPosition.x < 0)
            bunnyPosition.x = 0;
        int newX = Gdx.graphics.getWidth() - BUNNY_HIT_BOX_WIDTH;
        if (bunnyPosition.x > newX)
            bunnyPosition.x = newX;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            BBG.setScreen(new PauseScreen(this));
            return;
        }

        if (System.currentTimeMillis() - lastSpawnTime > ENTITY_SPAWN_SPEED) {
            spawnEntity();
            lastSpawnTime = System.currentTimeMillis();
        }

        entities.forEach(entity -> {
            Vector2 vec2 = entity.getVector();
            vec2.y -= ENTITY_FALL_SPEED * difficulty.getFallSpeedMultiplier() * delta;
            if (vec2.y + ENTITY_HIT_BOX_HEIGHT < 0) {
                entities.removeValue(entity, true);
                return;
            }
            if (vec2.x < bunnyPosition.x + BUNNY_HIT_BOX_WIDTH
                    && vec2.x + ENTITY_HIT_BOX_WIDTH > bunnyPosition.x
                    && vec2.y < bunnyPosition.y + ENTITY_HIT_BOX_HEIGHT) {
                if (mainBerry == EntityType.NONE &&
                        (entity.getEntityType() != EntityType.CARROT && entity.getEntityType() != EntityType.PEPPER)) {
                    mainBerry = entity.getEntityType();
                }

                if (entity.getEntityType() == mainBerry) points += 2;
                else switch (entity.getEntityType()) {
                    case CARROT:
                        points += 5;
                        break;
                    case PEPPER:
                        lives--;
                        if (lives == 0)
                            BBG.setScreen(new GameOverScreen(this));
                        break;
                    default:
                        points++;
                        break;
                }
                eatSound.play();
                entities.removeValue(entity, true);
            }
        });
    }

    @Override
    public void show() {
        spawnEntity();
    }

    public void renderWithoutUpdate() {
        SpriteBatch sb = BBG.getBatch();
        BitmapFont font = BBG.getFont();

        sb.begin();

        // background
        sb.draw(BBG.getBackground(), 0, -Gdx.graphics.getHeight() / 2f);

        // entities
        entities.forEach(entity -> sb.draw(entity.getTexture(), entity.getVector().x, entity.getVector().y));

        // hot bar
        sb.draw(hotBar, 0, Gdx.graphics.getHeight() - HOT_BAR_TEX_HEIGHT);
        // score
        font.setColor(Color.PINK);
        font.draw(sb, "Score: " + points + " pts", 25, Gdx.graphics.getHeight() - (HOT_BAR_TEX_HEIGHT / 2f) + (font.getXHeight() / 2f));
        // main berry
        Texture berryToDraw = null;
        int berryTexWidth = 0, berryTexHeight = 0;
        if (mainBerry == EntityType.BLUEBERRY) {
            berryToDraw = blueberry;
            berryTexWidth = BLUEBERRY_TEX_WIDTH;
            berryTexHeight = BLUEBERRY_TEX_HEIGHT;
        } else if (mainBerry == EntityType.STRAWBERRY) {
            berryToDraw = strawberry;
            berryTexWidth = STRAWBERRY_TEX_WIDTH;
            berryTexHeight = STRAWBERRY_TEX_HEIGHT;
        }
        if (berryToDraw != null) {
            sb.draw(
                    berryToDraw,
                    Gdx.graphics.getWidth() / 2f - berryTexWidth / 2f,
                    Gdx.graphics.getHeight() - HOT_BAR_TEX_HEIGHT / 2f - berryTexHeight / 2f
            );
        }
        // hearts (lives)
        int nextHeart = Gdx.graphics.getWidth() - (HEART_TEX_WIDTH + 5) - 25;
        for (int i = 0; i < lives; i++) {
            sb.draw(heart, nextHeart, Gdx.graphics.getHeight() - HOT_BAR_TEX_HEIGHT / 2f - HEART_TEX_HEIGHT / 2f);
            nextHeart -= HEART_TEX_WIDTH + 5;
        }

        // bunny
        Texture bunnyToDraw = clearBunny;
        if (mainBerry == EntityType.BLUEBERRY) bunnyToDraw = blueberryBunny;
        else if (mainBerry == EntityType.STRAWBERRY) bunnyToDraw = strawberryBunny;
        sb.draw(bunnyToDraw, bunnyPosition.x, bunnyPosition.y);

        sb.end();
    }

    public void increaseTimers(long delta) {
        lastSpawnTime += delta;
    }

    public void endGame() {
        difficulty.checkAndSetRecord(points);
        mainBerry = EntityType.NONE;
        entities.clear();
        points = 0;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    private void spawnEntity() {
        Vector2 entityPosition = new Vector2(
                MathUtils.random(0, Gdx.graphics.getWidth() - ENTITY_HIT_BOX_WIDTH),
                Gdx.graphics.getHeight()
        );
        EntityType entityType = EntityType.PEPPER;
        Texture entityTexture = pepper;
        int chance = MathUtils.random(0, 100);
        if (chance < 10) {
            entityTexture = carrot;
            entityType = EntityType.CARROT;
        } else if (chance < 40) {
            entityTexture = strawberry;
            entityType = EntityType.STRAWBERRY;
        } else if (chance < 70) {
            entityTexture = blueberry;
            entityType = EntityType.BLUEBERRY;
        }
        entities.add(new Entity(entityType, entityPosition, entityTexture));
        lastSpawnTime = System.currentTimeMillis();
    }
}
