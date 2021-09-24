package sudokuSolver.Models;

public class NoSolutionsException extends Exception {
    public NoSolutionsException() {
        super("There are no solutions");
    }
}
