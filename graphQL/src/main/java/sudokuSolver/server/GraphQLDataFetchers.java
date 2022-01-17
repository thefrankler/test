package sudokuSolver.server;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;
import sudokuSolver.app.models.Difficulty;
import sudokuSolver.app.models.Sudoku;

import java.util.Map;

@Component
public class GraphQLDataFetchers {

    public DataFetcher getRandomSudokuDataFetcher() {
        return dataFetchingEnvironment -> {
            Difficulty difficulty = Difficulty.valueOf(dataFetchingEnvironment.getArgument("difficulty"));
            return Sudoku.newPuzzle(difficulty);
        };
    }

    public DataFetcher getCellsDataFetcher() {
        return dataFetchingEnvironment -> {
            Sudoku sudoku = dataFetchingEnvironment.getSource();
            return sudoku.getCells();
        };
    }
}
