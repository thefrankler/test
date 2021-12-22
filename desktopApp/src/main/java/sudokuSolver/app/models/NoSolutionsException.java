package sudokuSolver.app.models;

public class NoSolutionsException extends Exception {
    public NoSolutionsException() {
        super("There are no solutions");
    }
}
