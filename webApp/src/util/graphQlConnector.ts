import {ApolloClient, gql, NormalizedCacheObject} from '@apollo/client';
import {Difficulty, Sudoku,} from './types';
import {GetCheckSolutionReturnType, GetNextPuzzleReturnType, GetSolutionReturnType} from "./graphqlTypes";

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

    public async checkSolution(solution: Sudoku): Promise<GetCheckSolutionReturnType> {
        const result = await this.client
        .query({
            query: gql`
                query checkSolution($solution: [[Int]!]!) {
                    checkSolution(solution: $solution)
                }
            `,
            variables: {
                solution
            },
        });
        return result.data;
    }
}

export default GraphQLConnector;
