import {Draft, PayloadAction} from '@reduxjs/toolkit'
import {Sudoku} from "../util/types";

export interface ISudokuState {
    value: Sudoku
}

export const reducers = {
    set: (state: Draft<ISudokuState>, action: PayloadAction<Sudoku>) => {
        state.value = action.payload
    }
};