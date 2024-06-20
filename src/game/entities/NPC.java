package game.entities;

import game.Game;
import game.Sprite;
import game.dialogue.DialogueNode;
public class NPC extends Entity{

    private DialogueNode defaultDialogue;
    public double distanceToTalk = 10.0;

    public NPC(String name, DialogueNode defaultDialogue) {
        super(name);
        this.defaultDialogue = defaultDialogue;
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
