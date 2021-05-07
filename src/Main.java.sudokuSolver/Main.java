module sudokuSolver;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Main {

    static final long serialVersionUID = 42L;

    static JFrame f;

    public static void main(String[] args) {

        f = new JFrame("Sudoku puzzle");
        MainPanel p = new MainPanel();
        f.getContentPane().add(p);
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true); //contains paint()

    }

}
