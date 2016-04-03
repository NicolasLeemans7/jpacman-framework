package nl.tudelft.jpacman.strategy;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nicolas Leemans on 10/03/16.
 */


/**
 * Class to test coordinates of a square in the board
 */
public class SquareCoordinate {
    private Launcher launcher;

    /**
     * Launch the user interface.
     */
    @Before
    public void setUpPacman() {
        launcher = new Launcher();
        launcher.launch();
    }

    /**
     * Quit the user interface when we're done.
     */
    @After
    public void tearDown() {
        launcher.dispose();
    }

    /**
     * Launch the game, and imitate what would happen in a typical game.
     * The test is only a smoke test, and not a focused small test.
     * Therefore it is OK that the method is a bit too long.
     *
     * @throws InterruptedException Since we're sleeping in this test.
     */
    @SuppressWarnings("methodlength")
    @Test
    public void SquareCoordinateTest1()
    {
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);

        Square square = player.getSquare();

        assertEquals(square.getX(), 11);
        assertEquals(square.getY(), 15);

        assertEquals(square.getSquareAt(Direction.NORTH).getX(), 11);
        assertEquals(square.getSquareAt(Direction.NORTH).getY(), 14);

        assertEquals(square.getSquareAt(Direction.WEST).getX(), 10);
        assertEquals(square.getSquareAt(Direction.WEST).getY(), 15);

        assertEquals(square.getSquareAt(Direction.SOUTH).getX(), 11);
        assertEquals(square.getSquareAt(Direction.SOUTH).getY(), 16);

        assertEquals(square.getSquareAt(Direction.EAST).getX(), 12);
        assertEquals(square.getSquareAt(Direction.EAST).getY(), 15);

        assertEquals(square.getX(), 11);
        assertEquals(square.getY(), 15);

        square.setX(20);
        square.setY(25);

        assertEquals(square.getX(), 20);
        assertEquals(square.getY(), 25);

    }
}
