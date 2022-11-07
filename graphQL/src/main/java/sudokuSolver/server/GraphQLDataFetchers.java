package sudokuSolver.server;

import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;
import sudokuSolver.app.models.Difficulty;
import sudokuSolver.app.models.Sudoku;

import java.util.ArrayList;

@Component
public class GraphQLDataFetchers {

    public DataFetcher<Sudoku> getNewPuzzleDataFetcher() {
        return dataFetchingEnvironment -> {
            Difficulty difficulty = Difficulty.valueOf(dataFetchingEnvironment.getArgument("difficulty"));
            return Sudoku.newPuzzle(difficulty);
        };
    }

    public DataFetcher<Sudoku> getSolutionDataFetcher() {
        return dataFetchingEnvironment -> {
            ArrayList<ArrayList<Integer>> puzzleList = dataFetchingEnvironment.getArgument("puzzle");
            int[][] puzzleArray = puzzleList.stream()
                    .map(row -> row.stream().mapToInt(Integer::intValue).toArray())
                    .toArray(int[][]::new);
            Sudoku puzzle = new Sudoku(puzzleArray);
            return puzzle.getSolution();
        };
    }
}
