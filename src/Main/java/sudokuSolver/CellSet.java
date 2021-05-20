package sudokuSolver;

public class CellSet {
    private final Cell[] set = new Cell[9];

    public void addCell(Cell cell, int i) {
        if (i >= 0 && i < 9) {
            set[i] = cell;
        } else {
            throw new IllegalArgumentException("Index must be an integer between 0 and 8, received " + i);
        }
    }

    public Cell getCell(int index) {
        return set[index];
    }

    public boolean isSolved() {
        // If any 2 Cells in a CellSet are equal, then it is not solved
        for (int compareColumn = 1; compareColumn < 9; compareColumn++) {
            for (int previousColumn = 0; previousColumn < compareColumn; previousColumn++) {
                if (set[compareColumn].getValue() == set[previousColumn].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }
}