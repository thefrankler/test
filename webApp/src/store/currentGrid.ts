import {createSlice} from '@reduxjs/toolkit'
import {blankPuzzle} from "../util/defaults";
import {ISudokuState, reducers} from "./ISudokuState";

const initialState: ISudokuState = {
    value: blankPuzzle(),
}

export const currentGridSlice = createSlice({
    name: 'currentGrid',
    initialState,
    reducers: reducers,
})

const {set} = currentGridSlice.actions
export const setCurrentGridDispatcher = set

export default currentGridSlice.reducer