package dsa.upc.edu.talesofeetacclient.Model.Cell;

/**
 * Created by Josean on 21/01/2018.
 */

public class Floor extends Cell {

    private final String type = "Floor";

    public Floor () {}

    @Override
    public String getType() {
        return type;
    }

}