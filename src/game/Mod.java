package game;

import java.awt.*;

public interface Mod {
    void init(Game game);
    void draw(Graphics graphics);
    void update();
}