package dsa.upc.edu.talesofeetacclient.Model.Main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 4/12/17.
 */

public class User implements Parcelable {
//Private atributes

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("items")
    @Expose
    private List<Item> items;
    @SerializedName("location")
    @Expose
    private Location location;


    //Constructors

    public User () {

    }

    public User(int id, String name, String password, Location location) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.items = new ArrayList<>();
        this.location = location;
    }

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.items = new ArrayList<>();
    }

    //Getters and Sertters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        name = in.readString();
        password = in.readString();
        email = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
        location = in.readParcelable(Location.class.getClassLoader());
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
        parcel.writeString(name);
        parcel.writeString(password);
        parcel.writeString(email);
        parcel.writeTypedList(items);
        parcel.writeParcelable(location, i);
    }
}
