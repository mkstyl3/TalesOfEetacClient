package dsa.upc.edu.talesofeetacclient.Model.Main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mike on 4/12/17.
 */

public class User implements Parcelable {
//Private atributes

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("lastMap")
    @Expose
    private int lastMap;
    @SerializedName("items")
    @Expose
    private List<Item> items;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("admin")
    @Expose
    private boolean admin;

    //Constructors

    public User () {

    }

    public User(int id, String username, String password, Location location) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.items = new ArrayList<>();
        this.location = location;
    }

    public User(int id, String username, String password, boolean admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.items = new ArrayList<>();
    }

    //Getters and Sertters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLastMap() {return lastMap;}

    public void setLastMap(int lastMap) {this.lastMap = lastMap;}

    public boolean getAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setItem (Item i) {
        this.items.add(i);
    }
    public Item getItem(int position) {
        return this.items.get(position);
    }

    //Parcelable implementation

    protected User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        password = in.readString();
        lastMap = in.readInt();
        items = in.createTypedArrayList(Item.CREATOR);
        location = in.readParcelable(Location.class.getClassLoader());
        admin = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeInt(lastMap);
        parcel.writeTypedList(items);
        parcel.writeParcelable(location, i);
        parcel.writeByte((byte) (admin ? 1 : 0));
    }
}
