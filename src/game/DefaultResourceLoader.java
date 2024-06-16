package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(this.getClass().getResource(path)));
    }
}
