package nl.tudelft.jpacman.strategy;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.npc.ghost.Ghost;

import java.util.*;


/**
 * Created by Nicolas Leemans on 8/03/16.
 */
public class PacManhattanAI extends AIStrategy
{
    private Deque<Direction> directionQueue;//Queue containing the potential directions to follow
    private AStarPath pathAStar; //The path calculates with AStar
    private boolean[][] visitedCase;//List to know if the square is yet visited or not
    private static int GHOST_DST_THRESHOLD = 14;//The threshold distance between the player and a ghost
    /**
     * The default constructor
     *
     * @param game the current game
     */
    public PacManhattanAI(final Game game)
    {
        super(game);
        init(game);
    }

    /**
     * Initialise the data used to calculate the best movement to apply
     * @param game
     *      The current game
     */
    public void init(Game game)
    {
        visitedCase = new boolean[getBoard().getHeight()][getBoard().getWidth()];
        pathAStar = new AStarPath(game);
    }

    /**
     * Calculates the best move to apply according the data of the game (ghosts position, pellet nearest,...)
     * @return the best move
     */
    @Override
    public Direction nextMove()
    {
        directionQueue = new ArrayDeque<>();//Initialisation of the queue containing best moves
        boolean warning = false;//Boolean to know if a ghost is near of the player or not
        updatePacmanbehaviour(game.getLevel().remainingPellets());//Accelerate the endgame

        for(Ghost ghost : getGhostsList())
        {
            //Test if a ghost is near of the player
            double distance = AStarPath.manhattanDistance(getPlayer().getSquare().getX(), getPlayer().getSquare().getY(), ghost.getSquare().getX(), ghost.getSquare().getY());
            if (distance < GHOST_DST_THRESHOLD)
            {
                computePath(BFSNearestSafetySquare());
                warning = true;
                break;
            }
        }
        if(warning == false)
        {
            //There is no near ghost, thus find the nearest pellet
            computePath(BFSNearestSafetyPelletSquare());
        }

        if (!directionQueue.isEmpty())
        {
            //Apply the best move
            return directionQueue.removeFirst();
        }
        else
        {
            if(warning == true)
            {
                //No safe square found, find the nearest pellet
                computePath(BFSNearestSafetyPelletSquare());
                if(!directionQueue.isEmpty())
                {
                    return directionQueue.removeFirst();
                }
                else
                {
                    //No path found, find a other direction
                    return hurryMove();
                }
            }
            else
            {
                //No path found to a nearest pellet, find a safe square
                computePath( BFSNearestSafetySquare());
                if(!directionQueue.isEmpty())
                {
                    return directionQueue.removeFirst();
                }
                else
                {
                    //No path found, find a other direction
                    return hurryMove();
                }
            }
        }
    }

    /**
     * Compute a path
     * @param square the goal square
     */
    public void computePath(Square square)
    {
        if (square == null)
        {
            directionQueue = null;
        }

        pathAStar = new AStarPath(game);
        pathAStar.setGoal(square);

        List<Square> path = pathAStar.compute(getPlayer().getSquare());

        //Check if the path is safe
        if (pathAStar.getCost() > AStarPath.GHOST_COST)
        {
            path = null;
        }
        directionQueue = convertPathToDirection(path);
    }

    /**
     * Search the nearest safe square where there is a pellet on the board.
     * The search used is the Breadth First Search
     * @return the nearest safe square where there is a pellet, null if no safe pellet found
     */
    public Square BFSNearestSafetyPelletSquare()
    {
        for (int i = 0; i < getBoard().getHeight(); i++)
        {
            for(int j = 0; j < getBoard().getWidth(); j++)
            {
                visitedCase[i][j] = false;
            }
        }

        Queue<Square> squareQueue = new ArrayDeque<>();
        squareQueue.add(getPlayer().getSquare());
        if(getPlayer().getSquare() != null)
        {
            visitedCase[getPlayer().getSquare().getY()][getPlayer().getSquare().getX()] = true;
        }
        while (!squareQueue.isEmpty())
        {
            Square square = squareQueue.remove();
            if (square.getOccupants().size() > 0 && square.getOccupants().get(0) instanceof Pellet)
            {
                return square;
            }
            else
            {
                List<Square> neighborsList = getValidNeighbors(square);
                for(Square neighborSquare : neighborsList)
                {
                    if(neighborSquare != null)
                    {
                        if(!visitedCase[neighborSquare.getY()][neighborSquare.getX()])
                        {
                            squareQueue.add(neighborSquare);
                        }
                        visitedCase[neighborSquare.getY()][neighborSquare.getX()] = true;
                    }
                }
            }
        }
        return getPlayer().getSquare();
    }

