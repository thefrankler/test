from random import randint
from datetime import datetime, timedelta

class Sudoku:
    def __init__(self, *args, **kwds):
        if len(args) == 1:
            self.s = args[0]
        else:
            self.s = [[0] * 9] * 9
        self.solution = None
        
    def __str__(self):
        string = ""
        for r in self.s:
            for c in r:
                string += str(c) + " "
            string += "\n"
        return string

    def __eq__(self, other):
        if isinstance(other, self.__class__):
            return self.s == other.s
        else:
            return False

    def __ne__(self, other):
        return not self.__eq__(other)
    
    def __hash__(self):
        return hash(repr(self.s))
    
    
    
    def getRow(self, r):
        row = []
        for c in range(9):
            row.append(self.s[r][c])
        return row

    def getColumn(self, c):
        column = [];
        for r in range(9):
            column.append(self.s[r][c])
        return column
    
    def getBox(self, x, y):
        box = []
        for i in range(3):
            for j in range(3):
                box.append(self.s[i + 3*x][j + 3*y])
        return box

    
    
    def isFull(self):
        for r in range(9):
            for c in range(9):
                if self.s[r][c] == 0:
                    return False
        return True

    def isSolved(self):
        # check complete
        for r in range(9):
            for c in range(9):
                if self.s[r][c] == 0:
                    return False

        # check rows
        for r in range(9):
            row = self.getRow(r);
            for c in range(9):
                for d in range(c):
                    if row[c] == row[d]:
                        return False

        # check columns
        for c in range(9):
            column = self.getColumn(c);
            for r in range(9):
                for d in range(r):
                    if column[r] == column[d]:
                        return False

        # check boxes
        for r in range(3):
            for c in range(3):
                box = self.getBox(r, c)
                for f in range(1, 9):
                    for d in range(f):
                        if box[f] == box[d]:
                            return False
                        
        return True
    
    
    def cellOptions(self, r, c):
        newList = [0] * 9
        options = []

        # check row
        row = self.getRow(r)
        for i in range(9):
            if row[i] != 0:
                newList[row[i] - 1] = 1

        # check column
        col = self.getColumn(c)
        for i in range(9):
            if col[i] != 0:
                newList[col[i] - 1] = 1

        # check box
        box = self.getBox(r // 3, c // 3)
        for i in range(9):
            if box[i] != 0:
                newList[box[i] - 1] = 1

        # check list
        for i in range(9):
            if newList[i] == 0:
                if not i+1 in options:
                    options.append(i + 1)
                    
        return options

    def getSolutions(self):
        options = []
        solutions = set()

        if self.solution is not None:
            solutions.add(Sudoku(self.solution.s.copy()))
            return solutions

        if self.isFull():
            if self.isSolved():
                self.solution = Sudoku(self.s.copy())
                solutions.add(self.solution)
        else:
            options.append(Sudoku(self.s.copy()))

            while len(options) > 0:
                sudoku = options.pop()
                bestRow = 0
                bestCol = 0
                bestNumOptions = 10
                bestOptionsVector = []
                for row in range(9):
                    for column in range(9):
                        if sudoku.s[row][column] == 0:
                            optionsVector = sudoku.cellOptions(row, column)
                            if len(optionsVector) < bestNumOptions:
                                bestRow = row
                                bestCol = column
                                bestNumOptions = len(optionsVector)
                                bestOptionsVector = optionsVector

                for i in range(bestNumOptions):
                    if len(bestOptionsVector) > 0:
                        sudoku.s[bestRow][bestCol] = bestOptionsVector[i]
                        if sudoku.isSolved():
                            solutions.add(Sudoku(self.s.copy()))
                            if len(solutions) >= 2:
                                return solutions; # remove if you want more than 2 solutions.
                        else:
                            options.append(Sudoku(self.s.copy()))
                            
        if len(solutions) == 1:
            self.solution = solutions.pop()
        return solutions
    
    def solve(self):
        if not self.solution is None:
            if self == self.solution:
                return self.solution
            else:
                raise Exception("There are no solutions")
        else:
            solutions = self.getSolutions()
            if len(solutions) == 0:
                raise Exception("There are no solutions")
            elif len(solutions) > 1:
                raise Exception("There is more than 1 solution")
            else:
                return solutions.pop()
    
        
    
    def minimize(self):
        sudoku = self
        rand = randint(0, len(cellCoordinateList))

        # make list of unchecked cells
        cellCoordinateList = []
        for row in range(9):
            for column in range(9):
                cellCoordinateList.append((row,column))

        start = datetime.now()
        finish = start + timedelta(0, 30)  # plus 30 seconds
        while datetime.now() < finish and len(cellCoordinateList) > 0:
            # randomly select a cell to check
            index = randint(0, len(cellCoordinateList))
            row = cellCoordinateList[index][0]
            column = cellCoordinateList[index][1]

            newSudoku = Sudoku(sudoku.s.copy())
            newSudoku.s[row][column] = 0

            numSolutions = newSudoku.checkUniqueSolution()
            if numSolutions == 1:
                sudoku = newSudoku
            elif numSolutions == 0:
                raise Exception("There are no solutions")

            cellCoordinateList.pop(index)
        return sudoku

    @staticmethod
    def randomSolution():
        sudoku = Sudoku()
        start = datetime.now()
        end = start + timedelta(0, 10)

        while datetime.now() < end:
            # fill a cell randomly
            cell = randint(0, 81)
            if sudoku.s[cell // 9][cell % 9] == 0:
                options = sudoku.cellOptions(cell // 9, cell % 9)
                size = len(options)

                if size == 0: # no options for cell, remove random cell
                    index = randint(0, 81)
                    sudoku.s[index // 9][index % 9] = 0
                else:
                    digit = randint(0, size)
                    sudoku.s[cell // 9][cell % 9] = options[digit]

                try:
                    return sudoku.complete
                except Exception as e:
                    pass

    @staticmethod
    def randomPuzzle():
        sudoku = Sudoku.randomSolution()
        return sudoku.minimize()