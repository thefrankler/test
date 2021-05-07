package sudokuSolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sudokuSolver.models.Sudoku;

import java.util.ArrayList;
import java.util.Arrays;

public class SudokuTest {
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
    public void testSudokuInit() {
        Sudoku blank = new Sudoku();

        Assertions.assertTrue(blank.getCell(0, 0).isEmpty());
        Assertions.assertFalse(blank.isSolved());
        Assertions.assertEquals(null, blank.getSolution());
    }

    @Test
    public void testOptions() {
        Assertions.assertEquals(new ArrayList<Integer>( Arrays.asList(2) ), dummySudoku.cellOptions(1, 1));
    }

    @Test
    public void testSolve() {
        Sudoku solution = almostSolvedSudoku.getSolution();
        System.out.println(solution);
        Assertions.assertEquals(testSolution, solution);
    }

    @Test
    public void testFillAllPossible() {
        System.out.println(almostSolvedSudoku);

        Assertions.assertEquals(testSolution, almostSolvedSudoku.fillAllPossible());
    }

    @Test
    public void testSolution() {
        Sudoku solution = Sudoku.getRandomSolution();
        System.out.println(solution);

        Assertions.assertTrue(solution.isSolved());
        Assertions.assertEquals(solution, solution.getSolution());
    }

    @Test
    public void testMinimise() {
        Sudoku solution = Sudoku.Minimise(testSolution);

        Assertions.assertFalse(solution.isSolved());
        Assertions.assertTrue(solution.getSolution().isSolved());
    }
}
