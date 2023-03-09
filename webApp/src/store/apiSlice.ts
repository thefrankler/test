import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {Sudoku} from "../util/types";

export const apiSlice = createApi({
    reducerPath: 'api',
    baseQuery: fetchBaseQuery({baseUrl: 'http://localhost:8081/graphql'}),
    endpoints: builder => ({
        getPosts: builder.query<Sudoku, number>({
            query: (id) => `post/${id}`,
        }),
    })
})

export const {useGetPostsQuery} = apiSlice