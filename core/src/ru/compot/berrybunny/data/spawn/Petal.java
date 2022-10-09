package ru.compot.berrybunny.data.spawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Petal {

    private final Rectangle vector;
    private final Texture texture;

    public Petal(Rectangle vector, Texture texture) {
        this.vector = vector;
        this.texture = texture;
    }

    public Rectangle getRectangle() {
        return vector;
    }

    public Texture getTexture() {
        return texture;
    }
}
