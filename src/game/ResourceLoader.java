package game;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface ResourceLoader {
    BufferedImage loadImage(String path) throws IOException;
    List<String> loadTextFile(String path) throws IOException;
    Clip loadSound(String path) throws IOException;
}
