package sudokuSolver.models;

import java.util.Vector;

public class Cell {
    private final int row;
    private final int column;
    private int value;
    private Vector<Integer> options;

    public Cell(int row, int column) {
        if (row >= 0 && row < 9) {
            this.row = row;
        } else {
            throw new IllegalArgumentException("Row must be an integer between 0 and 8, received " + row);
        }

        if (column >= 0 && column < 9) {
            this.column = column;
        } else {
            throw new IllegalArgumentException("Column must be an integer between 0 and 8, received " + column);
        }

        this.value = 0;
    }

    public Cell setValue(int value) {
        if (value > 0 && value <= 9) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Value must be an integer between 1 and 9, received " + value);
        }
        return this;
    }

    public Cell clear() {
        value = 0;
        return this;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public int getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Cell setOptions(Vector<Integer> options) {
        this.options = options;
        return this;
    }

    public Vector<Integer> getOptions() {
        return options;
    }
}