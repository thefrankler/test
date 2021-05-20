package sudokuSolver;

import javax.swing.*;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class Sudoku {

    private Cell[][] cells = new Cell[9][9];

    public Sudoku() {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            cells[row][column] = new Cell(row,column);;
        }
    }

    public Sudoku(int[][] puzzle) {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            cells[row][column] = new Cell(row,column).setValue(puzzle[row][column]);
        }
    }

    public Sudoku(Cell[][] cells) {
        this.cells = cells;
    }

    public Cell getCell(int row, int column) {
        return cells[row][column];
    }

    private CellSet getRow(int rowIndex) {
        CellSet row = new CellSet();
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            row.addCell(cells[rowIndex][columnIndex], columnIndex);
        }
        return row;
    }

    private CellSet getColumn(int columnIndex) {
        CellSet column = new CellSet();
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            column.addCell(cells[rowIndex][columnIndex], rowIndex);
        }
        return column;
    }

    private CellSet getBox(int boxRow, int boxColumn) {
        if (boxRow < 0 || boxRow >= 3) {
            throw new IllegalArgumentException("Box row must be an integer between 0 and 2, received " + boxRow);
        }
        if (boxColumn < 0 || boxColumn >= 3) {
            throw new IllegalArgumentException("Box column must be an integer between 0 and 2, received " + boxColumn);
        }

        CellSet box = new CellSet();
        for (int index = 0; index < 9; index++) {
            box.addCell( cells[index/3 + 3 * boxRow][index%3 + 3 * boxColumn], index);
        }
        return box;
    }

    private boolean isFull() {
        for (int row = 0; row < 9; row++) for (int column = 1; column < 9; column++) {
            if (cells[row][column].isEmpty()) { return false; }
        }
        return true;
    }

    public boolean isSolved() {
        if (!this.isFull()) { return false; }

        // check rows and columns
        for (int i = 0; i < 9; i++) {
            if (!getRow(i).isSolved()) { return false; }
            if (!getColumn(i).isSolved()) { return false; }
        }

        //check boxes
        for (int row = 0; row < 3; row++) for (int column = 0; column < 3; column++) {
            if (!getBox(row, column).isSolved()) { return false; }
        }

        return true;
    }

    public Sudoku solve() {
        Stack<Sudoku> options = new Stack<>();
        HashSet<Sudoku> solutions = new HashSet<>();
        int result;

        if (this.isFull()) {
            if (this.isSolved()) return this;
        } else {
            Sudoku newSudoku = new Sudoku(this.cells);
            options.push(newSudoku);

            while (options.size() > 0 && solutions.size()<2) {
                Sudoku sudoku = options.pop();
                int numOptions = 0;
                while (numOptions < 10) {  //checkButton for cells with i options
                    for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {  // inefficient, could save all values of cellOptions first time around
                        if (sudoku.cells[row][column].isEmpty()) {
                            Vector<Integer> optionsVector = sudoku.cellOptions(row, column);
                            if (optionsVector.size() == numOptions) {
                                for (int i = 0; i < numOptions; i++) {
                                    sudoku.cells[row][column].setValue(optionsVector.get(i));
                                    if (sudoku.isSolved()) {
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

        if (this.isFull()) {
            if (this.isSolved()) return 1;
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
                        if (sudoku.cells[row][column].isEmpty()) {
                            Vector<Integer> optionsVector = sudoku.cellOptions(row, column);
                            if (optionsVector.size() == numOptions) {
                                for (int i = 0; i < numOptions; i++) {
                                    sudoku.cells[row][column].setValue(optionsVector.get(i));
                                    if (sudoku.isSolved()) {
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

    public Vector<Integer> cellOptions(int rowIndex, int columnIndex) {
        int[] isDigitForbidden = new int[9];
        Vector<Integer> options = new Vector<>();

        //checkButton row
        CellSet row = getRow(rowIndex);
        CellSet column = getColumn(columnIndex);
        CellSet box = getBox(rowIndex / 3, columnIndex / 3);
        for (int digit = 0; digit < 9; digit++) {
            if (!row.getCell(digit).isEmpty()) {
                isDigitForbidden[row.getCell(digit).getValue() - 1] = 1;
            }
            if (!column.getCell(digit).isEmpty()) {
                isDigitForbidden[column.getCell(digit).getValue() - 1] = 1;
            }
            if (!box.getCell(digit).isEmpty()) {
                isDigitForbidden[box.getCell(digit).getValue() - 1] = 1;
            }
        }

        //checkButton list
        for (int digit=0; digit<9; digit++){
            if (isDigitForbidden[digit] == 0){
                if (!options.contains(digit+1)) {
                    options.add(digit + 1);
                }
            }
        }
        return options;
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                string.append(this.cells[row][column].getValue()).append("  ");
            }
            string.append(System.lineSeparator());
        }
        return string.toString();
    }

}
