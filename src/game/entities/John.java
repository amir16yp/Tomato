package game.entities;

import game.dialogue.DialogueLoader;

public class John extends NPC {
    public John() {
        super("John", DialogueLoader.loadDialogueTree("/game/entities/john.xml", true));
    }
}
