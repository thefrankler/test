package sudokuSolver.Models;

public class MultipleSolutionsException extends Exception {
    public MultipleSolutionsException() {
        super("There are multiple solutions");
    }
}
