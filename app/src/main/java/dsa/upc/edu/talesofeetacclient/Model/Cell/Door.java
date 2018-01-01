package dsa.upc.edu.talesofeetacclient.Model.Cell;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import dsa.upc.edu.talesofeetacclient.R;

public class Door extends Cell {

    private int nextMap;
    private final String type = "Door";


    public Door () {

    } //"yes, it is unnecessary to include super() in the child constructor", because super() is a call to the "accessible no-args constructor"


    public int getNextMap() {
        return nextMap;
    }

    public void setNextMap(int nextmap) {
        this.nextMap = nextmap;
    }

    @Override
    public String getType() {
        return type;
    }
}
