import {
  Difficulty,
  Digit,
  InvalidSolutionError,
  MultipleSolutionsError,
  NoSolutionsError,
  NotCompleteError
} from "./definitions";

export async function getNextPuzzle(difficulty: Difficulty): Promise<(Digit | undefined)[][]> {
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

export async function getSolution(puzzle: (Digit | undefined)[][]): Promise<(Digit | undefined)[][]> {
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

export async function checkSolution(puzzle: (Digit | undefined)[][], solution: (Digit | undefined)[][]): Promise<void> {
  await new Promise(r => setTimeout(r, 2000));

  return await Promise.resolve();
  // throw new NotCompleteError();
  // throw new InvalidSolutionError();

}