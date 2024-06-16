package game;

import game.ui.Menu;

import java.awt.*;

public interface Mod {
    void init();
    void draw(Graphics graphics);
    void update();
    Menu[] getMenus();
}