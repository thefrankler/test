import {createSlice} from '@reduxjs/toolkit'
import {blankPuzzle} from "../util/defaults";
import {ISudokuState, reducers} from "./ISudokuState";

const initialState: ISudokuState = {
    value: blankPuzzle(),
}

export const currentPuzzleSlice = createSlice({
    name: 'currentPuzzle',
    initialState,
    reducers: reducers,
})

const {set} = currentPuzzleSlice.actions
export const setCurrentPuzzleDispatcher = set

export default currentPuzzleSlice.reducer