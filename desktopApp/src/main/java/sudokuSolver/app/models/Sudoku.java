package sudokuSolver.app.models;

import sudokuSolver.app.Moves;

import java.util.*;
import static java.lang.Math.max;

public class Sudoku {

    private Cell[][] cells = new Cell[9][9];
    private Sudoku solution;
    private Difficulty difficulty;

    //region CRUD
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

    protected CellSet getBox(int boxRow, int boxColumn) {
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

    private ArrayList<CellSet> getRows() {
        ArrayList<CellSet> rows = new ArrayList();
        for (int row = 0; row < 9; row++) {
            rows.add(this.getRow(row));
        }
        return rows;
    }

    private ArrayList<CellSet> getColumns() {
        ArrayList<CellSet> columns = new ArrayList();
        for (int column = 0; column < 9; column++) {
            columns.add(this.getColumn(column));
        }
        return columns;
    }

    private ArrayList<CellSet> getBoxes() {
        ArrayList<CellSet> boxes = new ArrayList();
        for (int boxRow = 0; boxRow < 3; boxRow++) for (int boxColumn = 0; boxColumn < 3; boxColumn++)  {
            boxes.add(getBox(boxRow, boxColumn));
        }
        return boxes;
    }

    public ArrayList<CellSet> getCellSets() {
        ArrayList<CellSet> cellSets = new ArrayList();
        cellSets.addAll(this.getRows());
        cellSets.addAll(this.getColumns());
        cellSets.addAll(this.getBoxes());
        return cellSets;
    }

    public ArrayList<Cell> getCells() {
        ArrayList<Cell> cells = new ArrayList();
        for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
            cells.add(this.getCell(row, column));
        }
        return cells;
    }

    protected Difficulty getDifficulty() {
        if (difficulty == null) {
            try {
                difficulty = calculateDifficulty();
            } catch (Exception e) {
                return null;
            }
        }
        return difficulty;
    }

    public Sudoku getSolution() throws NoSolutionsException, MultipleSolutionsException {
        if (solution == null) {
            solution = bruteSolve();
        }
        return solution;
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
    //endregion

    //region Brute Solve
    public Vector<Integer> cellOptions(Cell cell) {
        return this.cellOptions(cell.getRow(), cell.getColumn());
    }

    protected Vector<Integer> cellOptions(int rowIndex, int columnIndex) {
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

    private Sudoku minimise(Difficulty level) throws NoSolutionsException {
        System.out.println("Minimising puzzle of difficulty " + level + "...");

        Sudoku sudoku = this.clone();
        Random rand = new Random();

        //make list of unchecked cells
        ArrayList<int[]> cellCoordinateList = new ArrayList<>();
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            cellCoordinateList.add(new int[] {row,column});
        }

        long start = System.currentTimeMillis();
        long finish = start + 5*1000; // 5 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < finish && cellCoordinateList.size()>0) {

            //randomly select a cell to check
            int index = rand.nextInt(cellCoordinateList.size());
            int row = cellCoordinateList.get(index)[0];
            int column = cellCoordinateList.get(index)[1];

            Sudoku newSudoku = sudoku.clone();
            newSudoku.getCell(row, column).clear();
            newSudoku.solution = null;

            int numSolutions = newSudoku.calculateSolutions().size();
            if (numSolutions == 0) { throw new NoSolutionsException(); }
            if (
                newSudoku.solution != null &&
                ( level == Difficulty.RANDOM || newSudoku.getDifficulty().ordinal() <= level.ordinal() )
            ){
                sudoku = newSudoku;
            }

            cellCoordinateList.remove(index);
        }
        System.out.println("Minimised");
        return sudoku;
    }

    private static Sudoku randomPuzzle() {
        System.out.println("Generating random puzzle...");
        Sudoku sudoku = new Sudoku();
        Random rand = new Random();
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 10 seconds * 1000 ms/sec
        List<Cell> emptyCells = sudoku.getCells();
        List<Cell> filledCells = new ArrayList<>();

        while (System.currentTimeMillis() < end) {
            //fill a cell randomly
            Cell cell = emptyCells.get(rand.nextInt(emptyCells.size()));
            Vector<Integer> options = sudoku.cellOptions(cell);

            if (options.size() == 0) { // no options for cell, remove random cell
                Cell filledCell = filledCells.get(rand.nextInt(filledCells.size()));
                filledCell.clear();
                filledCells.remove(filledCell);
                emptyCells.add(filledCell);
            } else {
                int digit = rand.nextInt(options.size());
                cell.setValue(options.get(digit));
                filledCells.add(cell);
                emptyCells.remove(cell);
            }

            sudoku.calculateSolutions();
            if (sudoku.solution != null) { // unique solution, output the puzzle
                return sudoku;
            } // else there are multiple solutions, so continue
        }
        return new Sudoku();
    }

    public static Sudoku newPuzzle(Difficulty level) throws NoSolutionsException {
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
    }

    private Sudoku bruteSolve() throws NoSolutionsException, MultipleSolutionsException {
        if (solution == null) {
            HashSet<Sudoku> solutions = calculateSolutions();
            if (solutions.size() == 0) {
                throw new NoSolutionsException();
            } else if (solutions.size() > 1) {
                throw new MultipleSolutionsException();
            } else {
                return solutions.iterator().next();
            }
        } else {
            if (this == this.solution) {
                return solution;
            } else {
                throw new NoSolutionsException();
            }
        }
    }

    protected HashSet<Sudoku> calculateSolutions() {
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
    //endregion

    //region Difficulty
    private Difficulty calculateDifficulty() throws NoSolutionsException {
        Difficulty calculatedDifficulty = this.calculateDifficulty(30);
        System.out.println("Difficulty is " + calculatedDifficulty);
        return calculatedDifficulty;
    }

    private Difficulty calculateDifficulty(int timeout) throws NoSolutionsException {
        System.out.println("Calculating difficulty...");

        //make list of unchecked cells
        ArrayList<int[]> cellCoordinateList = new ArrayList<>();
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            cellCoordinateList.add(new int[] {row,column});
        }

        long start = System.currentTimeMillis();
        long finish = start + timeout*1000;
        if (difficulty != null) {
            System.out.println("Difficulty is " + difficulty);
            return difficulty;
        }

        if (isFull()) {
            if (isSolved()) {
                return Difficulty.EASY;
            } else {
                throw new NoSolutionsException();
            }
        } else {
            Set<Sudoku> options = new LinkedHashSet();

            int level = 0;
            while (options.isEmpty() && level <= Moves.maxLevel) {
                options = Moves.getOptionsForLevel(level++, this);
            }
            Difficulty moveDifficulty = Difficulty.EASY;
            if (level > 1) {
                moveDifficulty = Difficulty.MEDIUM;
            }

            if (options.isEmpty()) {
                return Difficulty.values()[Difficulty.values().length - 1]; // hardest difficulty
            } else {
                if (System.currentTimeMillis() < finish) {
                    return moveDifficulty;
                } else {
                    return Difficulty.values()[max(
                        moveDifficulty.ordinal(),
                        options.stream()
                            .mapToInt(option -> option.getDifficulty().ordinal())
                            .min()
                            .orElseThrow(NoSuchElementException::new)
                    )];
                }
            }
        }
    }
    //endregion

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
    public int hashCode() {
        return getCells().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        Sudoku sudoku = (Sudoku) o;
        return getCells().equals(sudoku.getCells());
    }
}