package sudokuSolver.panels;

import sudokuSolver.Main;
import sudokuSolver.models.Difficulty;
import sudokuSolver.models.MultipleSolutionsException;
import sudokuSolver.models.NoSolutionsException;
import sudokuSolver.models.Sudoku;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class MainPanel extends JPanel implements ActionListener {

    static final long serialVersionUID = 42L;

    public Sudoku sudoku;
    public Difficulty currentLevel = Difficulty.RANDOM;

    private JFrame parent;
    private SudokuPanel sudokuPanel;
    private ButtonPanel buttonPanel;
    static private JPanel loadingPanel;

    public MainPanel(JFrame parent) {
        //set variables
        buttonPanel = new ButtonPanel(this);
        sudokuPanel = new SudokuPanel();
        this.parent = parent;

        setPreferredSize(new Dimension(600, 700));
        setLayout(new BorderLayout());
        setBackground(Color.red);

        //Layout and borders
        sudokuPanel.setBorder(new EmptyBorder(50, 70, 50, 70)); //top, left, bottom, right
        buttonPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        add(BorderLayout.CENTER, sudokuPanel);
        add(BorderLayout.SOUTH, buttonPanel);

        createLoadingPanel();
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
                    return;
                } else if (options[successPanel] == "Next puzzle") {
                    getNext();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "There is a mistake somewhere.");
            }


        }
        else if (event.getSource() == buttonPanel.solveButton) {
            startLoading();
            sudoku = sudokuPanel.readScreen();

            Sudoku solution = getSolution(sudoku);
            if (solution != null) {
                this.sudokuPanel.setScreen(solution);
            }
            finishLoading();
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
        startLoading();
        this.sudokuPanel.setScreen(randomPuzzle(currentLevel));
        finishLoading();
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

    public void createLoadingPanel() {
        loadingPanel = (JPanel) parent.getGlassPane();
        loadingPanel.setLayout(new BorderLayout());
        loadingPanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                e.consume();
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                e.consume();
            }
        });

        int transparency = 200;
        ImageIcon image = new ImageIcon(Main.class.getClassLoader().getResource("gif/sliding-squares-2.gif"));

        JLabel label = new JLabel(image) {
            protected void paintComponent(Graphics g)
            {
                g.setColor( new Color(255,255,255,transparency) );
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);

        loadingPanel.add(label);
    }

    private void startLoading() {
        System.out.println("------- start loading --------");
        loadingPanel.setVisible(true);
    }

    private void finishLoading() {
        loadingPanel.setVisible(false);
        System.out.println("------- finish loading --------");
    }
}