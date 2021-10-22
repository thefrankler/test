package sudokuSolver.models;

public class NoSolutionsException extends Exception {
    public NoSolutionsException() {
        super("There are no solutions");
    }
}
