package dsa.upc.edu.talesofeetacclient.Model.Cell;

public class Door extends Cell {

    private int nextMap;
    final private String symbol = "D";


    public Door () { } //"yes, it is unnecessary to include super() in the child constructor", because super() is a call to the "accessible no-args constructor"


    public int getNextMap() {
        return nextMap;
    }

    public void setNextMap(int nextmap) {
        this.nextMap = nextmap;
    }

    @Override
    public String getSYMBOL() {
        return this.symbol;
    }
}
