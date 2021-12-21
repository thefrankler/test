package sudokuSolver;

import sudokuSolver.panels.LoadingPanel;
import sudokuSolver.panels.MainPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Main {
    static JFrame frame;
    static private JPanel loadingPanel;

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

        createLoadingPanel();
        startLoading();

        frame.setVisible(true); //contains paint()
        panel.getNext();

        finishLoading();
    }

    public static void createLoadingPanel() {
        loadingPanel = (JPanel) frame.getGlassPane();
        loadingPanel.setLayout(new BorderLayout());
        loadingPanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                e.consume();
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                e.consume();
            }
        });

        int transparency = 200;
        ImageIcon image = new ImageIcon(Main.class.getClassLoader().getResource("gif/sliding-squares-2.gif"));

        JLabel label = new JLabel(image) {
            protected void paintComponent(Graphics g)
            {
                g.setColor( new Color(255,255,255,transparency) );
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);

        loadingPanel.add(label);
    }

    public static void startLoading() {
        loadingPanel.setVisible(true);
    }

    public static void finishLoading() {
        loadingPanel.setVisible(false);
    }
}
