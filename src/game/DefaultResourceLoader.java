package game;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultResourceLoader implements ResourceLoader {
    @Override
    public BufferedImage loadImage(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(this.getClass().getResource(path)));
    }

    @Override
    public List<String> loadTextFile(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(path))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }


    @Override
    public Clip loadSound(String path) throws IOException  {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(path)));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception e) {
            throw new IOException("Failed to load sound: " + path, e);
        }
    }
}
