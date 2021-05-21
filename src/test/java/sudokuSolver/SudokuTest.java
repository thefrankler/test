package sudokuSolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Vector;

public class SudokuTest {
    SudokuPanel sudokuPanel = new SudokuPanel();

    int[][] dummy = {{0, 0, 0, 0, 0, 8, 0, 0, 0},
            {0, 0, 4, 0, 0, 0, 8, 0, 7},
            {0, 5, 0, 0, 9, 0, 0, 3, 0},
            {0, 6, 2, 0, 0, 0, 0, 0, 0},
            {0, 8, 0, 0, 0, 0, 0, 0, 0},
            {5, 9, 0, 6, 0, 0, 0, 0, 2},
            {2, 7, 5, 0, 0, 0, 0, 0, 0},
            {8, 1, 6, 0, 0, 0, 5, 0, 0},
            {4, 3, 9, 0, 0, 0, 0, 0, 0}};
    Sudoku dummySudoku = new Sudoku(dummy);

    int[][] solution = {{7, 8, 4, 3, 9, 2, 6, 5, 1},
            {5, 3, 6, 1, 4, 8, 2, 7, 9},
            {1, 2, 9, 7, 6, 5, 3, 8, 4},
            {2, 4, 3, 8, 1, 6, 7, 9, 5},
            {6, 9, 1, 5, 2, 7, 4, 3, 8},
            {8, 7, 5, 9, 3, 4, 1, 2, 6},
            {4, 6, 8, 2, 7, 9, 5, 1, 3},
            {3, 5, 2, 6, 8, 1, 9, 4, 7},
            {9, 1, 7, 4, 5, 3, 8, 6, 2}};
    Sudoku testSolution = new Sudoku(solution);

    int[][] almostSolved = {{7, 8, 4, 3, 9, 2, 6, 0, 1},
            {5, 3, 6, 1, 4, 8, 2, 7, 9},
            {1, 2, 9, 7, 6, 5, 3, 8, 4},
            {2, 4, 3, 8, 1, 6, 7, 9, 5},
            {6, 9, 1, 5, 2, 7, 4, 3, 8},
            {8, 7, 5, 9, 3, 4, 1, 2, 6},
            {4, 6, 8, 2, 7, 9, 5, 1, 3},
            {3, 5, 2, 6, 8, 1, 9, 4, 7},
            {9, 1, 7, 4, 5, 3, 8, 6, 2}};
    Sudoku almostSolvedSudoku = new Sudoku(almostSolved);

    @Test
    public void testGetBox() {
        CellSet box = new CellSet(new int[]{7, 9, 5, 4, 3, 8, 1, 2, 6});
        Assertions.assertEquals(box, testSolution.getBox(1,2));
    }

    @Test
    public void testSudokuInit() {
        Sudoku blank = new Sudoku();
        Assertions.assertTrue(blank.getCell(0, 0).isEmpty());
        Assertions.assertTrue(blank.checkUniqueSolution() > 1);
    }

    @Test
    public void testOptions() {
        Vector options = new Vector<Integer>(1);
        options.add(2);
        Assertions.assertEquals(options, dummySudoku.cellOptions(1, 1));
    }

    @Test
    public void testSolve() {
        Sudoku solution = almostSolvedSudoku.clone().solve();
        Assertions.assertEquals(testSolution, solution);
    }

    @Test
    public void testUniqueSolution() {
        Sudoku clone = almostSolvedSudoku.clone();
        int solutions = almostSolvedSudoku.checkUniqueSolution();
        Assertions.assertEquals(1, solutions);
        Assertions.assertEquals(clone, almostSolvedSudoku);
    }

    @Test
    public void testRandomSolution() {
        Sudoku solution = sudokuPanel.randomSolution();
        Assertions.assertTrue(solution.isSolved());
        Assertions.assertEquals(solution, solution.solve());
    }

    @Test
    public void testRandomPuzzle() {
        Sudoku sudoku = sudokuPanel.randomPuzzle();
        System.out.println(sudoku);
        Assertions.assertFalse(sudoku.isFull());
    }
}