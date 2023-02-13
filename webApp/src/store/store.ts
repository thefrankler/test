import {configureStore} from '@reduxjs/toolkit'
import currentPuzzleReducer from './currentPuzzle'
import currentGridReducer from './currentGrid'

export const store = configureStore({
    reducer: {
        currentPuzzle: currentPuzzleReducer,
        currentGrid: currentGridReducer
    }
})

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch