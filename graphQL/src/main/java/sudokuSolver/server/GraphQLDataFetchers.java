package sudokuSolver.server;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;
import sudokuSolver.app.models.Difficulty;
import sudokuSolver.app.models.Sudoku;

@Component
public class GraphQLDataFetchers {

    public DataFetcher<Sudoku> getNewPuzzleDataFetcher() {
        return dataFetchingEnvironment -> {
            Difficulty difficulty = Difficulty.valueOf(dataFetchingEnvironment.getArgument("difficulty"));
            return Sudoku.newPuzzle(difficulty);
        };
    }
}
