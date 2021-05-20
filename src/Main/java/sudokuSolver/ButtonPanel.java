package sudokuSolver;

import javax.swing.*;

public class ButtonPanel extends JPanel {
    public JButton checkButton, solveButton, nextButton;

    public ButtonPanel(MainPanel parent) {
        checkButton = new JButton("Check solution");
        solveButton = new JButton("Solve");
        nextButton = new JButton("Next puzzle");

        add(checkButton);
        add(solveButton);
        add(nextButton);

        //add action listeners
        checkButton.addActionListener(parent);
        solveButton.addActionListener(parent);
        nextButton.addActionListener(parent);
    }

}
