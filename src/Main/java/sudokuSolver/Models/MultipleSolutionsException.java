package sudokuSolver.models;

public class MultipleSolutionsException extends Exception {
    public MultipleSolutionsException() {
        super("There are multiple solutions");
    }
}
