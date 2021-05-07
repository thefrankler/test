module sudokuSolver;

import javax.swing.*;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class Sudoku {

    public int[][] s = new int[9][9];

    public Sudoku() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                s[r][c] = 0;
            }
        }
    }

    public Sudoku(int[][] t) {
        for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {
            s[r][c] = t[r][c];
        }
    }

    @Override
    public String toString()
    {
        String str = "";
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                str = str + Integer.toString(this.s[r][c]) + "  ";
            }
            str = str + System.lineSeparator();
        }
        return str;
    }

    public int[] getRow(int r) {
        int[] row = new int[9];
        for (int c = 0; c < 9; c++) {
            row[c] = s[r][c];
        }
        return row;
    }

    public int[] getColumn(int c) {
        int[] column = new int[9];
        for (int r = 0; r < 9; r++) {
            column[r] = s[r][c];
        }
        return column;
    }

    public int[] getBox(int x, int y) {
        int[] box = new int[9];
        box[0] = s[0 + 3 * x][0 + 3 * y];
        box[1] = s[0 + 3 * x][1 + 3 * y];
        box[2] = s[0 + 3 * x][2 + 3 * y];
        box[3] = s[1 + 3 * x][0 + 3 * y];
        box[4] = s[1 + 3 * x][1 + 3 * y];
        box[5] = s[1 + 3 * x][2 + 3 * y];
        box[6] = s[2 + 3 * x][0 + 3 * y];
        box[7] = s[2 + 3 * x][1 + 3 * y];
        box[8] = s[2 + 3 * x][2 + 3 * y];
        return box;
    }

    private boolean checkFull() {
        for (int r = 0; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                if (s[r][c] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkSolution() {
        //check complete
        for (int r = 0; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                if (s[r][c] == 0) {
                    return false;
                }
            }
        }


        //check rows
        for (int r = 0; r < 9; r++) {
            int[] row = getRow(r);
            for (int c = 1; c < 9; c++) {
                for (int d = 0; d < c; d++) {
                    if (row[c] == row[d]) {
                        return false;
                    }
                }
            }
        }


        //check columns
        for (int c = 0; c < 9; c++) {
            int[] column = getColumn(c);
            for (int r = 1; r < 9; r++) {
                for (int d = 0; d < r; d++) {
                    if (column[r] == column[d]) {
                        return false;
                    }
                }
            }
        }

        //check boxes
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                int[] box = getBox(r, c);
                for (int f = 1; f < 9; f++) {
                    for (int d = 0; d < f; d++) {
                        if (box[f] == box[d]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public Sudoku complete() {
        Stack<Sudoku> options = new Stack<>();
        HashSet<Sudoku> solutions = new HashSet<>();
        int n;

        if (this.checkFull()) {
            if (this.checkSolution()) return this;
        } else {
            Sudoku news = new Sudoku(this.s);
            options.push(news);


            while (options.size() > 0 && solutions.size()<2) {
                Sudoku sud = options.pop();
                int i = 0;
                while (i < 10) {  //check for cells with i options
                    for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {  // inefficient, could save all values of cellOptions first time around
                        if (sud.s[r][c] == 0) {
                            Vector<Integer> v = sud.cellOptions(r, c);
                            if (v.size() == i) {
                                for (int j = 0; j < i; j++) {
                                    sud.s[r][c] = v.get(j);
                                    if (sud.checkSolution()) {
                                        Sudoku temp = new Sudoku(sud.s);
                                        solutions.add(temp);
                                    } else options.push(new Sudoku(sud.s));
                                }
                                i=100;
                            }
                        }
                    }
                    i++;
                }
            }

            int m = solutions.size();
//            System.out.println("number of solutions = "+Integer.toString(n));
            if (m>=2) n = 2;
            else if (m == 1) n = 1;
            else n = 0;

            if (n==0) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "There are no solutions.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else if (n>1) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "There is more than one solution.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            } else { // n == 1
                return solutions.iterator().next();
            }
        }
        return null;
    }

    public int checkUniqueSolution() //0 = no solutions, 1 = 1 solution, 2 = more solutions
    {   Stack<Sudoku> options = new Stack<>();
        HashSet<Sudoku> solutions = new HashSet<>();

        if (this.checkFull()) {
            if (this.checkSolution()) return 1;
            else return 0;
        } else {
            Sudoku news = new Sudoku(this.s);
            options.push(news);

            while (options.size() > 0) {
                Sudoku sud = options.pop();
                int i = 0;
                Main:
                while (i < 10) {  //check for cells with i options
                    for (int r = 0; r < 9; r++) for (int c = 0; c < 9; c++) {  // inefficient, could save all values of cellOptions first time around
                        if (sud.s[r][c] == 0) {
                            Vector<Integer> v = sud.cellOptions(r, c);
                            if (v.size() == i) {
                                for (int j = 0; j < i; j++) {
                                    sud.s[r][c] = v.get(j);
                                    if (sud.checkSolution()) {
                                        Sudoku temp = new Sudoku(sud.s);
                                        solutions.add(temp);
                                        if (solutions.size() >=2) return 2; // remove if you want more than 2 solutions.
                                    } else options.push(new Sudoku(sud.s));
                                }
                                break Main;
                            }
                        }
                    }
                    i++;
                }
            }

            int n = solutions.size();
//            System.out.println("number of solutions = "+Integer.toString(n));
            if (n>2) n = 2;
            return n;
        }
    }

    public Vector<Integer> cellOptions(int r, int c) {
        int[] list = new int[9];
        Vector<Integer> options = new Vector<>();

        //check row
        int[] row = getRow(r);
        for (int i = 0; i < 9; i++) {
            if (row[i] != 0) {
                list[row[i] - 1] = 1;
            }
        }

        //check column
        int[] col = getColumn(c);
        for (int i = 0; i < 9; i++) {
            if (col[i] != 0) {
                list[col[i] - 1] = 1;
            }
        }

        //check box
        int[] box = getBox(r / 3, c / 3);
        for (int i = 0; i < 9; i++) {
            if (box[i] != 0) {
                list[box[i] - 1] = 1;
            }
        }

        //check list
        for (int i=0; i<9; i++){
            if (list[i] == 0){
                if (!options.contains(i+1)) {
                    options.add(i + 1);
                }
            }
        }
        return options;
    }

}
