package sudokuSolver;

import sudokuSolver.panels.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    static JFrame frame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 16);
        UIManager.put("Button.font", buttonFont);

        frame = new JFrame("Sudoku puzzle");
        MainPanel panel = new MainPanel();
        frame.getContentPane().add(panel);
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); //contains paint()
        panel.startLoading();

        panel.getNext();
        panel.finishLoading();
    }

}
