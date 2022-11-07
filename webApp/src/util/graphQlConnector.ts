import {ApolloClient, gql, NormalizedCacheObject} from '@apollo/client';
import {Difficulty, Sudoku,} from './types';
import {GetNextPuzzleReturnType, GetSolutionReturnType} from "./graphqlTypes";

class GraphQLConnector {
    constructor(private client: ApolloClient<NormalizedCacheObject>) {
    }

    public async getNextPuzzle(difficulty: Difficulty): Promise<GetNextPuzzleReturnType> {
        const result = await this.client
        .query({
            query: gql`
                query newPuzzle($difficulty: Difficulty!) {
                    newPuzzle(difficulty: $difficulty) {
                        cells {
                            row,
                            column,
                            value
                        }
                    }
                }
            `,
            variables: {
                // difficulty,
                difficulty: Difficulty.Random.toUpperCase(),
            },
        });

        return result.data;
    }

    public async getSolution(puzzle: Sudoku): Promise<GetSolutionReturnType> {
        debugger;
        const result = await this.client
        .query({
            query: gql`
                query solution($puzzle: [[Int]!]!) {
                    solution(puzzle: $puzzle) {
                        cells {
                            row,
                            column,
                            value
                        }
                    }
                }
            `,
            variables: {
                puzzle
            },
        });

        return result.data;
    }

    public async checkSolution(puzzle: Sudoku, solution: Sudoku): Promise<void> {
        await new Promise((r) => setTimeout(r, 2000));

        return Promise.resolve();
        // throw new NotCompleteError();
        // throw new InvalidSolutionError();
    }
}

export default GraphQLConnector;
