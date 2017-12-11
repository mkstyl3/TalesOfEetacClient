package dsa.upc.edu.talesofeetacclient.Model.Cell;

//Functions where involve Cells and what the can contain: Users, Objects or Enemies

/*
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
*/
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

    //Custom implementation on Subclasses
    public abstract String getSYMBOL();

    //No Constructor because it's abstract

    /*Getters and Setters

    Should be inhereted by Subclasses*/
    public  Location getOnMapLoc() {
        return this.onMapLoc;
    }
    public void setOnMapLoc(Location loc) {
        this.onMapLoc = loc;
    }
}
