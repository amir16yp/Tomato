package game.dialogue;

public class DialogueOption {
    private String text;
    private DialogueNode nextNode;

    public DialogueOption(String text, DialogueNode nextNode) {
        this.text = text;
        this.nextNode = nextNode;
    }

    public String getText() {
        return text;
    }

    public DialogueNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(DialogueNode nextNode) {
        this.nextNode = nextNode;
    }

}

