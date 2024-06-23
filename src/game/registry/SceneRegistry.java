package game.registry;

import game.Logger;
import game.Scene;
import game.Tile;
import game.entities.Entity;
import game.entities.TileEntity;
import game.items.PickupItem;
import game.ui.UIElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneRegistry {

    public static SceneRegistry registry = new SceneRegistry();

    private int idCount = 0;
    private Map<Integer, Scene> scenesMap = new HashMap<>();
    private Map<Integer, List<Entity>> sceneEntityMap = new HashMap<>();
    private Map<Integer, List<TileEntity>> sceneTileEntityMap = new HashMap<>();

    private Map<Integer, List<PickupItem>> scenePickupItemMap = new HashMap<>();
    private Map<Integer, List<UIElement>> sceneUiElementMap = new HashMap<>();
    private final Logger logger = new Logger(this.getClass().getName());

    public SceneRegistry()
    {
        logger.Log("Initialized scene registry");
    }

    public int registerScene(Scene scene) {
        scene.sceneId = idCount;
        scenesMap.put(idCount, scene);
        sceneEntityMap.put(idCount, new ArrayList<>());
        scenePickupItemMap.put(idCount, new ArrayList<>()); // Initialize empty list for PickupItems
        sceneUiElementMap.put(idCount, new ArrayList<>()); // Initialize empty list for UIElements
        sceneTileEntityMap.put(idCount, new ArrayList<>());
        idCount++;
        logger.Log("registered scene ID " + scene.sceneId);
        return scene.sceneId;
    }

    public Scene getScene(int id) {
        return scenesMap.get(id);
    }

    public List<Entity> getEntitiesInScene(int id) {
        return sceneEntityMap.get(id);
    }

    public List<PickupItem> getPickupItemsInScene(int id) {
        return scenePickupItemMap.get(id);
    }

    public List<UIElement> getUiElementsInScene(int id) {
        return sceneUiElementMap.get(id);
    }

    public List<TileEntity> getTileEntitiesInScene(int id)
    {
        return sceneTileEntityMap.get(id);
    }

    public void registerEntityInScene(int sceneId, Entity entity) {
        List<Entity> entities = sceneEntityMap.get(sceneId);
        if (entities != null) {
            entities.add(entity);
        }
    }

    public void registerPickupItemInScene(int sceneId, PickupItem item) {
        List<PickupItem> items = scenePickupItemMap.get(sceneId);
        if (items != null) {
            items.add(item);
        }
    }

    public void registerUiElementInScene(int sceneId, UIElement uiElement) {
        List<UIElement> uiElements = sceneUiElementMap.get(sceneId);
        if (uiElements != null) {
            uiElements.add(uiElement);
        }
    }

    public void registerTileEntitiesInScene(int sceneId, TileEntity tileEntity)
    {
        List<TileEntity> tileEntities = sceneTileEntityMap.get(sceneId);
        if (tileEntities != null)
        {
            tileEntities.add(tileEntity);
        }
    }

    // Method to update a Scene using its own update method
    public void update(Scene scene) {
        scene.entityList.addAll(getEntitiesInScene(scene.sceneId));
        scene.uiElements.addAll(getUiElementsInScene(scene.sceneId));
        scene.tileEntitiesList.addAll(getTileEntitiesInScene(scene.sceneId));
        scene.pickupItemList.addAll(getPickupItemsInScene(scene.sceneId));
    }
}
