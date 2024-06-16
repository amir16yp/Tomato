package game.items;

import game.Game;
import game.Sprite;

public class DoorKey extends PickupItem{
    private int doorToID;
    public DoorKey(int doorToID)  {
        super(new Sprite("/game/sprites/items/doorkey.png", 16, 16, 1000, Game.defaultResourceLoader), 1, 0);
        this.doorToID = doorToID;
    }

    public int getDoorToID() {
        return doorToID;
    }
}
