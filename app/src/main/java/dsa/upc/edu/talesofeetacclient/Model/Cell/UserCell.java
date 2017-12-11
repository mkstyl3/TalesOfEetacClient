package dsa.upc.edu.talesofeetacclient.Model.Cell;

import dsa.upc.edu.talesofeetacclient.Model.Main.User;

public class UserCell extends Cell {

    final protected String symbol = "@";

    private User u;

    public UserCell () {
        u = new User();
    }

    public UserCell (User u) {
        this.u = new User ();
        this.u = u;
        super.setOnMapLoc(u.getLocation());
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    @Override
    public String getSYMBOL() {
        return this.symbol;
    }

}