    /**
     * Convert a square's list to know which direction the player must follow to get the nearest safe square
     * @param squaresList the square's list determining the path
     * @return a queue with each direction at each step
     */
    public Deque<Direction> convertPathToDirection(List<Square> squaresList)
    {

        Deque<Direction> directions = new ArrayDeque<>();

        //Direction can't calculate if path has not at least 2 squares
        if(squaresList == null || squaresList.size() < 2)
        {
            return directions;
        }
        for(int i = 1; i < squaresList.size(); ++i)
        {
            Square originSquare = squaresList.get(i-1);
            Square destinationSquare = squaresList.get(i);
            int x = destinationSquare.getX() - originSquare.getX();
            int y = destinationSquare.getY() - originSquare.getY();

            if(x == 0)
            {
                //Vertical situation
                if(y == 1 || y < -1)
                {
                    directions.add(Direction.SOUTH);
                }
                else
                {
                    directions.add(Direction.NORTH);
                }
            }
            else
            {
                //Horizontal situation
                if(x == 1 || x < -1)
                {
                    directions.add(Direction.EAST);
                }
                else
                {
                    directions.add(Direction.WEST);
                }
            }
        }
        return directions;
    }

    /**
     * Compute a BFS to know the nearest safety square
     * @return the nearest safety square, null if there is'nt
     */
    public Square BFSNearestSafetySquare()
    {
        for (int i = 0; i < getBoard().getHeight(); ++i)
        {
            for (int j = 0; j < getBoard().getWidth(); ++j)
            {
                visitedCase[i][j] = false;
            }
        }

        Queue<Square> squaresQueue = new ArrayDeque<>();
        squaresQueue.add(getPlayer().getSquare());

        while (!squaresQueue.isEmpty())
        {
            Square square = squaresQueue.remove();
            visitedCase[square.getY()][square.getX()] = true;
            if (isSafetySquare(square))
            {
                return square;
            }
            else
            {
                List<Square> neighborsList = getValidNeighbors(square);
                for (Square neighbor : neighborsList)
                {
                    if (!visitedCase[neighbor.getY()][neighbor.getX()])
                        squaresQueue.add(neighbor);
                }
            }
        }
        return getPlayer().getSquare();
    }

    /**
     * Get valid neighbors to visit
     * @param square A determined square
     * @return a new list with valid neighbor
     */
    public List<Square> getValidNeighbors(Square square)
    {
        List<Square> neighbors = square.getNeighbours();

        List<Square> validNeighbors = new ArrayList<>(neighbors);
        Iterator<Square> iterator = validNeighbors.iterator();

        while(iterator.hasNext())
        {
            Square neighbor = iterator.next();
            boolean invalidNeighbor = false;
            if(neighbor.isAccessibleTo(getPlayer()))
            {
                if(neighbor.getOccupants().size()==2)
                {
                    invalidNeighbor = neighbor.getOccupants().get(1) instanceof Ghost;
                }
                if(neighbor.getOccupants().size()==1)
                {
                    invalidNeighbor = neighbor.getOccupants().get(0) instanceof Ghost;
                }
            }
            else
            {
                invalidNeighbor = true;
            }
            if(invalidNeighbor)
            {
                iterator.remove();
            }
        }
        return validNeighbors;
    }
    /**
     * Determines if the square is safe
     * @param square the current square
     * @return true if the square is safe
     */
    public boolean isSafetySquare(Square square)
    {
        for (Ghost ghost : getGhostsList())
        {
            double distance = AStarPath.manhattanDistance(square.getX(), square.getY(), ghost.getSquare().getX(), ghost.getSquare().getY());
            if (distance < GHOST_DST_THRESHOLD)
            {
                return false;
            }
        }
        return true;
    }


    /**
     * FInd a not optimised direction in last resort (No best move found)
     * @return a not optimised direction
     */
    public Direction hurryMove()
    {
        if(getPlayer().getSquare().getSquareAt(getPlayer().getDirection()).isAccessibleTo(getPlayer()))
        {
            return getPlayer().getDirection();
        }
        else
        {
            if(getPlayer().getDirection() == Direction.WEST || getPlayer().getDirection() == Direction.EAST)
            {
                return Direction.SOUTH;
            }
            else
            {
                return Direction.EAST;
            }
        }
    }

    /**
     * Define the pacman Behavior in the game
     * @param pelletNbr the pellets number remaining in the game
     */
    private void updatePacmanbehaviour(int pelletNbr)
    {
        if(pelletNbr <= 30)
        {
            if(pelletNbr <= 7)
            {
                //Pacman must recover the last pellets to finish
                setGhostDstThreshold(3);
            }
            else
            {
                //The pacman play safety and recover pellets if he can
                setGhostDstThreshold(6);
            }
        }
        else
        {
            //Pacman play safety
            setGhostDstThreshold(14);
        }
    }


    /**
     * No Strategy to execute for the AI
     */
    @Override
    public void executeStrategy() {}

    /**
     * Get the GHOST_DST_THRESHOLD
     * @return GHOST_DST_THRESHOLD
     */
    public int getGhostDstThreshold()
    {
        return GHOST_DST_THRESHOLD;
    }

    /**
     * Set the GHOST_DST_THRESHOLD
     * @param ghostDst the new GHOST_DST_THRESHOLD
     */
    public void setGhostDstThreshold(int ghostDst)
    {
        GHOST_DST_THRESHOLD = ghostDst;
    }

}

