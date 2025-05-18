package utils;

public class Vehicle {
    private char id;
    private int row;
    private int col;
    private int length;
    private boolean isHorizontal;

    public Vehicle(char id, int row, int col, int length, boolean isHorizontal) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.length = length;
        this.isHorizontal = isHorizontal;
    }

    public Vehicle(Vehicle other) {
        this.id = other.id;
        this.row = other.row;
        this.col = other.col;
        this.length = other.length;
        this.isHorizontal = other.isHorizontal;
    }

    public boolean canMove(int direction, char[][] board) {
        // direction: 1 = maju (kanan/bawah), -1 = mundur (kiri/atas)
        if (isHorizontal) {
            if (direction < 0) { 
                // cek gerakan ke kiri
                int newCol = col - 1;
                return newCol >= 0 && board[row][newCol] == '.';
            } else { 
                // cek gerakan ke kanan
                int newCol = col + length;
                return newCol < board[0].length && board[row][newCol] == '.';
            }
        } else { 
            // vertikal
            if (direction < 0) { 
                // cek gerakan ke atas
                int newRow = row - 1;
                return newRow >= 0 && board[newRow][col] == '.';
            } else { 
                // cek gerakan ke bawah
                int newRow = row + length;
                return newRow < board.length && board[newRow][col] == '.';
            }
        }
    }

    public void move(int direction) {
        if (isHorizontal) {
            col += direction;
        } else {
            row += direction;
        }
    }
    
    public char getId() {
        return id;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }

    public int getLength() {
        return length;
    }
    
    public boolean isHorizontal() {
        return isHorizontal;
    }
    
    @Override
    public String toString() {
        return id + " at (" + row + "," + col + ") length=" + length + " " + (isHorizontal ? "horizontal" : "vertical");
    }
}
