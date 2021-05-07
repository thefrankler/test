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

    private Vector<Integer> cellOptions(int r, int c) {
        boolean[] isDigitForbidden = new boolean[9];
        Vector<Integer> options = new Vector<>();

        if (!cells[r][c].isEmpty()) {
            options.add(cells[r][c].getValue());
            return options ;
        }

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

        for (int digit=0; digit<9; digit++){
            if (!isDigitForbidden[digit]){
                options.add(digit + 1);
            }
        }
        return options;
    }

    private boolean isFull() {
        for (int row = 0; row < 9; row++) {
            for (int column = 1; column < 9; column++) {
                if (cells[row][column].isEmpty()) { return false; }
            }
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
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                if (!getBox(row, column).isSolved()) { return false; }
            }
        }

        return true;
    }

    public Sudoku getSolution() {
        Stack<Sudoku> options = new Stack<>();
        HashSet<Sudoku> solutions = new HashSet<>();

        if (this.isFull()) {
            if (this.isSolved()) {
                return this;
            } else {
                throw new IllegalArgumentException("This sudoku can't be solved, as it already contains a mistake.");
            }
        } else {
            Sudoku newSudoku = new Sudoku(this.cells);
            options.push(newSudoku);

            while (options.size() > 0 && solutions.size()<2) {
                Sudoku partialSudoku = options.pop();
                int numOptions = 1;
                while (numOptions < 10) {
                    // TODO inefficient, could save all values of cellOptions first time around
                    for (int row = 0; row < 9; row++) for (int column = 0; column < 9; column++) {
                        if (partialSudoku.cells[row][column].isEmpty()) {
                            Vector<Integer> cellOptions = partialSudoku.cellOptions(row, column);
                            if (cellOptions.size() == numOptions) {
                                for(int index = 0; index < cellOptions.size(); index++) {
                                    partialSudoku.cells[row][column].setValue( cellOptions.get(index) );
                                    Sudoku copy = new Sudoku(partialSudoku.cells);
                                    if (copy.isSolved()) {
                                        solutions.add(copy);
                                    } else {
                                        options.push(copy);
                                    }
                                }
                                numOptions=100;
                            }
                        }
                    }
                    numOptions++;
                }
            }

            int numSolutions = solutions.size();
//            System.out.println("number of solutions = "+Integer.toString(numSolutions));
            if (numSolutions>=2) {
                return null;
            } else if (numSolutions <= 0) {
                throw new IllegalArgumentException("Sudoku has no solutions: \n" + this);
            } else {
                return solutions.iterator().next();
            }
        }
    }

    public static Sudoku getRandomPuzzle() {
        Sudoku sudoku = new Sudoku();

        Random rand = new Random();
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 10 seconds * 1000 ms/sec

        while (System.currentTimeMillis() < end) {
            //fill a cell randomly
            int cell = rand.nextInt(81);
            if (sudoku.getCell(cell / 9, cell % 9).isEmpty()) {
                Vector<Integer> options = sudoku.cellOptions(cell/9,cell%9);

                if (options.size() == 0) {
                    // no options for cell, remove random cell
                    int randomCell = rand.nextInt(81);
                    sudoku.getCell(randomCell / 9, randomCell % 9).clear();
                } else {
                    int option = rand.nextInt(options.size());
                    sudoku.getCell(cell / 9, cell % 9).setValue( options.get(option) );
                }

                Sudoku solution = sudoku.getSolution();
                if (solution != null) {
                    return solution;
                }
                // else there are multiple solutions, so continue
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
        long finish = start + 30*1000; // 30 seconds * 1000 ms/sec
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

//    public Sudoku randomEasyPuzzle() {
//        Sudoku sud = getRandomSolvedPuzzle();
//        Random rn = new Random();
//
//        boolean end = true;
//        for (int r=0; r<9; r++) for (int c=0; c<9; c++) {
//            if (sud.grid[r][c] != 0 && sud.cellOptions(r,c).size() == 0) { // if there is a nonempty cell with options open
//                end = false;
//            }
//        }
//
//        while (!end) {
//            //randomly delete unnecessary cells
//            int d = rn.nextInt(81);
//            if (sud.grid[d / 9][d % 9] != 0) {
//                Vector<Integer> options = sud.cellOptions(d / 9, d % 9);
//                int size = options.size();
//                if (size == 0) {
//                    sud.grid[d / 9][d % 9] = 0;
//                }
//            }
//
//            end = true;
//            for (int r=0; r<9; r++) for (int c=0; c<9; c++) {
//                if (sud.grid[r][c] != 0 && sud.cellOptions(r,c).size() == 0) {
//                    end = false;
//                }
//            }
//        }
//
//        return sud;
//    }

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
}
