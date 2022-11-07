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

type ReturnTypeBase = {
    loading: unknown,
    error: {
        message: string
    }
}

export type GetNextPuzzleReturnType = ReturnTypeBase & {
    newPuzzle: ApiSudoku
}

export type GetSolutionReturnType = ReturnTypeBase & {
    solution: ApiSudoku
}