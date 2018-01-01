package dsa.upc.edu.talesofeetacclient.Model.Cell;

import java.util.ArrayList;
import java.util.List;

import dsa.upc.edu.talesofeetacclient.Model.Main.Chest;
import dsa.upc.edu.talesofeetacclient.Model.Main.Item;

/**
 * Created by mike on 1/01/18.
 */

public class ChestCell extends Cell{

    private Chest chest;
    private final String type = "ChestCell";

    public ChestCell () {
        chest = new Chest();
    }
    public Chest getChest() {
        return chest;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    @Override
    public String getType() {
        return type;
    }
}
