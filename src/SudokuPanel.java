import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;

public class SudokuPanel extends JPanel {

    private JFormattedTextField[][] gridCells;

    public SudokuPanel() {

        setPreferredSize(new Dimension(400, 400));

        //set variables
        gridCells = new JFormattedTextField[9][9];
        for (int r=0; r<9; r++) {
            for (int c=0; c<9; c++){
                NumberFormatter numberFormatter = new NumberFormatter();
                numberFormatter.setAllowsInvalid(true);
                gridCells[r][c] = new JFormattedTextField(numberFormatter);
                gridCells[r][c].setHorizontalAlignment(JTextField.CENTER);
            }
        }

        //set layout and borders
        setLayout(new GridLayout(9,9,0,0));

        int topBorder, bottomBorder, leftBorder, rightBorder;
        for (int row=0; row<9; row++) {
            for (int column=0; column<9; column++) {
                if (row == 0) {
                    topBorder = 2;
                } else {
                    topBorder = 0;
                }
                if (row == 8 || row == 5 || row == 2) {
                    bottomBorder = 2;
                } else {
                    bottomBorder = 1;
                }
                if (column == 0) {
                    leftBorder = 2;
                } else {
                    leftBorder = 0;
                }
                if (column == 8 || column == 5 || column == 2) {
                    rightBorder = 2;
                } else {
                    rightBorder = 1;
                }
                gridCells[row][column].setBorder(BorderFactory.createMatteBorder(topBorder, leftBorder, bottomBorder, rightBorder, Color.BLACK));
            }
        }

        //add fields
        for (int row=0; row<9; row++) {
            for (int column=0; column<9; column++) {
                add(gridCells[row][column]);
            }
        }

        setScreen(Sudoku.Minimise(Sudoku.getRandomPuzzle()));

    }

    public Sudoku readScreen(){
        Cell[][] cells = new Cell[9][9];
            for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
                try {
                    int value = Integer.parseInt(gridCells[row][column].getText());
                    if (value > 9 || value < 1){
                        cells[row][column].clear();
                    }
                    cells[row][column].setValue( value );
                } catch(Exception e){
                    cells[row][column].clear();
                }
            }

        return new Sudoku(cells);

    }

    public void setScreen(Sudoku sudoku){
        for (int row=0; row<9; row++) for(int column=0; column<9; column++){
            Cell cell = sudoku.getCell(row, column);
            if (cell.isEmpty()) {
                gridCells[row][column].setText("");
            } else {
                gridCells[row][column].setText(Integer.toString(cell.getValue()));
            }
        }
    }
}
