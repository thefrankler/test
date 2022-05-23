export enum Difficulty {
  Random = "Random",
  Easy = "Easy",
  Medium = "Medium",
}

const digits = [1,2,3,4,5,6,7,8,9] as const;
export type Digit = typeof digits[number];

export const blankPuzzle = [
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined],
  [undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined,undefined]
]