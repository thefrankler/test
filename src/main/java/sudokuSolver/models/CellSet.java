package sudokuSolver.models;

public class CellSet {
    private final Cell[] cellSet = new Cell[9];

    public CellSet addCell(Cell cell, int i) {
        if (i >= 0 && i < 9) {
            cellSet[i] = cell;
        } else {
            throw new IllegalArgumentException("Index must be an integer between 0 and 8, received " + i);
        }
        return this;
    }

    public Cell getCell(int index) {
        return cellSet[index];
    }

    public boolean isSolved() {
        // If any 2 Cells in a CellSet are equal, then it is not solved
        for (int compareIndex = 1; compareIndex < 9; compareIndex++) for (int previousIndex = 0; previousIndex < compareIndex; previousIndex++) {
            if (cellSet[compareIndex].getValue() == cellSet[previousIndex].getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (int index = 0; index < 9; index++) {
            str.append(this.getCell(index).getValue()).append(" ");
        }
        str.append(System.lineSeparator());
        return str.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;

        if (getClass() != object.getClass()) return false;

        CellSet cellSet = (CellSet) object;
        boolean equal = true;
        for (int index = 0; index < 9; index++) {
            if ( cellSet.getCell(index).getValue() != this.getCell(index).getValue()) {
                equal = false;
            }
        }
        return equal;
    }
}
