import {Sudoku} from "./types";
import {blankPuzzle} from "./defaults";
import {ApiSudokuCells} from "./graphqlTypes";

export function ApiSudokuToSudoku(cells: ApiSudokuCells[]): Sudoku {
    const grid: Sudoku = blankPuzzle();
    cells.forEach((cell) => {
        grid[cell.row][cell.column] = cell.value;
    });

    return grid;
}