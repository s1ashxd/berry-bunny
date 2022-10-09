package ru.compot.berrybunny.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import ru.compot.berrybunny.data.enums.Difficulty;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameData {

    public static final GameData INSTANCE = new GameData();

    private final Map<Difficulty, Integer> records = new HashMap<>();

    private GameData() {
    }

    public int getRecord(Difficulty difficulty) {
        return records.getOrDefault(difficulty, 0);
    }

    public void setRecord(Difficulty difficulty, int record) {
        records.put(difficulty, record);
        save();
    }

    public void load() {
        Preferences preferences = Gdx.app.getPreferences("bunny");
        preferences.get().forEach((k, v) -> {
            Difficulty diff = Difficulty.valueOf(k.toUpperCase(Locale.ROOT));
            try {
                records.put(diff, Integer.parseInt((String) v));
            } catch (NumberFormatException ignored) {
            }
        });
    }

    public void save() {
        Preferences preferences = Gdx.app.getPreferences("bunny");
        records.forEach((d, r) -> preferences.putInteger(d.name().toLowerCase(Locale.ROOT), r));
        preferences.flush();
    }

}
