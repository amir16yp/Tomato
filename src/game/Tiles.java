package game;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.*;

public class Tiles {

    public Tile[] tiles;
    private final Map<Integer, Sprite> tileMap;
    private final Map<Integer, Boolean> tileSolidMap;
    private final Map<Integer, Integer> tileDoorTo;
    private int tileWidth = 32;
    private int tileHeight = 32;
    private int rowCount;
    private int columnCount = 0;
    private ResourceLoader resourceLoader;
    private Logger logger = new Logger(this.getClass().getName());

    public Tiles(String idCSV, String levelCSV, ResourceLoader resourceLoader) {
        logger.addPrefix(levelCSV);
        tileMap = new HashMap<>();
        tileSolidMap = new HashMap<>();
        tileDoorTo = new HashMap<>();
        this.resourceLoader = resourceLoader;
        loadTilesIDFromCSV(idCSV);
        loadTilesFromCSV(levelCSV);
    }

    private void loadTilesIDFromCSV(String csvPath) {
        logger.Log("Loading tile IDs from CSV " + csvPath);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(csvPath))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                //System.out.println(parts[1]);
                int tileID = Integer.parseInt(parts[0]);
                String spritePath = parts[1];
                int animationInterval = Integer.parseInt(parts[2]);
                boolean isSolid = Boolean.parseBoolean(parts[3]);
                int doorToLevel = Integer.parseInt(parts[4]);
                logger.Log("TileID: " + tileID + ", SpritePath: " + spritePath + ", AnimationInterval: " + animationInterval + ", IsSolid: " + isSolid + ", DoorToLevel: " + doorToLevel);
                tileMap.put(tileID, new Sprite(spritePath, tileWidth, tileHeight, animationInterval,  this.resourceLoader));
                tileSolidMap.put(tileID, isSolid);
                tileDoorTo.put(tileID, doorToLevel);
            }
        } catch (Exception e) {
            logger.Error(e);
        }
    }

    private void loadTilesFromCSV(String csvPath) {
        List<Tile> loadedTiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(csvPath))))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                if (columnCount < parts.length) {
                    columnCount = parts.length; // Update columnCount to reflect the number of columns
                }
                for (int column = 0; parts.length > column; column++) {
                    int id = Integer.parseInt(parts[column]);
                    loadedTiles.add(new Tile(row, column, id, tileSolidMap.get(id), tileDoorTo.get(id)));
                }
                row++;
            }
            rowCount = row;
            tiles = loadedTiles.toArray(Tile[]::new);
            logger.Log(String.format("Loaded tiles %d Rows, %d Columns", rowCount, columnCount));
        } catch (Exception e) {
            logger.Error(e);
        }
    }

    public void drawTile(Graphics g, int tileID, int x, int y) {
        Sprite tileSprite = tileMap.get(tileID);
        if (tileSprite != null) {
            tileSprite.draw(g, x, y);
        } else {
            // Handle missing tile (e.g., draw a placeholder or log a warning)
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
            drawTile(g, tile.ID, tile.column * tileWidth, tile.row * getHeight());
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
        tileDoorTo.clear();
    }
}
