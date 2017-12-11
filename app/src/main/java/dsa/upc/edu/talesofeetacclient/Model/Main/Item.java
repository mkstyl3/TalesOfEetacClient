package dsa.upc.edu.talesofeetacclient.Model.Main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mike on 11/12/17.
 */

public class Item implements Parcelable{

    //Variable declarations
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("value")
    @Expose
    private double value;
    @SerializedName("cost")
    @Expose
    private int cost;

    //Constructors

    public Item(int id, String name, int type, String description, double value, int cost) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.value = value;
        this.cost = cost;
    }

    public Item (){}

    //Getters and Setters

    protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readInt();
        description = in.readString();
        value = in.readDouble();
        cost = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(type);
        parcel.writeString(description);
        parcel.writeDouble(value);
        parcel.writeInt(cost);
    }
}
