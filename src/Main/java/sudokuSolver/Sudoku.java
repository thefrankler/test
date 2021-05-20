package sudokuSolver;

import javax.swing.*;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class Sudoku {

    public int[][] cells = new int[9][9];

    public Sudoku() {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            cells[row][column] = 0;
        }
    }

    public Sudoku(int[][] grid) {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            cells[row][column] = grid[row][column];
        }
    }

    @Override
    public String toString()
    {
        String string = "";
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                string = string + this.cells[row][column] + "  ";
            }
            string = string + System.lineSeparator();
        }
        return string;
    }

    public int[] getRow(int row) {
        int[] rowCells = new int[9];
        for (int column = 0; column < 9; column++) {
            rowCells[column] = cells[row][column];
        }
        return rowCells;
    }

    public int[] getColumn(int column) {
        int[] columnCells = new int[9];
        for (int row = 0; row < 9; row++) {
            columnCells[row] = cells[row][column];
        }
        return columnCells;
    }

    public int[] getBox(int x, int y) {
        int[] boxCells = new int[9];
        boxCells[0] = cells[0 + 3 * x][0 + 3 * y];
        boxCells[1] = cells[0 + 3 * x][1 + 3 * y];
        boxCells[2] = cells[0 + 3 * x][2 + 3 * y];
        boxCells[3] = cells[1 + 3 * x][0 + 3 * y];
        boxCells[4] = cells[1 + 3 * x][1 + 3 * y];
        boxCells[5] = cells[1 + 3 * x][2 + 3 * y];
        boxCells[6] = cells[2 + 3 * x][0 + 3 * y];
        boxCells[7] = cells[2 + 3 * x][1 + 3 * y];
        boxCells[8] = cells[2 + 3 * x][2 + 3 * y];
        return boxCells;
    }

    private boolean checkFull() {
        for (int row = 0; row < 9; row++) for (int column = 1; column < 9; column++) {
            if (cells[row][column] == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSolution() {
        //checkButton complete
        for (int row = 0; row < 9; row++) for (int column = 1; column < 9; column++) {
            if (cells[row][column] == 0) {
                return false;
            }
        }

        //checkButton rows
        for (int row = 0; row < 9; row++) {
            int[] rowCells = getRow(row);
            for (int column = 1; column < 9; column++) for (int columnIndex = 0; columnIndex < column; columnIndex++) {
                if (rowCells[column] == rowCells[columnIndex]) {
                    return false;
                }
            }
        }

        //checkButton columns
        for (int column = 0; column < 9; column++) {
            int[] columnCells = getColumn(column);
            for (int row = 1; row < 9; row++) for (int rowIndex = 0; rowIndex < row; rowIndex++) {
                if (columnCells[row] == columnCells[rowIndex]) {
                    return false;
                }
            }
        }

        //checkButton boxes
        for (int row = 0; row < 3; row++) for (int column = 0; column < 3; column++) {
            int[] boxCells = getBox(row, column);
            for (int digit = 1; digit < 9; digit++) for (int checkDigit = 0; checkDigit < digit; checkDigit++) {
                if (boxCells[digit] == boxCells[checkDigit]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Sudoku complete() {
        Stack<Sudoku> options = new Stack<>();
        HashSet<Sudoku> solutions = new HashSet<>();
        int result;

        if (this.checkFull()) {
            if (this.checkSolution()) return this;
        } else {
            Sudoku newSudoku = new Sudoku(this.cells);
            options.push(newSudoku);

            while (options.size() > 0 && solutions.size()<2) {
                Sudoku sudoku = options.pop();
                int numOptions = 0;
                while (numOptions < 10) {  //checkButton for cells with i options
                    for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {  // inefficient, could save all values of cellOptions first time around
                        if (sudoku.cells[row][column] == 0) {
                            Vector<Integer> optionsVector = sudoku.cellOptions(row, column);
                            if (optionsVector.size() == numOptions) {
                                for (int i = 0; i < numOptions; i++) {
                                    sudoku.cells[row][column] = optionsVector.get(i);
                                    if (sudoku.checkSolution()) {
                                        Sudoku temp = new Sudoku(sudoku.cells);
                                        solutions.add(temp);
                                    } else options.push(new Sudoku(sudoku.cells));
                                }
                                numOptions=100;
                            }
                        }
                    }
                    numOptions++;
                }
            }

            int solutionsSize = solutions.size();
//            System.out.println("number of solutions = "+Integer.toString(n));
            if (solutionsSize>=2) result = 2;
            else if (solutionsSize == 1) result = 1;
            else result = 0;

            if (result==0) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "There are no solutions.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else if (result>1) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "There is more than one solution.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else { // result == 1
                return solutions.iterator().next();
            }
        }
        return null;
    }

    public int checkUniqueSolution() //0 = no solutions, 1 = 1 solution, 2 = more solutions
    {   Stack<Sudoku> options = new Stack<>();
        HashSet<Sudoku> solutions = new HashSet<>();

        if (this.checkFull()) {
            if (this.checkSolution()) return 1;
            else return 0;
        } else {
            Sudoku newSudoku = new Sudoku(this.cells);
            options.push(newSudoku);

            while (options.size() > 0) {
                Sudoku sudoku = options.pop();
                int numOptions = 0;
                Main:
                while (numOptions < 10) {  //checkButton for cells with i options
                    for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {  // inefficient, could save all values of cellOptions first time around
                        if (sudoku.cells[row][column] == 0) {
                            Vector<Integer> optionsVector = sudoku.cellOptions(row, column);
                            if (optionsVector.size() == numOptions) {
                                for (int i = 0; i < numOptions; i++) {
                                    sudoku.cells[row][column] = optionsVector.get(i);
                                    if (sudoku.checkSolution()) {
                                        Sudoku temp = new Sudoku(sudoku.cells);
                                        solutions.add(temp);
                                        if (solutions.size() >=2) return 2; // remove if you want more than 2 solutions.
                                    } else options.push(new Sudoku(sudoku.cells));
                                }
                                break Main;
                            }
                        }
                    }
                    numOptions++;
                }
            }

            int numSolutions = solutions.size();
//            System.out.println("number of solutions = "+Integer.toString(n));
            if (numSolutions>2) numSolutions = 2;
            return numSolutions;
        }
    }

    public Vector<Integer> cellOptions(int row, int column) {
        int[] list = new int[9];
        Vector<Integer> options = new Vector<>();

        //checkButton row
        int[] rowCells = getRow(row);
        for (int digit = 0; digit < 9; digit++) {
            if (rowCells[digit] != 0) {
                list[rowCells[digit] - 1] = 1;
            }
        }

        //checkButton column
        int[] columnCells = getColumn(column);
        for (int digit = 0; digit < 9; digit++) {
            if (columnCells[digit] != 0) {
                list[columnCells[digit] - 1] = 1;
            }
        }

        //checkButton box
        int[] boxCells = getBox(row / 3, column / 3);
        for (int digit = 0; digit < 9; digit++) {
            if (boxCells[digit] != 0) {
                list[boxCells[digit] - 1] = 1;
            }
        }

        //checkButton list
        for (int digit=0; digit<9; digit++){
            if (list[digit] == 0){
                if (!options.contains(digit+1)) {
                    options.add(digit + 1);
                }
            }
        }
        return options;
    }

}
