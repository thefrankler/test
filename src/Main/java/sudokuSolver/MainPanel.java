package sudokuSolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {

    static final long serialVersionUID = 42L;

    private SudokuPanel sudokuPanel;
    private ButtonPanel buttonPanel;
    public Sudoku sudoku;

    public MainPanel() {

        //set variables
        buttonPanel = new ButtonPanel(this);
        sudokuPanel = new SudokuPanel();

        //Layout and borders
        setLayout(new BorderLayout());
        sudokuPanel.setBorder(new EmptyBorder(50, 70, 50, 70)); //top, left, bottom, right
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        this.add(BorderLayout.CENTER, sudokuPanel);
        this.add(BorderLayout.SOUTH, buttonPanel);

    }

    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == buttonPanel.checkButton) {
            sudoku = sudokuPanel.readScreen();

            if (sudoku.isSolved()) {
                String[] options = {"Next puzzle"};
                int successPanel = JOptionPane.showOptionDialog(this,
                        "Congratulations, you have solved this puzzle!",
                        "Check solution",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        null);
                if (successPanel == JOptionPane.DEFAULT_OPTION) {
                    this.sudokuPanel.setScreen(Sudoku.randomPuzzle());
                }
                // TODO other options
            } else {
                JOptionPane.showMessageDialog(this,
                        "There is a mistake somewhere.");
            }


        } else if (event.getSource() == buttonPanel.solveButton) {
            sudoku = sudokuPanel.readScreen();

            Sudoku solution = sudoku.solve();
            if(solution != null) {
                this.sudokuPanel.setScreen(solution);
            }

        } else if (event.getSource() == buttonPanel.nextButton) {
            this.sudokuPanel.setScreen(Sudoku.randomPuzzle());
        }

    }

}