package utils;

public class Position {
    public int row;
    public int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position(Position other) {
        this.row = other.row;
        this.col = other.col;
    }

    public void displayPosition() {
    System.out.println("Row: " + row + ", Col: " + col);
    }

    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }

}