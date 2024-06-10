package game.entities;

import game.Sprite;
import game.dialogue.DialogueNode;
public class NPC extends Entity{

    private DialogueNode defaultDialogue;
    public double distanceToTalk = 10.0;

    public NPC(String name, DialogueNode defaultDialogue) {
        super(name);
        this.defaultDialogue = defaultDialogue;
        // temporary
        this.addSprite("north", new Sprite("sprites/zombie/walk_north.png", 32, 32, 100));
        this.addSprite("east", new Sprite("sprites/zombie/walk_east.png", 32, 32, 100));
        this.addSprite("west", new Sprite("sprites/zombie/walk_west.png", 32, 32, 100));
        this.addSprite("south", new Sprite("sprites/zombie/walk_south.png", 32, 32, 100));
        this.setCurrentSprite("south");
    }

    public DialogueNode interaction()
    {
        if (this.hp == 0)
        {
            return new DialogueNode("This NPC is dead");
        }
        return defaultDialogue;
    }

}
