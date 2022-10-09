package ru.compot.berrybunny.data.enums;

import ru.compot.berrybunny.data.GameData;

import java.util.function.Consumer;
import java.util.function.Supplier;

public enum Difficulty {

    EASY(0.5f, 3),
    NORMAL(1f, 3),
    DIFFICULT(1.5f, 3),
    HARD(2f, 1);

    private final float fallSpeedMultiplier;
    private final int defaultLives;

    Difficulty(float fallSpeedMultiplier, int defaultLives) {
        this.fallSpeedMultiplier = fallSpeedMultiplier;
        this.defaultLives = defaultLives;
    }

    public float getFallSpeedMultiplier() {
        return fallSpeedMultiplier;
    }

    public int getDefaultLives() {
        return defaultLives;
    }

    public void checkAndSetRecord(int record) {
       GameData gd = GameData.INSTANCE;
       if (gd.getRecord(this) < record)
           gd.setRecord(this, record);
    }
}
