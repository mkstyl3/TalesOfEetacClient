package dsa.upc.edu.talesofeetacclient.Model.Cell;

//Functions where involve Cells and what the can contain: Users, Objects or Enemies

/*
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
*/
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import dsa.upc.edu.talesofeetacclient.Model.Main.Location;


/* Necesary to Serialize and Deserialize abstract classes and its childs. */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Door.class, name = "Door"),
        @JsonSubTypes.Type(value = Tree.class, name = "Tree"),
        @JsonSubTypes.Type(value = UserCell.class, name = "UserCell"),
        @JsonSubTypes.Type(value = Wall.class, name = "Wall"),
        @JsonSubTypes.Type(value = Field.class, name = "Field"),
        @JsonSubTypes.Type(value = NPC.class, name = "NPC"),
        @JsonSubTypes.Type(value = ChestCell.class, name = "ChestCell")
})
public abstract class Cell {

    /*Variables

    onMapLoc is inherited*/

    protected Location onMapLoc;
    protected Bitmap bitmap;
    protected Rect rect;
    protected String type;

    //Custom implementation on Subclasses

    public Cell (){}

    public Cell (Location location, Bitmap bitmap, Rect rect) {
        this.onMapLoc = location;
        this.bitmap = bitmap;
        this.rect = rect;
    }

    public Cell (Location location, Rect rect) {
        this.onMapLoc = location;
        this.rect = rect;
    }

    /*Getters and Setters

    Should be inhereted by Subclasses*/
    public  Location getOnMapLoc() {
        return this.onMapLoc;
    }
    public void setOnMapLoc(Location loc) {
        this.onMapLoc = loc;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public String getType() {
        return type;
    }
}
