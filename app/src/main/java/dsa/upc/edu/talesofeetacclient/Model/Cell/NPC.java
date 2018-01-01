package dsa.upc.edu.talesofeetacclient.Model.Cell;

/**
 * Created by Josean on 07/11/2017.
 */
public class NPC extends Cell {

    private final String type = "NPC";
    private String dialogue;

    public NPC () {};

    public String getDialogue() {
        return dialogue;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    @Override
    public String getType() {
        return type;
    }

}
