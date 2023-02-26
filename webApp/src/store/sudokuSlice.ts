import {createSlice, PayloadAction} from '@reduxjs/toolkit'
import {blankPuzzle} from "../util/defaults";
import {Sudoku} from "../util/types";

export interface SudokuState {
    value: {
        currentPuzzle: Sudoku,
        currentGrid: Sudoku
    }
}

const initialState: SudokuState = {
    value: {
        currentPuzzle: blankPuzzle(),
        currentGrid: blankPuzzle()
    },
}

export const sudokuSlice = createSlice({
    name: 'sudoku',
    initialState,
    reducers: {
        setCurrentPuzzleDispatcher: (state, action: PayloadAction<Sudoku>) => {
            state.value.currentPuzzle = action.payload
        },
        setCurrentGridDispatcher: (state, action: PayloadAction<Sudoku>) => {
            state.value.currentGrid = action.payload
        }
    },
})

export const {setCurrentPuzzleDispatcher, setCurrentGridDispatcher} = sudokuSlice.actions

export default sudokuSlice.reducer