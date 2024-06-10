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

public class Scene {
    private Logger logger = new Logger(this.getClass().getName());
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
        pickupItemList = new ArrayList<PickupItem>();
        playerEntity = new PlayerEntity();
        PlayerInventory.clearItemHistory();
        PlayerEntity.inventory.resetUses();
        boundaries = new ArrayList<Rectangle>();
        uiElements = new ArrayList<UIElement>();
        init();
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


    private void init() {
        logger.Log("initializing");
        DialogueBox dialogueBox = new DialogueBox(Color.black, Color.white, Font.getFont(Font.MONOSPACED), 50, false, 200);
        this.uiElements.add(dialogueBox);
        this.uiElements.add(PlayerEntity.inventory.getHotbarUI());
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

        for (PickupItem item : pickupItemList)
        {
            item.draw(g);
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