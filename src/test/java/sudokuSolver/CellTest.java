package sudokuSolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sudokuSolver.models.Cell;

public class CellTest {
    @Test
    public void testCellInit() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Cell(-1,2);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Cell(1,9);
        });
    }

    @Test
    public void testCellOptions() {
        Cell cell = new Cell(2,3);
        cell.setValue(4);

        Assertions.assertFalse(cell.isEmpty());
        Assertions.assertEquals(4, cell.getValue());
        Assertions.assertEquals(2, cell.getRow());
        Assertions.assertEquals(3, cell.getColumn());

        cell.clear();

        Assertions.assertTrue(cell.isEmpty());
    }
}
