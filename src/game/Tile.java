package game;

public class Tile {
    public final int column;
    public final int row;
    public int ID;
    public boolean isSolid;

    public Tile(int row, int column, int ID, boolean isSolid) {
        this.row = row;
        this.column = column;
        this.ID = ID;
        this.isSolid = isSolid;
    }
}
