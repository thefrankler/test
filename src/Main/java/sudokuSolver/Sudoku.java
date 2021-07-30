package sudokuSolver;

import javax.swing.*;
import java.util.*;

public class Sudoku {

    private Cell[][] cells = new Cell[9][9];
    public Sudoku solution;

    public Sudoku() {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            cells[row][column] = new Cell(row,column);
        }
    }

    public Sudoku(int[][] puzzle) {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            if (puzzle[row][column] == 0) {
                cells[row][column] = new Cell(row,column);
            } else {
                cells[row][column] = new Cell(row, column).setValue(puzzle[row][column]);
            }
        }
    }

    public Sudoku clone() {
        Sudoku newSudoku = new Sudoku();
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            if (!this.getCell(row, column).isEmpty()) {
                newSudoku.getCell(row, column).setValue(this.getCell(row, column).getValue());
            }
        }
        newSudoku.solution = this.solution;
        return newSudoku;
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

    public CellSet getBox(int boxRow, int boxColumn) {
        if (boxRow < 0 || boxRow >= 3) {
            throw new IllegalArgumentException("Box row must be an integer between 0 and 2, received " + boxRow);
        }
        if (boxColumn < 0 || boxColumn >= 3) {
            throw new IllegalArgumentException("Box column must be an integer between 0 and 2, received " + boxColumn);
        }

        CellSet box = new CellSet();
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) {
            box.addCell( cells[i + 3 * boxRow][j + 3 * boxColumn], 3*i+j);
        }
        return box;
    }

    public boolean isFull() {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
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

    public boolean checkAgainstSolution() {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            if (cells[row][column] != solution.getCell(row, column) && !cells[row][column].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public Vector<Integer> cellOptions(int rowIndex, int columnIndex) {
        int[] isDigitForbidden = new int[9];
        Vector<Integer> options = new Vector<>();
        Cell cell = this.getCell(rowIndex, columnIndex);

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
        cell.setOptions(options);
        return options;
    }

    public Sudoku solve() {
        if (solution != null) {
            if (checkAgainstSolution()) {
                return solution;
            } else {
                JOptionPane.showMessageDialog(new JFrame(),
                    "There are no solutions.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else {
            HashSet<Sudoku> solutions = getSolutions();
            if (solutions.size() == 0) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "There are no solutions.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else if (solutions.size() > 1) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "There is more than one solution.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                return solutions.iterator().next();
            }
        }
        return null;
    }

    public HashSet<Sudoku> getSolutions() {
        Stack<Sudoku> options = new Stack<>();
        HashSet<Sudoku> solutions = new HashSet<>();

        if (solution != null) {
            solutions.add(solution.clone());
            return solutions;
        }

        if (this.isFull()) {
            if (this.isSolved()) {
                solution = this.clone();
                solutions.add(solution);
            }
        } else {
            options.push(this.clone());

            while (options.size() > 0) {
                Sudoku sudoku = options.pop();
                int bestRow=0, bestCol=0, bestNumOptions = 10;
                Vector<Integer> bestOptionsVector = new Vector<>();
                for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
                    if (sudoku.getCell(row, column).isEmpty()) {
                        Vector<Integer> optionsVector = sudoku.cellOptions(row, column);
                        if (optionsVector.size() < bestNumOptions) {
                            bestRow = row;
                            bestCol = column;
                            bestNumOptions = optionsVector.size();
                            bestOptionsVector = optionsVector;
                        }
                    }
                }

                for (int i = 0; i < bestNumOptions; i++) {
                    if (bestOptionsVector.size() > 0) {
                        sudoku.getCell(bestRow, bestCol).setValue(bestOptionsVector.get(i));
                        if (sudoku.isSolved()) {
                            solutions.add(sudoku.clone());
                            if (solutions.size() >= 2) return solutions; // remove if you want more than 2 solutions.
                        } else options.push(sudoku.clone());
                    }
                }
            }
        }
        if (solutions.size() == 1) {
            solution = solutions.iterator().next();
        }
        return solutions;
    }

    public Sudoku minimize() throws Exception {
        Sudoku sudoku = this.clone();
        Random rand = new Random();

        //make list of unchecked cells
        ArrayList<int[]> cellCoordinateList = new ArrayList<>();
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            cellCoordinateList.add(new int[] {row,column});
        }

        long start = System.currentTimeMillis();
        long finish = start + 30*1000; // 30 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < finish && cellCoordinateList.size()>0) {

            //randomly select a cell to check
            int index = rand.nextInt(cellCoordinateList.size());
            int row = cellCoordinateList.get(index)[0];
            int column = cellCoordinateList.get(index)[1];

            Sudoku newSudoku = sudoku.clone();
            newSudoku.getCell(row, column).clear();
            newSudoku.solution = null;

            int numSolutions = newSudoku.getSolutions().size();
            if (newSudoku.solution != null) {
                sudoku = newSudoku;
            } else if (numSolutions == 0) {
                throw new Exception("There are no solutions");
            }

            cellCoordinateList.remove(index);
        }
        return sudoku;
    }

    public static Sudoku randomSolution() {
        Sudoku sudoku = new Sudoku();
        Random rand = new Random();
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 10 seconds * 1000 ms/sec

        while (System.currentTimeMillis() < end) {
            //fill a cell randomly
            int cell = rand.nextInt(81);
            if (sudoku.getCell(cell / 9, cell % 9).isEmpty()) {
                Vector<Integer> options = sudoku.cellOptions(cell/9,cell%9);
                int size = options.size();

                if (size == 0) { // no options for cell, remove random cell
                    int index = rand.nextInt(81);
                    sudoku.getCell(index / 9, index % 9).clear();
                } else {
                    int digit = rand.nextInt(size);
                    sudoku.getCell(cell / 9, cell % 9).setValue(options.get(digit));
                }

                sudoku.getSolutions();
                if (sudoku.solution != null) { // unique solution, output the puzzle
                    return sudoku.solution;
                } // else there are multiple solutions, so continue
            }
        }
        return new Sudoku();
    }

    public static Sudoku randomPuzzle() {
        try {
            Sudoku sudoku = randomSolution();
            return sudoku.minimize();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(),
                    e);
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("{");
        string.append(System.lineSeparator());
        for (int row = 0; row < 9; row++) {
            string.append("{");
            for (int column = 0; column < 9; column++) {
                string.append(this.getCell(row, column).getValue()).append(", ");
            }
            string.append("}");
            if (row != 8) string.append(",");
            string.append(System.lineSeparator());
        }
        string.append("}");
        string.append(System.lineSeparator());
        return string.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Sudoku)) {
            return false;
        }
        Sudoku other = (Sudoku) object;


        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            if (other.getCell(row, column).getValue() != this.getCell(row, column).getValue()) {
                return false;
            }
        }
        return true;
    }
}
