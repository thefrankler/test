package sudokuSolver.panels;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;

public class LoadingPanel extends JPanel {

    public LoadingPanel() {
        setPreferredSize(new Dimension(600, 700));

        setLayout(new BorderLayout());

        URL url = this.getClass().getResource("../../gif/sliding-squares.gif");
        Icon imageIcon = null;
        if (url != null) {
            imageIcon = new ImageIcon(url);
        } else {
            System.err.println("Couldn't find loading gif");
        }

        JLabel label = new JLabel(imageIcon);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(0, 0, 600, 700);

        label.setBackground(Color.green);
        label.setOpaque(true);

        this.add(label);
    }
}
