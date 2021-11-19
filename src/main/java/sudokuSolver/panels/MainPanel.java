package sudokuSolver.panels;

import sudokuSolver.models.Difficulty;
import sudokuSolver.models.MultipleSolutionsException;
import sudokuSolver.models.NoSolutionsException;
import sudokuSolver.models.Sudoku;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MainPanel extends JPanel implements ActionListener {

    static final long serialVersionUID = 42L;

    public Sudoku sudoku;
    public Difficulty currentLevel = Difficulty.EASY;

    private JPanel contentPanel;
    private SudokuPanel sudokuPanel;
    private ButtonPanel buttonPanel;

    public MainPanel() {
        //set variables
        buttonPanel = new ButtonPanel(this);
        sudokuPanel = new SudokuPanel();

        setPreferredSize(new Dimension(600, 700));
        setLayout(new BorderLayout());
        setBackground(Color.red);

        //Layout and borders
        sudokuPanel.setBorder(new EmptyBorder(50, 70, 50, 70)); //top, left, bottom, right
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        add(BorderLayout.CENTER, sudokuPanel);
        add(BorderLayout.SOUTH, buttonPanel);

    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonPanel.checkButton) {
            sudoku = sudokuPanel.readScreen();

            if (!sudoku.isFull()) {
                JOptionPane.showMessageDialog(this,
                        "The puzzle is not complete.");
            } else if (sudoku.isSolved()) {
                String[] options = {"Next puzzle"};
                int successPanel = JOptionPane.showOptionDialog(this,
                        "Congratulations, you have solved this puzzle!",
                        "Check solution",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (successPanel == JOptionPane.DEFAULT_OPTION) {
                    this.sudokuPanel.setScreen(randomPuzzle(currentLevel));
                } else if (options[successPanel] == "Next puzzle") {
                    getNext();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "There is a mistake somewhere.");
            }


        }
        else if (event.getSource() == buttonPanel.solveButton) {
            sudoku = sudokuPanel.readScreen();

            Sudoku solution = getSolution(sudoku);
            if (solution != null) {
                this.sudokuPanel.setScreen(solution);
            }
        }
        else if (event.getSource() == buttonPanel.nextButton) {
            getNext();
        }
        else if ( Arrays.asList(buttonPanel.difficultyButtons).contains(event.getSource()) ) {
            Difficulty level = Difficulty.values()[Arrays.asList(buttonPanel.difficultyButtons).indexOf(event.getSource())];
            currentLevel = level;

            for (Difficulty buttonLevel : Difficulty.values()) {
                buttonPanel.difficultyButtons[buttonLevel.ordinal()].setBackground(Color.GRAY);
                buttonPanel.difficultyButtons[buttonLevel.ordinal()].setForeground(Color.BLACK);
            }
            buttonPanel.difficultyButtons[level.ordinal()].setBackground(Color.BLUE);
            buttonPanel.difficultyButtons[level.ordinal()].setForeground(Color.GRAY);
        }

    }

    public void getNext() {
        this.sudokuPanel.setScreen(randomPuzzle(currentLevel));
    }

    public Sudoku getSolution(Sudoku sudoku) {
        try {
            return sudoku.getSolution();
        } catch (NoSolutionsException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "There are no solutions.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } catch (MultipleSolutionsException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "There is more than one solution.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    public static Sudoku randomPuzzle(Difficulty level) {
        try {
            Sudoku sudoku;
            if (level == Difficulty.RANDOM){
                sudoku = Sudoku.randomPuzzle();
                sudoku = sudoku.minimise(level);
            } else {
                Difficulty difficulty;
                do {
                    sudoku = Sudoku.randomPuzzle();
                    sudoku = sudoku.minimise(level);

                    difficulty = sudoku.getDifficulty();
                    System.out.println("Difficulty: " + difficulty);
                } while (difficulty != level);
            }
            return sudoku;
        } catch (NoSolutionsException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "There are no solutions.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }
}