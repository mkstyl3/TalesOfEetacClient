package dsa.upc.edu.talesofeetacclient.Model.Main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 1/01/18.
 */

public class Chest implements Parcelable{

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("items")
    @Expose
    List<Item> items;

    public Chest() {
        items = new ArrayList<>();
    }

    protected Chest(Parcel in) {
        id = in.readInt();
        description = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Chest> CREATOR = new Creator<Chest>() {
        @Override
        public Chest createFromParcel(Parcel in) {
            return new Chest(in);
        }

        @Override
        public Chest[] newArray(int size) {
            return new Chest[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(description);
        parcel.writeTypedList(items);
    }
}
