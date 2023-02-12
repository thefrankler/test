import {Difficulty, Digit} from "./types";

export type ApiSudokuCells = {
    row: Digit,
    column: Digit,
    value?: Digit
}

export type ApiSudoku = {
    cells: ApiSudokuCells[]
    solution: ApiSudoku
    difficulty: Difficulty
}

export type GetNextPuzzleReturnType = {
    newPuzzle: ApiSudoku
}

export type GetSolutionReturnType = {
    solution: ApiSudoku
}

export type GetCheckSolutionReturnType = {
    checkSolution: boolean
}