import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class SudokuPanel extends JPanel {

    static final long serialVersionUID = 42L;

    public JFormattedTextField[][] cell;

    public SudokuPanel() {

        setPreferredSize(new Dimension(400, 400));
        Font font1 = new Font("SansSerif", Font.BOLD, 20);

        //set variables
        cell = new JFormattedTextField[9][9];
        for (int r=0; r<9; r++) {
            for (int c=0; c<9; c++){
                NumberFormatter numberFormatter = new NumberFormatter();
                numberFormatter.setAllowsInvalid(true);
                cell[r][c] = new JFormattedTextField(numberFormatter);
                cell[r][c].setHorizontalAlignment(JTextField.CENTER);
                cell[r][c].setFont(font1);

            }
        }

        //set layout and borders
        setLayout(new GridLayout(9,9,0,0));

        int top, bottom, left, right;
        for (int r=0; r<9; r++) {
            for (int c=0; c<9; c++) {
                if (r == 0) {
                    top = 2;
                } else {
                    top = 0;
                }
                if (r == 8 || r == 5 || r == 2) {
                    bottom = 2;
                } else {
                    bottom = 1;
                }
                if (c == 0) {
                    left = 2;
                } else {
                    left = 0;
                }
                if (c == 8 || c == 5 || c == 2) {
                    right = 2;
                } else {
                    right = 1;
                }
                cell[r][c].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
            }
        }

        //add fields
        for (int r=0; r<9; r++) {
            for (int c=0; c<9; c++) {
                add(cell[r][c]);
            }
        }

        setScreen(new Sudoku());

    }

    public Sudoku randomPuzzle() {
        Sudoku sud = randomSolution();
        Random rn = new Random();

        //make list of unchecked cells
        ArrayList<int[]> cells = new ArrayList<>();
        for (int r=0; r<9; r++) for (int c=0; c<9; c++) {
            cells.add(new int[] {r,c});
        }

        long start = System.currentTimeMillis();
        long finish = start + 30*1000; // 30 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < finish && cells.size()>0) {

            //randomly select a cell to check
            int d = rn.nextInt(cells.size());
            int r = cells.get(d)[0];
            int c = cells.get(d)[1];

            int temp = sud.s[r][c];
            sud.s[r][c] = 0;

            if (sud.checkUniqueSolution() > 1) {
                sud.s[r][c] = temp;
            }

            cells.remove(d);
        }
        return sud;
    }

    public Sudoku readScreen(){
        int[][] s = new int[9][9];
            for (int r=0; r<9; r++) {
                for (int c=0; c<9; c++) {
                    try {
                        s[r][c] = Integer.parseInt(cell[r][c].getText());
                    } catch(Exception e){
                        s[r][c] = 0;
                    }

                    if (s[r][c] > 9 || s[r][c] < 1){
                        s[r][c] = 0;
                    }
                }
            }

        return new Sudoku(s);

    }

    public void setScreen(Sudoku sud){
        for (int r=0; r<9; r++) {
            for(int c=0; c<9; c++){
                if (sud.s[r][c] == 0) {
                    cell[r][c].setText("");
                } else {
                    cell[r][c].setText(Integer.toString(sud.s[r][c]));
                }
            }
        }
    }

    public Sudoku randomEasyPuzzle() {
        Sudoku sud = randomSolution();
        Random rn = new Random();

        boolean end = true;
        for (int r=0; r<9; r++) for (int c=0; c<9; c++) {
            if (sud.s[r][c] != 0 && sud.cellOptions(r,c).size() == 0) { // if there is a nonempty cell with options open
                end = false;
            }
        }

        while (!end) {
            //randomly delete unnecessary cells
            int d = rn.nextInt(81);
            if (sud.s[d / 9][d % 9] != 0) {
                Vector<Integer> options = sud.cellOptions(d / 9, d % 9);
                int size = options.size();
                if (size == 0) {
                    sud.s[d / 9][d % 9] = 0;
                }
            }

            end = true;
            for (int r=0; r<9; r++) for (int c=0; c<9; c++) {
                if (sud.s[r][c] != 0 && sud.cellOptions(r,c).size() == 0) {
                    end = false;
                }
            }
        }

        return sud;
    }

    private Sudoku randomSolution() {
        //create blank sudoku
        Sudoku sud = new Sudoku();

        Random rn = new Random();
        long start = System.currentTimeMillis();
        long end = start + 10*1000; // 10 seconds * 1000 ms/sec
        while (System.currentTimeMillis() < end) {
            //fill a cell randomly
            int c = rn.nextInt(81);
            if (sud.s[c / 9][c % 9] == 0) {
                Vector<Integer> options = sud.cellOptions(c/9,c%9);
                int size = options.size();

                if (size == 0) { // no options for cell, remove random cell
                    int d = rn.nextInt(81);
                    sud.s[d / 9][d % 9] = 0;
                } else {
                    int n = rn.nextInt(size);
                    sud.s[c / 9][c % 9] = options.get(n);
                }

                int check = sud.checkUniqueSolution();
                if (check == 1) { // unique solution, output the puzzle
                    return sud.complete();
                } // else there are multiple solutions, so continue
            }
        }
        return new Sudoku();
    }

}
