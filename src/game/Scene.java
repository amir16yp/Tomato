package game;

import game.entities.Entity;
import game.entities.PlayerEntity;
import game.ui.DialogueBox;
import game.ui.UIElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Logger logger = new Logger(this.getClass().getName());
    public Tiles currentTiles;
    public java.util.List<Entity> entityList = new ArrayList<Entity>();
    public PlayerEntity playerEntity = new PlayerEntity();
    public List<Rectangle> boundaries = new ArrayList<Rectangle>();

    public List<UIElement> uiElements = new ArrayList<>();
    private final String tileIdPath;
    private final String tilePath;
    private final int spawnX;
    private final int spawnY;

    public Scene(String tileIdPath, String tilePath, int playerSpawnX, int playerSpawnY) {
        this.tileIdPath = tileIdPath;
        logger.addPrefix(tilePath);
        this.tilePath = tilePath;
        this.spawnX = playerSpawnX;
        this.spawnY = playerSpawnY;
        logger.Log(String.format("Spawn X:%d Y:%d", spawnX, spawnY));
        init();
    }

    public void reset() {
        logger.Log("resetting scene");
        currentTiles = null;
        entityList = new ArrayList<Entity>();
        playerEntity = new PlayerEntity();
        boundaries = new ArrayList<Rectangle>();
        uiElements = new ArrayList<>();
        init();
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public void spawnEntity(Entity entity, int x, int y) {
        entity.setPosition(x, y);
        entityList.add(entity);
    }

    private void init() {
        logger.Log("initializing");
        DialogueBox dialogueBox = new DialogueBox(Color.black, Color.white, Font.getFont(Font.MONOSPACED), 50, false, 200);
        this.uiElements.add(dialogueBox);
        this.currentTiles = new Tiles(tileIdPath, tilePath);
        playerEntity.setPosition(spawnX, spawnY);
        this.entityList.add(playerEntity);

        for (Tile tile : currentTiles.tiles) {
            if (tile.isSolid) {
                boundaries.add(new Rectangle(tile.column * currentTiles.getWidth(), tile.row * currentTiles.getHeight(), currentTiles.getWidth(), currentTiles.getHeight()));
            }
        }
    }

    public void update() {
        currentTiles.update();
        for (UIElement element : uiElements)
        {
            if (element.isVisible())
            {
                element.update();
            }
        }
        for (Entity entity : entityList) {
            entity.update();
        }
    }

    public void draw(Graphics g) {
        currentTiles.draw(g);
        for (UIElement element : uiElements)
        {
            if (element.isVisible())
            {
                element.draw(g);
            }
        }
        for (Entity entity : entityList) {
            entity.draw(g);
        }
    }

}