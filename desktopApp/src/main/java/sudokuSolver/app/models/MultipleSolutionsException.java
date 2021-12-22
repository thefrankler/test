package sudokuSolver.app.models;

public class MultipleSolutionsException extends Exception {
    public MultipleSolutionsException() {
        super("There are multiple solutions");
    }
}
