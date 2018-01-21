package dsa.upc.edu.talesofeetacclient.Model.Main;

/**
 * Created by mike on 1/01/18.
 */

public class ChestItem {

    private int id;
    private int chestId;
    private int itemId;

    public ChestItem() {}

    public ChestItem(int chestId, int itemId) {
        this.chestId = chestId;
        this.itemId = itemId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return chestId;
    }

    public void setUserId(int userId) {
        this.chestId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
