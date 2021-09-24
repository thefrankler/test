package sudokuSolver.Models;

public class CellSet {
    private Cell[] set;

    public CellSet() {
        set = new Cell[9];
    }

    public CellSet(int[] array) {
        if (array.length != 9) {
            throw new IllegalArgumentException("CellSet input must be exactly 9 elements long");
        }
        set = new Cell[9];
        for (int index = 0; index < 9; index++) {
            set[index] = new Cell(index, index);
            set[index].setValue(array[index]);
        }
    }

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
        if (!isFull()) { return false;}
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

    public boolean isFull() {
        for (int index = 1; index < 9; index++) {
            if (set[index].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder();
        for (int index = 0; index < 9; index++) {
                string.append(this.set[index].getValue()).append("  ");
        }
        return string.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof CellSet)) {
            return false;
        }
        CellSet other = (CellSet) object;


        for (int index = 0; index < 9; index++) {
            if (other.getCell(index).getValue() != this.getCell(index).getValue()) {
                return false;
            }
        }
        return true;
    }
}