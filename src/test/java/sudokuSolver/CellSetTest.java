package sudokuSolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sudokuSolver.models.Cell;
import sudokuSolver.models.CellSet;

public class CellSetTest {
    @Test
    public void testCellSet() {
        CellSet column2 = new CellSet();
        for (int i = 0; i < 9; i++) {
            Cell cell = (new Cell(i,2)).setValue(i+1);
            column2.addCell(cell, i);
        }
        System.out.printf(column2.toString());
        Assertions.assertTrue(column2.isSolved());

        column2.getCell(3).setValue(8);
        System.out.printf(column2.toString());
        Assertions.assertFalse(column2.isSolved());
    }
}
