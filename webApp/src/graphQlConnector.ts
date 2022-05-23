import {
  Difficulty,
  Digit,
  InvalidSolutionError,
  MultipleSolutionsError,
  NoSolutionsError,
  NotCompleteError
} from "./definitions";
import {
  ApolloClient,
  gql, NormalizedCacheObject
} from "@apollo/client";

export class GraphQLConnector {
  constructor(private client: ApolloClient<NormalizedCacheObject>) {}

  public async getNextPuzzle(difficulty: Difficulty): Promise<(Digit | undefined)[][]> {
    let result = await this.client
      .query({
        query: gql`
          query newPuzzle($difficulty: Difficulty!) {
            newPuzzle(difficulty: $difficulty) {
              difficulty,
              cells {
                row,
                column,
                value
              }
            }
          }
        `,
        variables: {
          difficulty: Difficulty.Random
        }
      });
    
    debugger;

    await new Promise(r => setTimeout(r, 2000));

    return await Promise.resolve(
    [
        [1,2,3,4,5,6,7,8,9],
        [1,2,3,4,5,6,7,8,9],
        [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
        [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
        [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
        [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
        [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
        [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
        [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined]
      ]
    )
  }

  public async getSolution(puzzle: (Digit | undefined)[][]): Promise<(Digit | undefined)[][]> {
    await new Promise(r => setTimeout(r, 2000));

    // return await Promise.resolve(
    //   [
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9],
    //     [1,2,3,4,5,6,7,8,9]
    //   ]
    // );

    throw new NoSolutionsError();
    // throw new MultipleSolutionsError();
  }

  public async checkSolution(puzzle: (Digit | undefined)[][], solution: (Digit | undefined)[][]): Promise<void> {
    await new Promise(r => setTimeout(r, 2000));

    return await Promise.resolve();
    // throw new NotCompleteError();
    // throw new InvalidSolutionError();
  }
}