package game;

import game.entities.Entity;
import game.entities.TileEntity;
import game.entities.player.PlayerEntity;
import game.entities.player.PlayerInventory;
import game.items.Item;
import game.items.PickupItem;
import game.registry.SceneRegistry;
import game.ui.DialogueBox;
import game.ui.Hotbar;
import game.ui.UIElement;

import javax.script.ScriptEngine;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
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
    public List<TileEntity> tileEntitiesList = new ArrayList<>();
    private final String tileIdPath;
    private final String tilePath;
    private final int spawnX;
    private final int spawnY;
    private ResourceLoader resourceLoader;
    public Lighting lighting;
    public int sceneId;
    public boolean initalized = false;
    public Scene(String tileIdPath, String tilePath, int playerSpawnX, int playerSpawnY, ResourceLoader resourceLoader)
    {
        this.resourceLoader = resourceLoader;
        this.tileIdPath = tileIdPath;
        logger.addPrefix(tilePath);
        this.tilePath = tilePath;
        this.spawnX = playerSpawnX;
        this.spawnY = playerSpawnY;
        this.lighting = new Lighting();
        logger.Log(String.format("Spawn X:%d Y:%d", spawnX, spawnY));
        //init();
    }

    public void reset() {
        logger.Log("resetting scene");
        initalized = false;
        currentTiles = null;
        entityList = new ArrayList<Entity>();
        pickupItemList = new ArrayList<PickupItem>();
        playerEntity = new PlayerEntity();
        PlayerInventory.clearItemHistory();
        PlayerEntity.inventory.resetUses();
        lighting = new Lighting();
        boundaries = new ArrayList<Rectangle>();
        uiElements = new ArrayList<UIElement>();
        init();
    }

    public void setTile(Tile tileToSet, int id) {
        for (Tile tile : currentTiles.getTiles()) {
            if (tile.row == tileToSet.row && tile.column == tileToSet.column) {
                tile.ID = id;
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
        logger.Log(String.format("Spawnining entity %s at %d,%d", entity.getClass().getName(), x, y));
        entity.setPosition(x, y);
        entityList.add(entity);
    }

    public void spawnItem(PickupItem item, int x, int y)
    {
        logger.Log(String.format("Spawning item %s at %d,%d", item.getClass().getName(), x, y));
        item.setCoordinate(x, y);
        pickupItemList.add(item);
    }

    public void spawnTileEntity(TileEntity tileEntity, int x, int y)
    {
        logger.Log(String.format("Spawning tileEntity %s at %d,%d", tileEntity.getClass().getName(), x, y));
        tileEntity.setX(x);
        tileEntity.setY(y);
        tileEntitiesList.add(tileEntity);
    }

    public int[] getRandomCoordsInMap() {
        Random random = new Random();
        int x, y;
        Rectangle candidateRect;

        boolean intersects;
        do {
            // Generate random coordinates
            x = random.nextInt(Game.INTERNAL_WIDTH);
            y = random.nextInt(Game.INTERNAL_HEIGHT);

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

    void init() {
        logger.Log("initializing");
        DialogueBox dialogueBox = new DialogueBox(Color.black, Color.white, Font.getFont(Font.MONOSPACED), 50, false, 200);
        this.uiElements.add(dialogueBox);
        PlayerEntity.inventory.getHotbarUI().setVisible(true);
        this.uiElements.add(PlayerEntity.inventory.getHotbarUI());
        // make sure to adjust internal resolution to a resolution divisible by the tile width and height
        this.currentTiles = new Tiles(tileIdPath, tilePath, 32, 32, this.resourceLoader);
        playerEntity.setPosition(spawnX, spawnY);
        //this.entityList.add(playerEntity);

        for (Tile tile : currentTiles.getTiles()) {
            if (tile.isSolid) {
                boundaries.add(
                        new Rectangle
                                (tile.column * currentTiles.getWidth(), tile.row * currentTiles.getHeight(),
                                        currentTiles.getWidth(), currentTiles.getHeight() )
                );
            }
        }
        SceneRegistry.registry.update(this);
        initalized = true;
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
        playerEntity.update();
        for (Entity entity : entityList) {
            entity.update();
        }

        for (TileEntity tileEntity : tileEntitiesList)
        {
            tileEntity.update();
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

    public void draw(Graphics2D g2d) {
        // Draw the scene components to an off-screen image
        BufferedImage sceneImage = new BufferedImage(Game.INTERNAL_WIDTH, Game.INTERNAL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D sceneGraphics = sceneImage.createGraphics();

        // Render tiles and lighting effects
        currentTiles.draw(sceneGraphics); // Draw tiles
        lighting.drawLighting(sceneGraphics, sceneImage); // Apply lighting effects

        // Draw entities (e.g., monsters, player)
        for (Entity entity : entityList) {
            entity.draw(sceneGraphics); // Draw entity
        }

        for (TileEntity tileEntity : tileEntitiesList)
        {
            tileEntity.draw(sceneGraphics);
        }

        // Draw pickup items
        for (PickupItem item : pickupItemList) {
            item.draw(sceneGraphics); // Draw pickup item
        }

        // Draw player entity last to ensure it's on top
        playerEntity.draw(sceneGraphics); // Draw player entity

        // Draw darkness overlay
        lighting.drawDarkness(sceneGraphics, sceneImage);

        // Draw the final scene image onto the screen
        g2d.drawImage(sceneImage, 0, 0, null);

        // Dispose of the off-screen graphics object
        sceneGraphics.dispose();

        // Draw UI elements directly on the main graphics context
        for (UIElement uiElement : uiElements) {
            if (uiElement.isVisible()) {
                uiElement.draw(g2d); // Draw UI element
            }
        }
    }

    public void disposeEntities(List<Entity> entities) {
        Iterator<Entity> iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            entity.dispose();
            iterator.remove(); // Use the iterator's remove method to avoid ConcurrentModificationException
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
        if (lighting != null)
        {
            lighting.clearLightSources();
            lighting = null;
        }
        if (currentTiles != null)
        {
            currentTiles.dispose();
        }
        if (playerEntity != null)
        {
            playerEntity.dispose();
        }
        this.initalized = false;
    }

}