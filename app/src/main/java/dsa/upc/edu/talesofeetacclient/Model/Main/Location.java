package dsa.upc.edu.talesofeetacclient.Model.Main;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mike on 11/12/17.
 */

public class Location implements Parcelable {

    //Private variables
    @SerializedName("x")
    @Expose
    private int x;
    @SerializedName("y")
    @Expose
    private int y;

    //Constructors
    public Location (){ }
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected Location(Parcel in) {
        x = in.readInt();
        y = in.readInt();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    // Getters and Setters
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void setCoords (int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(x);
        parcel.writeInt(y);
    }
}
