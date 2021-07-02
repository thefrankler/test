# Definitons:
- *Neighbours* of a cell are all cells that share a cellset with it.
- The *intersection* of 2 cells are the cells that are neighbours to both.
- A *link* for n is when n can only be in 2 cells in a cellset.
- 2 cells are *doubly linked* if they are chained by 2 numbers.

# Moves
## Easy

1. Naked single 8 -         There is only 1 option for a number in a particular cell. (look at row, column and box)
2. Hidden single 3 -        A particular number only has one option in a row / column / box.

3. Interactions 20 -        Get all possible places for a number in a particular primary cellset. If they all lie in another secondary cellset, use that to deduce things about the other cellsets of the same type as the primary.
5. Similar to 3 -           If a particular number lies in 2 rows in 2 boxes of the same row, then the third box of that must have the number in the third row.

## Medium

6. Naked pairs 35 -         Get all options for all cells, then if there are 2 doubly linked cells (with exactly 2 options), then the other cells in that cellset cannot contain those numbers.
7. Hidden pairs 55 -        Get all options for all cells. If there are 2 doubly linked cells, then there are no other options for those cells, and those numbers cannot be in other cells in the cellset.
8. Naked subsets 35 -       Get all options for all cells, then if there are n cells in the same row, column or box that have the same n options, then the other cells in that cellset cannot contain those numbers.
9. Hidden subsets 55 -      Get all options for all cells. If there is a cellset where n numbers can only be in n cells, then there are no other options for those cells, and those numbers cannot be in other cells in the cellset.

10. X-wing 45 -             If there are 2 columns where an n-link, and those cells are in the same 2 rows, then no other cells in those rows can be that number.
10. Mutant X-wing -         If there are 2 primary cellsets where a number only has 2 options, and those options are in the same 2 secondary cellsets, then no other cells in those secondary cellsets can be that number.

## Hard

11. Chained pairs (remote pair) 150 -     If there are some cells making a chain of double linked pairs using the same 2 numbers, then the intersection of the endpoints of the chain cannot have either of the options.

12. Swordfish 75 -          Similar to X-wing, but any loop, not just a rectangle. All columns with only 2 options for a number - all corresponding rows must have the number in one of the columns.
13. Forcing chain -         Find all cells with only 2 options. Pick one of these cells, choose an option for it, then fill in the other cells accordingly, keeping track of the result. Then choose the other option, and see if the result is the same for any cells.
14. XY-wing 120 -           If there is "root" cell with 2 options a and b, and 2 other "wing" cells that share a cellset with it, with 2 options, ac and bc, then any cell that is in the intersection of a cellset of wing1 and a cellset of wing2 cannot be c. Could find all potential wings of each cell with 2 options.
14. XYZ-wing 180 -          If there is "root" cell with 3 options abc, and 2 other "wing" cells that share a cellset with it, with 2 options, ac and bc, then any cell that is in the intersection of all 3 cannot be c. Could find all potential wings of each cell with 2 options.
15. Colouring 200 -         Similar to forcing chain. If there is a chain of cells where each pair is the only pair in a cellset to contain a number. Then either all even links in the chain are that number, or all odd links. Check all pairs of different colours - intersections cannot be that number.
16. Jellyfish 260 -         If there are a set of n rows that can only have a number in up to n columns, then the other rows cannot have the number in those columns.

## Extreme

17. Mutant swordfish 500 -  If n cellsets have a number in only n places, then the secondary cellsets are if these cells happen to fall into n other cellsets. Eg columns are primary and rows are secondary for a swordfish. Then that number in a secondary cellset that is not in a primary cellset can be removed.
18. Multi-colouring 750 -   If you have 2 groups of colourings of the same number a1 a2 and b1 b2, then if all cells in a1 share cellsets with an element of b1, then intersections of an a2 and a b2 cannot be that number. In addition, if 2 cells in a1 share a cellset with b1 and b2, then a1 is not the number and all a2 are.
19. Naked XY chain 900 -    If there is a chain of cells each with 2 options, where an option is shared between neighbouring cells and the ends of the chain share on option, then the intersection of the ends cannot contain that option.

## Possible additions:
- Finned X-wing http://www.sudokusnake.com/finnedxwings.php
- Finned swordfish
- wxyz-wings
- Anything from the second row of advanced onwards.


# Possible prototypes:
- Count the number of "moves" available at each stage
- Count how many of each difficulty of move are available at each step.
- Calculate the hardest move in the "easiest" path through the solution tree.
- Keep track of how much solid or induced data is used for a move.