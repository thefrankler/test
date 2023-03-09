import {configureStore} from '@reduxjs/toolkit'
import sudokuReducer from './sudokuSlice'
import {apiSlice} from './apiSlice'

export const store = configureStore({
    reducer: {
        sudoku: sudokuReducer,
        [apiSlice.reducerPath]: apiSlice.reducer
    },
    middleware: getDefaultMiddleware =>
        getDefaultMiddleware().concat(apiSlice.middleware)
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch