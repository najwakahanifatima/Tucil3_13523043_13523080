import utils.Position;
import utils.RushHourGame;
import utils.SaveLoad;


public class Main {
    public static void main(String[] args) {
        SaveLoad system = new SaveLoad();
        String input = system.Load();

        // main program
        // testing
        RushHourGame game = new RushHourGame(input );
        game.displayBoard();
        Position p = new Position(game.getExitPosition().getRow(), game.getExitPosition().getCol());
        p.displayPosition();
        system.saveMatrixToTxt(game.getBoard(), "hasil1");

    }
}
