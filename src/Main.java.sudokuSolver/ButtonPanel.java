module sudokuSolver;

import javax.swing.*;

public class ButtonPanel extends JPanel {

    static final long serialVersionUID = 42L;

    public JButton check, solve, next;

    public ButtonPanel(MainPanel parent) {
        check = new JButton("Check solution");
        solve = new JButton("Solve");
        next = new JButton("Next puzzle");

        add(check);
        add(solve);
        add(next);

        //add action listeners
        check.addActionListener(parent);
        solve.addActionListener(parent);
        next.addActionListener(parent);
    }

}
