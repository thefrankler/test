package sudokuSolver;

import sudokuSolver.models.Cell;
import sudokuSolver.models.CellSet;
import sudokuSolver.models.Sudoku;

import java.util.*;
import java.util.stream.Collectors;

public class Moves {
    public static int maxLevel = 1;

    public static Set<Sudoku> getOptionsForLevel(int level, Sudoku sudoku) {
        Set<Sudoku> options = new LinkedHashSet();
        if (level == 0) {
            options.addAll(nakedSingle(sudoku));
            options.addAll(hiddenSingle(sudoku));
        }

        return options;
    }

    private static Set<Sudoku> nakedSingle(Sudoku sudoku) {
        // only 1 option for a number in a particular row, column or box
        Set<Sudoku> options = new LinkedHashSet();

        for (Cell cell : sudoku.getCells()) {
            if (cell.isEmpty()) {
                Vector<Integer> optionsVector = sudoku.cellOptions(cell);
                if (optionsVector.size() == 1) {
                    Sudoku newSudoku = sudoku.clone();
                    newSudoku.getCell(cell.getRow(), cell.getColumn()).setValue(optionsVector.firstElement());
                    options.add(newSudoku);
                }
            }
        }
        return options;
    }

    private static Set<Sudoku> hiddenSingle(Sudoku sudoku) {
        // only 1 number available in a particular row, column or box
        Set<Sudoku> options = new LinkedHashSet();

        for (CellSet cellSet : sudoku.getCellSets()) {
            Map<Integer, ArrayList<Cell>> optionsForDigit = new HashMap();

            for (int digit = 0; digit < 9; digit++) {
                optionsForDigit.put(digit, new ArrayList());
                for (Cell cell : cellSet.getCells()) {
                    if (!cell.isEmpty()) {
                        if (sudoku.cellOptions(cell.getRow(), cell.getColumn()).contains(digit)) {
                            optionsForDigit.get(digit).add(cell);
                        }
                    }
                }
            }
            Map<Integer, Cell> singleOptions = optionsForDigit.entrySet().stream()
                    .filter(map -> map.getValue().size() == 1)
                    .collect(Collectors.toMap(map -> map.getKey(), map -> map.getValue().get(0)));

            for (Map.Entry<Integer, Cell> entry : singleOptions.entrySet()) {
                Integer digit = entry.getKey();
                Cell cell = entry.getValue();

                Sudoku newSudoku = sudoku.clone();
                newSudoku.getCell(cell.getRow(), cell.getColumn()).setValue(digit);
                options.add(newSudoku);
            }
        }
        return options;
    }
}
