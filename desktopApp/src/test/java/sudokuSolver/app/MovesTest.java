package sudokuSolver.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sudokuSolver.app.models.Sudoku;

import java.util.Set;

public class MovesTest {
    int[][] dummy = {
            {0, 0, 0, 0, 8, 2, 0, 0, 5},
            {0, 5, 0, 1, 0, 0, 0, 7, 8},
            {2, 8, 0, 0, 0, 5, 3, 1, 0},
            {0, 0, 0, 0, 2, 0, 0, 0, 6},
            {3, 6, 5, 0, 7, 0, 9, 4, 2},
            {1, 0, 0, 0, 3, 0, 0, 0, 0},
            {0, 3, 8, 2, 0, 0, 0, 6, 1},
            {5, 4, 0, 0, 0, 8, 0, 2, 0},
            {9, 0, 0, 6, 5, 0, 0, 0, 0}};
    Sudoku dummySudoku = new Sudoku(dummy);

    @Test
    public void testNakedSingle() {
        int[][] option1 = {
                {0, 0, 0, 0, 8, 2, 0, 9, 5},
                {0, 5, 0, 1, 0, 0, 0, 7, 8},
                {2, 8, 0, 0, 0, 5, 3, 1, 0},
                {0, 0, 0, 0, 2, 0, 0, 0, 6},
                {3, 6, 5, 0, 7, 0, 9, 4, 2},
                {1, 0, 0, 0, 3, 0, 0, 0, 0},
                {0, 3, 8, 2, 0, 0, 0, 6, 1},
                {5, 4, 0, 0, 0, 8, 0, 2, 0},
                {9, 0, 0, 6, 5, 0, 0, 0, 0}};
        Sudoku option1Sudoku = new Sudoku(option1);

        Set<Sudoku> options = Moves.nakedSingle(dummySudoku);

        Assertions.assertEquals(options.size(), 6);
        Assertions.assertTrue(options.contains(option1Sudoku));
    }


    @Test
    public void testHiddenSingle() {
        int[][] option1 = {
                {0, 0, 0, 0, 8, 2, 0, 0, 5, },
                {0, 5, 0, 1, 0, 0, 2, 7, 8, },
                {2, 8, 0, 0, 0, 5, 3, 1, 0, },
                {0, 0, 0, 0, 2, 0, 0, 0, 6, },
                {3, 6, 5, 0, 7, 0, 9, 4, 2, },
                {1, 0, 0, 0, 3, 0, 0, 0, 0, },
                {0, 3, 8, 2, 0, 0, 0, 6, 1, },
                {5, 4, 0, 0, 0, 8, 0, 2, 0, },
                {9, 0, 0, 6, 5, 0, 0, 0, 0, }};
        Sudoku option1Sudoku = new Sudoku(option1);

        Set<Sudoku> options = Moves.hiddenSingle(dummySudoku);

        Assertions.assertEquals(options.size(), 10);
        Assertions.assertTrue(options.contains(option1Sudoku));
    }
}
