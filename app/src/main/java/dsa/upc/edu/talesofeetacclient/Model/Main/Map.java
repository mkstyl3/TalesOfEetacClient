package dsa.upc.edu.talesofeetacclient.Model.Main;

import dsa.upc.edu.talesofeetacclient.Model.Cell.Cell;

/**
 * Created by mike on 11/12/17.
 */

public class Map {

    private int id;
    private Cell[] cellArray;

    public Cell[] getCellArray() {
        return cellArray;
    }

    public void setCellArray(Cell[] cellArray) {
        this.cellArray = cellArray;
    }



    public Map () {}

    public Map (int id, Cell[] cellmap) {
        this.id = id;
        cellArray = new Cell[144];
        this.cellArray = cellmap;
    }

    public Cell getCell (Location cellLoc) {
        return cellArray[cellLoc.getX()*10+cellLoc.getY()];
    }

    public Cell getCellByCoords (int x, int y) {
        return cellArray[x*10+y];
    }


    public void setCell (Cell cell) {
        this.cellArray[cell.getOnMapLoc().getX()*10+cell.getOnMapLoc().getY()] = cell;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
