package game;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ResourceLoader {
    BufferedImage loadImage(String path) throws IOException;
}
