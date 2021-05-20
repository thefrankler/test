package sudokuSolver;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class SudokuPanel extends JPanel {
    public JFormattedTextField[][] gridCells;

    public SudokuPanel() {
        setPreferredSize(new Dimension(400, 400));

        //set variables
        gridCells = new JFormattedTextField[9][9];
        for (int row=0; row<9; row++) for (int column=0; column<9; column++){
            NumberFormatter numberFormatter = new NumberFormatter();
            numberFormatter.setAllowsInvalid(true);
            gridCells[row][column] = new JFormattedTextField(numberFormatter);
            gridCells[row][column].setHorizontalAlignment(JTextField.CENTER);
        }

        //set layout and borders
        setLayout(new GridLayout(9,9,0,0));

        int topBorder, bottomBorder, leftBorder, rightBorder;
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            if (row == 0) {
                topBorder = 2;
            } else {
                topBorder = 0;
            }
            if (row == 8 || row == 5 || row == 2) {
                bottomBorder = 2;
            } else {
                bottomBorder = 1;
            }
            if (column == 0) {
                leftBorder = 2;
            } else {
                leftBorder = 0;
            }
            if (column == 8 || column == 5 || column == 2) {
                rightBorder = 2;
            } else {
                rightBorder = 1;
            }
            gridCells[row][column].setBorder(BorderFactory.createMatteBorder(topBorder, leftBorder, bottomBorder, rightBorder, Color.BLACK));
        }

        //add fields
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            add(gridCells[row][column]);
        }

        setScreen(new Sudoku());
    }

    public Sudoku randomPuzzle() {
        Sudoku sudoku = randomSolution();
        Random rand = new Random();

        //make list of unchecked cells
        ArrayList<int[]> cells = new ArrayList<>();
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            cells.add(new int[] {row,column});
        }

        long start = System.currentTimeMillis();
        long finish = start + 30*1000; // 30 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < finish && cells.size()>0) {

            //randomly select a gridCells to checkButton
            int index = rand.nextInt(cells.size());
            int row = cells.get(index)[0];
            int column = cells.get(index)[1];

            int temp = sudoku.cells[row][column];
            sudoku.cells[row][column] = 0;

            if (sudoku.checkUniqueSolution() > 1) {
                sudoku.cells[row][column] = temp;
            }

            cells.remove(index);
        }
        return sudoku;
    }

    public Sudoku readScreen(){
        int[][] sudoku = new int[9][9];
            for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
                try {
                    sudoku[row][column] = Integer.parseInt(gridCells[row][column].getText());
                } catch(Exception e){
                    sudoku[row][column] = 0;
                }

                if (sudoku[row][column] > 9 || sudoku[row][column] < 1){
                    sudoku[row][column] = 0;
                }
            }
        return new Sudoku(sudoku);
    }

    public void setScreen(Sudoku sudoku){
        for (int row=0; row<9; row++) for(int column=0; column<9; column++){
            if (sudoku.cells[row][column] == 0) {
                gridCells[row][column].setText("");
            } else {
                gridCells[row][column].setText(Integer.toString(sudoku.cells[row][column]));
            }
        }
    }

    public Sudoku randomEasyPuzzle() {
        Sudoku sudoku = randomSolution();
        Random rand = new Random();

        boolean end = true;
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            if (sudoku.cells[row][column] != 0 && sudoku.cellOptions(row,column).size() == 0) { // if there is a nonempty gridCells with options open
                end = false;
            }
        }

        while (!end) {
            //randomly delete unnecessary cells
            int index = rand.nextInt(81);
            if (sudoku.cells[index / 9][index % 9] != 0) {
                Vector<Integer> options = sudoku.cellOptions(index / 9, index % 9);
                int size = options.size();
                if (size == 0) {
                    sudoku.cells[index / 9][index % 9] = 0;
                }
            }

            end = true;
            for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
                if (sudoku.cells[row][column] != 0 && sudoku.cellOptions(row,column).size() == 0) {
                    end = false;
                }
            }
        }

        return sudoku;
    }

    private Sudoku randomSolution() {
        //create blank sudoku
        Sudoku sudoku = new Sudoku();
        Random rand = new Random();
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 10 seconds * 1000 ms/sec

        while (System.currentTimeMillis() < end) {
            //fill a gridCells randomly
            int cell = rand.nextInt(81);
            if (sudoku.cells[cell / 9][cell % 9] == 0) {
                Vector<Integer> options = sudoku.cellOptions(cell/9,cell%9);
                int size = options.size();

                if (size == 0) { // no options for gridCells, remove random gridCells
                    int index = rand.nextInt(81);
                    sudoku.cells[index / 9][index % 9] = 0;
                } else {
                    int digit = rand.nextInt(size);
                    sudoku.cells[cell / 9][cell % 9] = options.get(digit);
                }

                int check = sudoku.checkUniqueSolution();
                if (check == 1) { // unique solution, output the puzzle
                    return sudoku.complete();
                } // else there are multiple solutions, so continue
            }
        }
        return new Sudoku();
    }

}
