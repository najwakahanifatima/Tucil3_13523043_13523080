package gui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;

import java.util.HashMap;
import java.util.Map;

public class BoardPage {
    private int rows;
    private int cols;
    private int exitCol;
    private int exitRow;
    private final GridPane board;
    private final GridPane wrapper;
    private final int CELL_SIZE = 60;
    private final Color[] colors = {
        Color.GREEN, Color.BLUE, Color.ORANGE,
        Color.PURPLE, Color.YELLOW, Color.PINK, Color.BROWN,
        Color.CYAN, Color.MAGENTA
    };

    private final Map<Rectangle, Block> blockMap = new HashMap<>();
    private Rectangle selectedBlockRect = null;
    private Block selectedBlock = null;

    public BoardPage(Block[] blocks, int rows, int cols, String exitOrient, int exitPos) {
        this.rows = rows;
        this.cols = cols;

        if (exitOrient == "Bottom") {
            this.exitCol = exitPos;
            this.exitRow = rows;
        } else if (exitOrient == "Above") {
            this.exitCol = exitPos;
            this.exitRow = -1;
        } else if (exitOrient == "Left") {
            this.exitCol = -1;
            this.exitRow = exitPos;
        } else { // Right
            this.exitCol = cols;
            this.exitRow = exitPos;
        }

        board = new GridPane();
        board.setGridLinesVisible(true);
        board.setPrefSize(rows * CELL_SIZE, cols * CELL_SIZE);

        // setup board
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                cell.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                cell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                setupClickTarget(cell);
                board.add(cell, col, row);
            }
        }

        // setup side bar
        VBox sidebar = new VBox(10);
        sidebar.setLayoutX(rows * CELL_SIZE + 10);
        sidebar.setLayoutY(10);
        sidebar.setAlignment(Pos.TOP_CENTER);

        for (int i = 0; i < blocks.length; i++) {
            Block b = blocks[i];
            Rectangle rect = new Rectangle(
                b.orientation.equals("Horizontal") ? b.length * CELL_SIZE : CELL_SIZE,
                b.orientation.equals("Vertical") ? b.length * CELL_SIZE : CELL_SIZE
            );
            char blockChar;
            Color blockColor;
            if (i == 0) {
                blockChar = (char) ('P');
                blockColor = Color.RED;
            } else {
                blockChar = (char) ('A' + i);
                if (blockChar == 'K') {
                    blockChar = 'Z';
                }
                blockColor = colors[i % blocks.length];
            }
            
            b.character = blockChar;
            b.color = blockColor;
            rect.setFill(blockColor);
            Label charLabel = new Label(String.valueOf(blockChar));
            charLabel.setTextFill(Color.WHITE);

            StackPane blockPane = new StackPane(rect, charLabel);
            blockPane.setPrefSize(rect.getWidth(), rect.getHeight());

            setupClickable(blockPane, rect, b); // Modify setupClickable to accept StackPane 
            blockMap.put(rect, b);
            sidebar.getChildren().add(blockPane);
        }

        HBox root = new HBox(20);
        root.getChildren().addAll(board, sidebar);

        wrapper = new GridPane();
        wrapper.add(root, 0, 0);
    }

    private void setupClickable(StackPane blockPane, Rectangle rect, Block b) {
        blockPane.setOnMouseClicked(e -> {
            selectedBlockRect = rect;
            selectedBlock = b;

            for (Rectangle r : blockMap.keySet()) {
                r.setStroke(null);
            }
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        });
    }

    private void setupClickTarget(StackPane cell) {
        cell.setOnMouseClicked(e -> {
            if (selectedBlock == null || selectedBlock.placed) return;

            Integer row = GridPane.getRowIndex(cell);
            Integer col = GridPane.getColumnIndex(cell);
            row = (row == null) ? 0 : row;
            col = (col == null) ? 0 : col;

            // Check bounds
            if ((selectedBlock.orientation.equals("Horizontal") && col + selectedBlock.length > rows) ||
                (selectedBlock.orientation.equals("Vertical") && row + selectedBlock.length > cols)) {
                return;
            }

            // First pass: check for overlap
            for (int i = 0; i < selectedBlock.length; i++) {
                int targetCol = selectedBlock.orientation.equals("Horizontal") ? col + i : col;
                int targetRow = selectedBlock.orientation.equals("Horizontal") ? row : row + i;

                for (Node node : board.getChildren()) {
                    Integer nodeCol = GridPane.getColumnIndex(node);
                    Integer nodeRow = GridPane.getRowIndex(node);
                    nodeCol = (nodeCol == null) ? 0 : nodeCol;
                    nodeRow = (nodeRow == null) ? 0 : nodeRow;

                    if (nodeCol == targetCol && nodeRow == targetRow && node instanceof StackPane) {
                        StackPane targetCell = (StackPane) node;
                        Background bg = targetCell.getBackground();

                        if (bg != null && !bg.getFills().isEmpty() && !isWhite(bg)) {
                            return; // found overlap
                        }
                    }
                }
            }

            // Second pass: place the block
            for (int i = 0; i < selectedBlock.length; i++) {
                int targetCol = selectedBlock.orientation.equals("Horizontal") ? col + i : col;
                int targetRow = selectedBlock.orientation.equals("Horizontal") ? row : row + i;

                for (Node node : board.getChildren()) {
                    Integer nodeCol = GridPane.getColumnIndex(node);
                    Integer nodeRow = GridPane.getRowIndex(node);
                    nodeCol = (nodeCol == null) ? 0 : nodeCol;
                    nodeRow = (nodeRow == null) ? 0 : nodeRow;

                    if (nodeCol == targetCol && nodeRow == targetRow && node instanceof StackPane) {
                        Label label = new Label(String.valueOf(selectedBlock.character));
                        label.setTextFill(Color.WHITE);
                        StackPane targetCell = (StackPane) node;
                        targetCell.getChildren().add(label);
                        targetCell.setBackground(new Background(new BackgroundFill(selectedBlock.color, null, null)));
                        targetCell.setBorder(new Border(new BorderStroke(Color.BLACK,
                                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                    }
                }
            }

            selectedBlock.placed = true;
            selectedBlockRect.setVisible(false);
            selectedBlock = null;
            selectedBlockRect = null;
        });
    }


    private boolean isWhite(Background bg) {
        Color color = (Color) bg.getFills().get(0).getFill();
        return color.equals(Color.WHITE);
    }


    public GridPane getLayout() {
        return wrapper;
    }
}
