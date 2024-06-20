package game;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Tiles {

    private Tile[] tiles;
    private final Map<Integer, Sprite> tileMap = new HashMap<>();
    private final Map<Integer, Boolean> tileSolidMap = new HashMap<>();
    private int tileWidth;
    private int tileHeight;
    private int rowCount;
    private int columnCount;
    private final ResourceLoader resourceLoader;
    private final Logger logger;

    public Tiles(String idCSV, String levelCSV, int tileWidth, int tileHeight, ResourceLoader resourceLoader) {
        this.logger = new Logger(this.getClass().getName());
        this.logger.addPrefix(levelCSV);

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.resourceLoader = (resourceLoader == null) ? new DefaultResourceLoader() : resourceLoader;

        loadTilesIDFromCSV(idCSV);
        loadTilesFromCSV(levelCSV);
    }

    private void loadTilesIDFromCSV(String csvPath) {
        try {
            List<String> lines = resourceLoader.loadTextFile(csvPath);
            for (String line : lines) {
                String[] parts = line.split(",");
                int tileID = Integer.parseInt(parts[0]);
                String spritePath = parts[1];
                int animationInterval = Integer.parseInt(parts[2]);
                boolean isSolid = Boolean.parseBoolean(parts[3]);

                logger.Log(String.format("Loaded TileID: %d, SpritePath: %s, AnimationInterval: %d, IsSolid: %b",
                        tileID, spritePath, animationInterval, isSolid));

                tileMap.put(tileID, new Sprite(spritePath, tileWidth, tileHeight, animationInterval, resourceLoader));
                tileSolidMap.put(tileID, isSolid);
            }
        } catch (IOException e) {
            logger.Error("Error loading tile IDs from CSV: " + e.getMessage());
        }
    }
    private void loadTilesFromCSV(String csvPath) {
        List<Tile> loadedTiles = new ArrayList<>();
        try {
            List<String> lines = resourceLoader.loadTextFile(csvPath);
            int row = 0;
            for (String line : lines) {
                String[] parts = line.split(",");
                if (columnCount < parts.length) {
                    columnCount = parts.length; // Update columnCount to reflect the number of columns
                }
                for (int column = 0; column < parts.length; column++) {
                    int id = Integer.parseInt(parts[column]);
                    Boolean isSolid = tileSolidMap.get(id); // Retrieve Boolean value from map

                    // Check if isSolid is null
                    if (isSolid == null) {
                        logger.Error("No solidity information found for tile with ID: " + id);
                        // Handle this case, perhaps default to false or throw an exception
                        isSolid = false; // Example default value
                    }

                    loadedTiles.add(new Tile(row, column, id, isSolid));
                }
                row++;
            }
            rowCount = row;
            tiles = loadedTiles.toArray(new Tile[0]);
            logger.Log(String.format("Loaded tiles %d Rows, %d Columns", rowCount, columnCount));
        } catch (IOException e) {
            logger.Error("Error loading tiles from CSV: " + e.getMessage());
        }
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void drawTile(Graphics g, int tileID, int x, int y) {
        Sprite tileSprite = tileMap.get(tileID);
        if (tileSprite != null) {
            tileSprite.draw(g, x, y);
        } else {
            logger.Error("Missing tile with ID: " + tileID);
        }
    }

    public Tile getTile(int column, int row) {
        row += 1;
        for (Tile tile : tiles) {
            if (row == tile.row && column == tile.column) {
                return tile;
            }
        }
        return null;
    }

    public void draw(Graphics g) {
        for (Tile tile : tiles) {
            drawTile(g, tile.ID, tile.column * tileWidth, tile.row * tileHeight);
        }
    }

    public void update() {
        for (Sprite tileSprite : tileMap.values()) {
            tileSprite.update();
        }
    }

    public void setTileSize(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public int getWidth() {
        return tileWidth;
    }

    public int getHeight() {
        return tileHeight;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void dispose() {
        logger.Log("Disposing tiles resources");
        for (Sprite tileSprite : tileMap.values()) {
            tileSprite.dispose();
        }
        tileMap.clear();
        tileSolidMap.clear();
    }
}
