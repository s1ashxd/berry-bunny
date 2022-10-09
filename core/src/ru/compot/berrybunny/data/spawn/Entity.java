package ru.compot.berrybunny.data.spawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.compot.berrybunny.data.enums.EntityType;

public class Entity {

    private final EntityType entityType;
    private final Vector2 vector;
    private final Texture texture;

    public Entity(EntityType entityType, Vector2 vector, Texture texture) {
        this.entityType = entityType;
        this.vector = vector;
        this.texture = texture;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Vector2 getVector() {
        return vector;
    }

    public Texture getTexture() {
        return texture;
    }
}
