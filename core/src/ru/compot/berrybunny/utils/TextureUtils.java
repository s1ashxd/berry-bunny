package ru.compot.berrybunny.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class TextureUtils {

    private TextureUtils() {
    }

    public static Texture resize(String path, int width, int height) {
        Pixmap pixmapInput = new Pixmap(Gdx.files.internal(path));
        if ((pixmapInput.getWidth() == width && pixmapInput.getHeight() == height)
                || (width == -1 && height == -1)) {
            pixmapInput.dispose();
            return new Texture(path);
        }
        int outputWidth = width == -1 ? pixmapInput.getWidth() : width;
        int outputHeight = height == -1 ? pixmapInput.getHeight() : height;
        Pixmap pixmapOutput = new Pixmap(outputWidth, outputHeight, pixmapInput.getFormat());
        pixmapOutput.drawPixmap(pixmapInput,
                0, 0, pixmapInput.getWidth(), pixmapInput.getHeight(),
                0, 0, outputWidth, outputHeight
        );
        Texture texture = new Texture(pixmapOutput);
        pixmapInput.dispose();
        pixmapOutput.dispose();
        return texture;
    }
}
