package game;

import game.entities.Entity;
import game.entities.player.PlayerEntity;
import game.entities.player.PlayerInventory;
import game.items.Item;
import game.items.PickupItem;
import game.ui.DialogueBox;
import game.ui.Hotbar;
import game.ui.UIElement;

import javax.script.ScriptEngine;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scene {
    private final Logger logger = new Logger(this.getClass().getName());
    public Tiles currentTiles;
    public java.util.List<Entity> entityList = new ArrayList<Entity>();
    public List<PickupItem> pickupItemList = new ArrayList<>();
    public PlayerEntity playerEntity = new PlayerEntity();
    public List<Rectangle> boundaries = new ArrayList<Rectangle>();
    public List<UIElement> uiElements = new ArrayList<>();
    private final String tileIdPath;
    private final String tilePath;
    private final int spawnX;
    private final int spawnY;
    private ResourceLoader resourceLoader;

    public Scene(String tileIdPath, String tilePath, int playerSpawnX, int playerSpawnY, ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
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
        pickupItemList = new ArrayList<PickupItem>();
        playerEntity = new PlayerEntity();
        PlayerInventory.clearItemHistory();
        PlayerEntity.inventory.resetUses();
        boundaries = new ArrayList<Rectangle>();
        uiElements = new ArrayList<UIElement>();
        init();
    }

    public void setTile(Tile tileToSet, int id, int doorToLevel) {
        for (Tile tile : currentTiles.tiles) {
            if (tile.row == tileToSet.row && tile.column == tileToSet.column) {
                tile.ID = id;
                tile.doorToLevel = doorToLevel;
                break;
            }
        }
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public void spawnEntity(Entity entity, int x, int y) {
        logger.Log(String.format("Spawnining %s at %d,%d", entity.getClass().getName(), x, y));
        entity.setPosition(x, y);
        entityList.add(entity);
    }

    public void spawnItem(Item item, int x, int y)
    {
        PickupItem pickupItem = (PickupItem)item;
        pickupItem.setCoordinate(x, y);
        pickupItemList.add(pickupItem);
    }

    public int[] getRandomCoordsInMap() {
        Random random = new Random();
        int x, y;
        Rectangle candidateRect;

        boolean intersects;
        do {
            // Generate random coordinates
            x = random.nextInt(Game.WIDTH);
            y = random.nextInt(Game.HEIGHT);

            // Create a rectangle representing the candidate point
            candidateRect = new Rectangle(x, y, 1, 1);

            // Check if the candidate rectangle intersects with any of the rectangles in the list
            intersects = false;
            for (Rectangle rect : boundaries) {
                if (rect.intersects(candidateRect)) {
                    intersects = true;
                    break;
                }
            }

            // Continue generating new coordinates until they do not intersect with any rectangles
        } while (intersects);

        return new int[]{x, y};
    }

    public Tile getTileInCoordinate(int x, int y)
    {
        // Convert pixel coordinates to tile coordinates
        int column = x / currentTiles.getHeight();  // Assuming each tile is 32x32 pixels
        int row = y / currentTiles.getWidth();

        // Ensure column and row are within bounds
        if (column < 0 || column >= currentTiles.getColumnCount() || row < 0 || row >= currentTiles.getRowCount()) {
            return null;  // Out of bounds
        }

        // Get the tile at the calculated column and row
        return currentTiles.getTile(column, row);
    }

    public Tile getRandomTileInMap() {
        int[] coords = getRandomCoordsInMap();
        return getTileInCoordinate(coords[0], coords[1]);
    }

    private void init() {
        logger.Log("initializing");
        DialogueBox dialogueBox = new DialogueBox(Color.black, Color.white, Font.getFont(Font.MONOSPACED), 50, false, 200);
        this.uiElements.add(dialogueBox);
        this.uiElements.add(PlayerEntity.inventory.getHotbarUI());
        // make sure to adjust internal resolution to a resolution divisible by the tile width and height
        this.currentTiles = new Tiles(tileIdPath, tilePath, 32, 32, this.resourceLoader);
        playerEntity.setPosition(spawnX, spawnY);
        this.entityList.add(playerEntity);

        for (Tile tile : currentTiles.tiles) {
            if (tile.isSolid) {
                boundaries.add(
                        new Rectangle
                                (tile.column * currentTiles.getWidth(), tile.row * currentTiles.getHeight(),
                                        currentTiles.getWidth(), currentTiles.getHeight() )
                );
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
        List<PickupItem> itemsToRemove = new ArrayList<>();
        for (PickupItem item : pickupItemList)
        {
            item.update();
            if (playerEntity.isCollidingWith(item))
            {
                PlayerEntity.inventory.addItem(item);
                itemsToRemove.add(item);
            }
        }
        pickupItemList.removeAll(itemsToRemove);
    }

    public void draw(Graphics g2d) {
        currentTiles.draw(g2d);
        for (UIElement element : uiElements)
        {
            if (element.isVisible())
            {
                element.draw(g2d);
            }
        }

        for (Entity entity : entityList) {
            entity.draw(g2d);
        }

        for (PickupItem item : pickupItemList)
        {
            item.draw(g2d);
        }
    }

    public void disposeEntities(List<Entity> entities)
    {
        for (Entity entity : entities)
        {
            entity.dispose();
        }
    }

    public void dispose()
    {
        logger.Log("Disposing scene resources");
        disposeEntities(entityList);
        pickupItemList.clear();
        entityList.clear();
        boundaries.clear();
        uiElements.clear();
        currentTiles.dispose();
        playerEntity.dispose();
    }

}