package gui;

import javafx.scene.paint.Color;

public class Block {
    public int length;
    public String orientation;
    public Color color;
    public boolean placed;
    public char character;

    public Block() {
        length = 2;
        orientation = "Horizontal";
        color = Color.BROWN;
        placed = false;
        character = '.';
    }
}