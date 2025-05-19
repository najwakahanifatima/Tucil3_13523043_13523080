package utils;

public class Move{
    char name;
    String direction;
    int count;

    Move(char name, String direction, int count){
        this.name = name;
        this.direction = direction;
        this.count = count;
    }

    public String getDirection()   {
        return direction;
    }

    public int getCount() {
        return count;
    }
    public char getName() {
        return name;
    }
};