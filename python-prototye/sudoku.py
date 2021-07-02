class Sudoku:
    def __init__(self, *args, **kwds):
        if len(args) == 1:
            self.s = args[0]
        else:
            self.s = [[0] * 9] * 9
        
    def __str__(self):
        string = ""
        for r in self.s:
            for c in r:
                string += str(c) + " "
            string += "\n"
        return string

    
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

    
    def checkFull(self):
        for r in range(9):
            for c in range(9):
                if self.s[r][c] == 0:
                    return false
        return true

    def checkSolution(self):
        # check complete
        for r in range(9):
            for c in range(9):
                if self.s[r][c] == 0:
                    return false

        # check rows
        for r in range(9):
            row = self.getRow(r);
            for c in range(9):
                for d in range(c):
                    if row[c] == row[d]:
                        return false

        # check columns
        for c in range(9):
            column = self.getColumn(c);
            for r in range(9):
                for d in range(r):
                    if column[r] == column[d]:
                        return false

        # check boxes
        for r in range(3):
            for c in range(3):
                box = self.getBox(r, c)
                for f in range(1, 9):
                    for d in range(f):
                        if box[f] == box[d]:
                            return false
                        
        return true
    
    
    def cellOptions(self, r, c):
        newList = []
        options = [];

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
                if not options.contains(i+1):
                    options.append(i + 1)
                    
        return options
    
    def complete(self):
        options = [];
        solutions = [];

        if self.checkFull():
            if self.checkSolution():
                return self
        else:
            news = Sudoku(self.s)
            options.append(news)


            while len(options) > 0 and len(solutions) < 2:
                sud = options.pop()
                i = 0
                while i < 10:  # check for cells with i options
                    for r in range(9):
                        for c in range(9):
                            if sud.s[r][c] == 0:
                                v = sud.cellOptions(r, c)
                                if len(v) == i:
                                    for j in range(i):
                                        sud.s[r][c] = v[j]
                                        if sud.checkSolution():
                                            temp = Sudoku(sud.s)
                                            solutions.append(temp)
                                        else:
                                            options.append(Sudoku(sud.s))
                                    i=100
                    i += 1

            m = len(solutions)
            if m >= 2:
                n = 2
            elif m == 1:
                n = 1
            else:
                n = 0

            if n==0:
                print('no solutions to ' + str(self))
            elif n>1:
                print('multiple solutions to ' + str(self))
            else: # n == 1
                return solutions.pop()

    def checkUniqueSolution(self): # 0 = no solutions, 1 = 1 solution, 2 = more solutions
        options = []
        solutions = []

        if self.checkFull():
            if self.checkSolution(): 
                return 1
            else:
                return 0
        else:
            news = Sudoku(self.s)
            options.append(news)

            while options.size() > 0:
                sud = options.pop()
                bestRow=0
                bestCol=0
                bestNumOptions = 10
                bestOptions = []
                for r in range(9):
                    for c in range(9):
                        if sud.s[r][c] == 0:
                            optionsList = sud.cellOptions(r, c)
                            if len(optionsList) < bestNumOptions:
                                bestRow = r
                                bestCol = c
                                bestNumOptions = len(optionsList)
                                bestOptions = optionsList;

                for i in range(bestNumOptions):
                    if len(bestOptionsVector) > 0:
                        sud.s[bestRow][bestCol] = bestOptionsVector[i]
                        if sud.checkSolution():
                            solutions.append(sud)
                            if len(solutions) >= 2:
                                return 2
                        else:
                            options.append(sud)
            
            return len(solutions)
        
s = Sudoku([
    [1, 2, 3, 4, 5, 6, 7, 8, 9],
    [2, 2, 3, 4, 5, 6, 7, 8, 9],
    [3, 2, 3, 4, 5, 6, 7, 8, 9],
    [4, 2, 3, 1, 5, 6, 2, 8, 9],
    [5, 2, 3, 4, 5, 6, 7, 8, 9],
    [6, 2, 3, 4, 5, 6, 7, 8, 9],
    [7, 2, 3, 3, 5, 6, 4, 8, 9],
    [8, 2, 3, 4, 5, 6, 7, 8, 9],
    [9, 2, 3, 4, 5, 6, 7, 8, 9]
])
print(s.getRow(2))
print(s.getColumn(2))
print(s.getBox(2, 1))