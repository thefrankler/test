package sudokuSolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sudokuSolver.Models.*;
import sudokuSolver.Panels.MainPanel;

import java.util.Vector;

public class SudokuTest {
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

    int[][] dummySolution = {
            {4, 1, 3, 7, 8, 2, 6, 9, 5, },
            {6, 5, 9, 1, 4, 3, 2, 7, 8, },
            {2, 8, 7, 9, 6, 5, 3, 1, 4, },
            {8, 7, 4, 5, 2, 9, 1, 3, 6, },
            {3, 6, 5, 8, 7, 1, 9, 4, 2, },
            {1, 9, 2, 4, 3, 6, 8, 5, 7, },
            {7, 3, 8, 2, 9, 4, 5, 6, 1, },
            {5, 4, 6, 3, 1, 8, 7, 2, 9, },
            {9, 2, 1, 6, 5, 7, 4, 8, 3, }
        };
    Sudoku dummySolved = new Sudoku(dummySolution);

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

    int[][] almostSolved = {
            {7, 8, 4, 3, 9, 2, 0, 0, 0},
            {5, 3, 6, 1, 4, 8, 0, 0, 0},
            {1, 2, 9, 7, 6, 5, 0, 0, 0},
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
        Assertions.assertTrue(blank.getSolutions().size() > 1);
    }

    @Test
    public void testOptions() {
        Vector options = new Vector<Integer>(1);
        options.add(3);
        options.add(4);
        options.add(6);
        options.add(9);
        Assertions.assertEquals(options, dummySudoku.cellOptions(1, 2));
    }

    @Test
    public void testSolve() throws NoSolutionsException, MultipleSolutionsException {
        Sudoku solution = dummySudoku.clone().getSolution();
        Assertions.assertEquals(dummySolved, solution);
    }

    @Test
    public void testBlankSolutions() {
        int solutions = (new Sudoku()).getSolutions().size();
        Assertions.assertEquals(2, solutions);
    }

    @Test
    public void testUniqueSolution() {
        Sudoku clone = almostSolvedSudoku.clone();
        int solutions = almostSolvedSudoku.getSolutions().size();
        Assertions.assertEquals(1, solutions);
        Assertions.assertEquals(clone, almostSolvedSudoku);
    }

    @Test
    public void testEasyPuzzle() {
        Assertions.assertEquals(almostSolvedSudoku.getDifficulty(), Difficulty.EASY);
    }

    @Test
    public void testRandomPuzzle() {
        Sudoku sudoku = MainPanel.randomPuzzle(Difficulty.EASY);
        System.out.println(sudoku);
        Assertions.assertFalse(sudoku.isFull());
    }
}