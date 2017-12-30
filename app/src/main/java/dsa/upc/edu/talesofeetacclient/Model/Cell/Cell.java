package dsa.upc.edu.talesofeetacclient.Model.Cell;

//Functions where involve Cells and what the can contain: Users, Objects or Enemies

/*
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
*/
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import dsa.upc.edu.talesofeetacclient.Model.Main.Location;


/* Necesary to Serialize and Deserialize abstract classes and its childs. */
/*
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
        @JsonSubTypes.Type(value = NPC.class, name = "NPC")
})*/
public abstract class Cell {

    /*Variables

    onMapLoc is inherited*/
    protected Location onMapLoc;
    protected Bitmap bitmap;
    protected Canvas canvas;
    protected Rect rect;
    protected Drawable drawable;

    //Custom implementation on Subclasses
    //public abstract String getSYMBOL();

    public Cell (){}

    public Cell (Location location, Bitmap bitmap, Canvas canvas, Drawable drawable) {
        this.onMapLoc = location;
        this.bitmap = bitmap;
        this.canvas = canvas;
        this.rect = cellMapper(location);

    }

    private Rect cellMapper(Location location) {
        int x = location.getX();
        int y = location.getY();
        return null;
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

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }



}
