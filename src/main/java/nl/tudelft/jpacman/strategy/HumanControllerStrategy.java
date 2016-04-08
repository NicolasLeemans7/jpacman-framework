package nl.tudelft.jpacman.strategy;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.ui.Action;
import nl.tudelft.jpacman.ui.PacManUiBuilder;
import java.awt.event.KeyEvent;


/**
 * Created by Nicolas Leemans on 2/03/16.
 */
public class HumanControllerStrategy extends PacmanStrategy
{
    private PacManUiBuilder builder;

    /**
     * Default constructor
     * @param game the current game
     */
    public HumanControllerStrategy(Game game, PacManUiBuilder builder)
    {
        super(game);
        this.game = game;
        this.builder = builder;
    }

    /**
     * Return the type of the strategy
     * @return PLAYER strategy
     */
    @Override
    public Type getTypeStrategy()
    {
        return Type.PLAYER;
    }


    /**
     * Adds key events UP, DOWN, LEFT and RIGHT to a game.
     *
     * @param builder
     *            The {@link PacManUiBuilder} that will provide the UI.
     * @param game
     *            The game that will process the events.
     */
    public void addSinglePlayerKeys(final PacManUiBuilder builder, final Game game)
    {
        final Player p1 = game.getPlayers().get(0);
        builder.addKey(KeyEvent.VK_UP, new Action()
        {

            @Override
            public void doAction()
            {
                game.continousMovement(p1, Direction.NORTH);

            }
        }).addKey(KeyEvent.VK_DOWN, new Action()
        {

            @Override
            public void doAction()
            {
                game.continousMovement(p1, Direction.SOUTH);
            }
        }).addKey(KeyEvent.VK_LEFT, new Action()
        {

            @Override
            public void doAction()
            {
                game.continousMovement(p1, Direction.WEST);
            }
        }).addKey(KeyEvent.VK_RIGHT, new Action()
        {

            @Override
            public void doAction()
            {
                game.continousMovement(p1, Direction.EAST);
            }
        });

    }

    /**
     * Return the next move to apply
     * @return null
     */
    @Override
    public Direction nextMove()
    {
        return null;
    }


    /**
     * Add keys to the player
     */
    public void executeStrategy()
    {
        addSinglePlayerKeys(builder,game);
    }
}
