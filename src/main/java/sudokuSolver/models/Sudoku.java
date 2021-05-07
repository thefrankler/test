package sudokuSolver.models;

import java.util.*;

public class Sudoku {

    private Cell[][] cells = new Cell[9][9];

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

    public Sudoku(Cell[][] cells) {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            this.cells[row][column] = new Cell(row,column);
            if (!cells[row][column].isEmpty()) {
                this.cells[row][column].setValue(cells[row][column].getValue());
            }
        }
    }

    public Sudoku clone() {
        Sudoku copy = new Sudoku();
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            Cell cell = new Cell(row,column);
            if (this.getCell(row, column).isEmpty()) {
                copy.setCell(row, column, cell);
            } else {
                copy.setCell(row, column, cell.setValue(this.getCell(row, column).getValue()));
            }
        }
        return copy;
    }

    public Sudoku setCell(int row, int column, Cell cell) {
        this.cells[row][column] = cell;
        return this;
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

    private CellSet getBox(int boxIndex) {
        if (boxIndex < 0 || boxIndex >= 9) {
            throw new IllegalArgumentException("Box index must be an integer between 0 and 8, received " + boxIndex);
        }

        CellSet box = new CellSet();
        for (int index = 0; index < 9; index++) {
            box.addCell( cells[index/3 + 3 * (boxIndex/3)][index%3 + 3 * (boxIndex%3)], index);
        }
        return box;
    }

    public ArrayList<Integer> cellOptions(int r, int c) {
        boolean[] isDigitForbidden = new boolean[9];
        ArrayList<Integer> options = new ArrayList<>();

        if (!cells[r][c].isEmpty()) {
            options.add(cells[r][c].getValue());
        } else {

            CellSet row = getRow(r);
            CellSet column = getColumn(c);
            CellSet box = getBox(r / 3, c / 3);
            for (int i = 0; i < 9; i++) {
                if (!row.getCell(i).isEmpty()) {
                    isDigitForbidden[row.getCell(i).getValue() - 1] = true;
                }
                if (!column.getCell(i).isEmpty()) {
                    isDigitForbidden[column.getCell(i).getValue() - 1] = true;
                }
                if (!box.getCell(i).isEmpty()) {
                    isDigitForbidden[box.getCell(i).getValue() - 1] = true;
                }
            }

            for (int digit = 0; digit < 9; digit++) {
                if (!isDigitForbidden[digit]) {
                    options.add(digit + 1);
                }
            }
        }
        cells[r][c].setOptions(options);
        return options;
    }

    private void fillCellOptions() {
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            cellOptions(row, column);
        }
    }

    private boolean isFull() {
        return countFilledCells() == 81;
    }

    private int countFilledCells() {
        int count = 0;
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            if (!cells[row][column].isEmpty()) { count +=1; }
        }
        return count;
    }

    public boolean isSolved() {
        if (!this.isFull()) { return false; }

        System.out.println("Checking if this is solved:");
        System.out.println(this.toString());
        for (int i = 0; i < 9; i++) {
            if (!getRow(i).isSolved()) {
                System.out.println("Row " + i + " is incorrect");
                return false;
            }
            if (!getColumn(i).isSolved()) {
                System.out.println("Column " + i + " is incorrect");
                return false;
            }
            if (!getBox(i).isSolved()) {
                System.out.println("Box " + i + ": ");
                System.out.println(getBox(i).toString());
                System.out.println("Box " + i + " is incorrect");
                return false;
            }
        }
        return true;
    }

    public Sudoku getSolution() {
        if (this.isFull()) {
            if (this.isSolved()) {
                return this;
            } else {
                throw new IllegalArgumentException("This sudoku can't be solved, as it already contains a mistake.");
            }
        } else {
            if (countFilledCells() < 17) {
                return null;
            }

            HashSet<Sudoku> solutions = new HashSet<>();
            depthFirstSearch(this, solutions);

            int numSolutions = solutions.size();
            // TODO add logger
//            System.out.println("number of solutions = " + numSolutions);
            if (numSolutions>=2) {
                return null;
            } else if (numSolutions <= 0) {
                throw new IllegalArgumentException("Sudoku has no solutions: \n" + this);
            } else {
                return solutions.iterator().next();
            }
        }
    }

    public Sudoku fillAllPossible() {
        boolean fullyChecked = true;
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            if (this.cells[row][column].isEmpty()) {
                ArrayList<Integer> cellOptions = this.cellOptions(row, column);
//                System.out.println("Cell: (" + row + ", " + column + ") has options " + cellOptions);
                if (cellOptions.size() == 1) {
                    this.cells[row][column].setValue(cellOptions.get(0));
                    fullyChecked = false;
                } else if (cellOptions.size() == 0) {
                    throw new IllegalArgumentException("No solutions");
                }
            }
        }
        if (!fullyChecked) this.fillAllPossible();
        return this;
    }

    private HashSet<Sudoku> depthFirstSearch(Sudoku partialSudoku, HashSet<Sudoku> solutions) {
        Stack<Sudoku> options = new Stack<>();
        options.push(partialSudoku.clone());

        while (options.size() > 0 && solutions.size()<2) {
            Sudoku current = options.pop();
//            System.out.println("Currently checking:");
//            System.out.println(current);
            current.fillCellOptions();

            int numOptions = 2;
            while (numOptions < 10) {
                try {
                    current.fillAllPossible();
                } catch (IllegalArgumentException e) {
                    break;
                }

                boolean optionAdded = false;
                for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
                    if (current.cells[row][column].isEmpty()) {
                        ArrayList<Integer> cellOptions = current.getCell(row, column).getOptions();
                        if (cellOptions.size() == numOptions) {
                            for (int index = 0; index < cellOptions.size(); index++) {
                                Sudoku copy = current.clone();
                                copy.cells[row][column].setValue(cellOptions.get(index));
                                if (copy.isSolved()) {
                                    solutions.add(copy);
//                                    System.out.println("Added solution:");
//                                    System.out.println(copy.toString());
                                } else {
                                    depthFirstSearch(copy, solutions);
//                                    System.out.println("Added option:");
//                                    System.out.println(copy.toString());
                                }
                                optionAdded = true;
                            }
                        }
                    }
                }
                if (optionAdded) {
                    break;
                } else {
                    numOptions++;
                }
            }
//            System.out.println("Number of options: " + options.size());
//            System.out.println("Number of solutions: " + solutions.size());
        }
        return solutions;
    }

    private HashSet<Sudoku> widthFirstSearch(Sudoku partialSudoku) {
        HashSet<Sudoku> solutions = new HashSet<>();
        Queue<Sudoku> options = new LinkedList<>();
        options.add(partialSudoku.clone());

        while (options.size() > 0 && solutions.size()<2) {
            Sudoku current = options.remove();
            int numOptions = 1;
            while (numOptions < 10) {
                // TODO inefficient, could save all values of cellOptions first time around
                boolean optionAdded = false;
                for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
                    if (current.cells[row][column].isEmpty()) {
                        ArrayList<Integer> cellOptions = current.cellOptions(row, column);
                        if (cellOptions.size() == numOptions) {
                            for (int index = 0; index < cellOptions.size(); index++) {
                                Sudoku copy = current.clone();
                                copy.cells[row][column].setValue(cellOptions.get(index));
                                if (copy.isSolved()) {
                                    solutions.add(copy);
//                                    System.out.println("Added solution:");
//                                    System.out.println(copy.toString());
                                } else {
                                    options.add(copy);
//                                    System.out.println("Added option:");
//                                    System.out.println(copy.toString());
                                }
                                optionAdded = true;
                            }
                        }
                    }
                }
                if (optionAdded) {
                    break;
                } else {
                    numOptions++;
                }
            }
//            System.out.println("Number of options: " + options.size());
//            System.out.println("Number of solutions: " + solutions.size());
        }
        return solutions;
    }

    public static Sudoku getRandomSolution() {
        Sudoku sudoku = new Sudoku();

        Random rand = new Random();
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 10 seconds

        while (System.currentTimeMillis() < end) {
            //fill a cell randomly
            int cellIndex = rand.nextInt(81);
            if (sudoku.getCell(cellIndex / 9, cellIndex % 9).isEmpty()) {
                ArrayList<Integer> options = sudoku.cellOptions(cellIndex/9,cellIndex%9);

                if (options.size() == 0) {
                    // no options for cell, remove random cell
                    // TODO remove random cell in the same row, column or box
                    int randomCell = rand.nextInt(81);
                    sudoku.getCell(randomCell / 9, randomCell % 9).clear();
                    System.out.println("Removing cell");
                    System.out.println(sudoku);
                } else {
                    int option = rand.nextInt(options.size());
                    sudoku.getCell(cellIndex / 9, cellIndex % 9).setValue( options.get(option) );
                    System.out.println("Filling in cell");
                    System.out.println(sudoku);
                }

                try {
                    System.out.println("Finding solution");
                    Sudoku solution = sudoku.getSolution();
                    System.out.println("Found solution");
                    System.out.println(solution);
                    if (solution != null) {
                        return solution;
                    }
                    // else there are multiple solutions, so continue
                } catch (IllegalArgumentException e) {
                    // there are no solutions
                    int randomCell = rand.nextInt(81);
                    sudoku.getCell(randomCell / 9, randomCell % 9).clear();
                }
            }
        }
        return new Sudoku();
    }

    public static Sudoku Minimise(Sudoku sudoku) {
        Random rand = new Random();

        //make list of unchecked cells
        ArrayList<Cell> uncheckedCells = new ArrayList<>();
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            uncheckedCells.add(sudoku.getCell(row, column));
        }

        long start = System.currentTimeMillis();
        long finish = start + 30*1000; // 30 seconds
        while (System.currentTimeMillis() < finish && uncheckedCells.size()>0) {
            //randomly select a cell to check
            int cellIndex = rand.nextInt(uncheckedCells.size());
            Cell cell = uncheckedCells.get(cellIndex);

            int tempValue = cell.getValue();
            cell.clear();
            if (sudoku.getSolution() == null) {
                // there are multiple solutions
                cell.setValue(tempValue);
            }

            uncheckedCells.remove(cellIndex);
        }
        return sudoku;
    }

    public static Sudoku getRandomPuzzle() {
        return Minimise(getRandomSolution());
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                str.append(this.cells[row][column].getValue()).append("  ");
            }
            str.append(System.lineSeparator());
        }
        return str.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (getClass() != object.getClass()) return false;

        Sudoku sudoku = (Sudoku) object;
        boolean equal = true;
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            if ( sudoku.getCell(row, column).getValue() != this.getCell(row, column).getValue()) {
                equal = false;
            }
        }
        return equal;
    }
}
