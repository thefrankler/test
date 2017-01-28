import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel implements ActionListener {

    static final long serialVersionUID = 42L;

    private SudokuPanel p;
    private ButtonPanel b;
    public Sudoku s;

    public MainPanel() {

        //set variables
        b = new ButtonPanel(this);
        p = new SudokuPanel();

        //Layout and borders
        setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(50, 70, 50, 70)); //top, left, bottom, right

        this.add(BorderLayout.CENTER, p);
        this.add(BorderLayout.SOUTH, b);

    }

    public void actionPerformed(ActionEvent ev) {

        if (ev.getSource() == b.check) {

            s = p.readScreen();
            boolean solved = s.checkSolution();

            if (solved) {
                String[] options = {"Next puzzle"};
                int n = JOptionPane.showOptionDialog(this,
                        "Congratulations, you have solved this puzzle!",
                        "Check solution",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        null);
                if (n == JOptionPane.DEFAULT_OPTION) {
                    this.p.setScreen(this.p.randomPuzzle());
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "There is a mistake somewhere.");
            }



        } else if (ev.getSource() == b.solve) {
            s = p.readScreen();

            if(s.complete() != null) {
                s = s.complete();
                this.p.setScreen(s);
            }

        } else if (ev.getSource() == b.next) {
            this.p.setScreen(this.p.randomPuzzle());
        }

    }

}