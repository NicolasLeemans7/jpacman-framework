package nl.tudelft.jpacman.strategy;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.ui.PacManUiBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * Created by Nicolas Leemans on 2/03/16.
 */


/**
 * Human strategy test in the game
 */
public class HumanControllerStrategyTest
{
    private Launcher launcher;


    @Before
    public void setUpPacman()
    {
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
     * Attribution of keys for the player to move the Pacman in the game
     * @throws AWTException
     * @throws InterruptedException
     */
    @SuppressWarnings("methodlength")
    @Test
    public void StrategyTest() throws AWTException, InterruptedException {
        Game game = launcher.getGame();
        Player player = game.getPlayers().get(0);
        assertFalse(game.isInProgress());
        assertNotNull(game);
        PacManUiBuilder builder = new PacManUiBuilder().withDefaultButtons();
        PacmanStrategy strategy = new HumanControllerStrategy(game, builder);

        assertTrue(strategy.getTypeStrategy() == PacmanStrategy.Type.PLAYER);
        assertNotNull(strategy);

        strategy.executeStrategy();

        assertFalse(game.isInProgress());
        game.start();
        assertTrue(game.isInProgress());

        //Score initial
        assertEquals(0, player.getScore());
    }
}
