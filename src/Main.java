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
        Font cellFont = new Font("SansSerif", Font.BOLD, 20);
        UIManager.put("TextField.font", cellFont);
        Font buttonFont = new Font("SansSerif", Font.PLAIN, 20);
        UIManager.put("Button.font", buttonFont);

        frame = new JFrame("Sudoku puzzle");
        MainPanel panel = new MainPanel();
        frame.getContentPane().add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); //contains paint()
    }

}
