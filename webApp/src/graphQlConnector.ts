import {Difficulty, Digit} from "./definitions";

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