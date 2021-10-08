package sudokuSolver.panels;

import sudokuSolver.models.Difficulty;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
    public JButton checkButton, solveButton, nextButton;
    public JButton[] difficultyButtons = new JButton[Difficulty.values().length];

    public ButtonPanel(MainPanel parent) {
        setLayout(new GridLayout(2, 1));

        JPanel normalOptions = new JPanel();
        JPanel difficultyPanel = new JPanel();
        add(normalOptions);
        add(difficultyPanel);

        checkButton = new JButton("Check solution");
        solveButton = new JButton("Solve");
        nextButton = new JButton("Next puzzle");

        normalOptions.add(checkButton);
        normalOptions.add(solveButton);
        normalOptions.add(nextButton);

        checkButton.addActionListener(parent);
        solveButton.addActionListener(parent);
        nextButton.addActionListener(parent);

        for (Difficulty level : Difficulty.values()) {
            difficultyButtons[level.ordinal()] = new JButton(level.toString());
            difficultyPanel.add(difficultyButtons[level.ordinal()]);
            difficultyButtons[level.ordinal()].addActionListener(parent);
        }

    }
}
