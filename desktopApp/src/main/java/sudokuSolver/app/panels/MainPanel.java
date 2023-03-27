package sudokuSolver.app.panels;

import sudokuSolver.app.Main;
import sudokuSolver.app.models.Difficulty;
import sudokuSolver.app.models.MultipleSolutionsException;
import sudokuSolver.app.models.NoSolutionsException;
import sudokuSolver.app.models.Sudoku;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Objects;

public class MainPanel extends JPanel implements ActionListener {

    static final long serialVersionUID = 42L;

    protected Difficulty currentLevel = Difficulty.RANDOM;

    private final JFrame parent;
    private final SudokuPanel sudokuPanel;
    private final ButtonPanel buttonPanel;
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
        Sudoku sudoku;
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
                } else if (Objects.equals(options[successPanel], "Next puzzle")) {
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

    private Sudoku getSolution(Sudoku sudoku) {
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

    private static Sudoku randomPuzzle(Difficulty level) {
        try {
            return Sudoku.newPuzzle(level);
        } catch (NoSolutionsException e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "There are no solutions.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    private void createLoadingPanel() {
//        TODO: Use a separate thread for the loading animation, so it can run at the same time as calculations.
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
        ImageIcon image = new ImageIcon(Objects.requireNonNull(Main.class.getClassLoader().getResource("gif/sliding-squares-2.gif")));

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
        loadingPanel.setVisible(true);
    }

    private void finishLoading() {
        loadingPanel.setVisible(false);
    }
}