package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.strategy.HumanControllerStrategy;
import nl.tudelft.jpacman.strategy.PacManhattanAI;
import nl.tudelft.jpacman.strategy.PacmanStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nicolas Leemans on 10/03/16.
 */
public class MyJDialogStrategy extends JDialog
{
    /**
     * Button of the window
     */
    private JButton HumanController, AIController;
    /**
     * The chose strategy by the player
     */
    private PacmanStrategy strategy;
    /**
     * The game
     */
    private final Game game;
    /**
     * The builder
     */
    private final PacManUiBuilder builder;
    private PacManUI pacManUI;

    /**
     * Create a new window to chose the game mode (strategy,...)
     * @param parent a JFrame parent
     * @param title a window title
     * @param message a message to add for the window
     * @param builder the builder
     * @param game the game
     * @param pacManUI the pacManUI
     */
    public MyJDialogStrategy(JFrame parent, String title, String message, PacManUiBuilder builder, Game game, PacManUI pacManUI)
    {
        super(parent, title);
        this.builder = builder;
        this.game = game;
        this.pacManUI = pacManUI;
        // set the position of the window
        Point p = new Point(100, 100);
        setLocation(p.x, p.y);

        // Create a message
        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel(message));
        // get content pane, which is usually the
        // Container of all the dialog's components.
        getContentPane().add(messagePane);

        // Create a button
        JPanel buttonPane = new JPanel();
        HumanController = new JButton("Control Pacman");
        AIController = new JButton("Be spectator");
        buttonPane.add(HumanController);
        buttonPane.add(AIController);
        // set action listener on the button
        HumanController.addActionListener(new MyActionListener());
        AIController.addActionListener(new MyActionListener());

        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }


    /**
     * override the createRootPane inherited by the JDialog, to create the rootPane.
     * create functionality to close the window when "Escape" button is pressed
     * @return the root pane
     */
    public JRootPane createRootPane()
    {
        JRootPane rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        AbstractAction action = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        };
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(stroke, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", action);
        return rootPane;
    }

    /**
     *  An action listener to be used when an action is performed
     *  (e.g. button is pressed)
     */
    class MyActionListener implements ActionListener
    {

        //close and dispose of the window.
        public void actionPerformed(ActionEvent e)
        {
            Object source = e.getSource();
            if(source == HumanController)
            {
                strategy= new HumanControllerStrategy(game,builder);
                System.out.println("The chosen strategy is : " + strategy.getTypeStrategy());
            }
            else if(source == AIController)
            {
                strategy = new PacManhattanAI(game);
                System.out.println("The chosen strategy is : " + strategy.getTypeStrategy());
            }
            game.getLevel().setStrategy(strategy);
            setVisible(false);
            dispose();
            pacManUI.start();
        }
    }
}
