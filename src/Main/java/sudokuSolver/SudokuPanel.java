package sudokuSolver;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class SudokuPanel extends JPanel {
    public JTextField[][] gridCells;

    public SudokuPanel() {
        setPreferredSize(new Dimension(400, 400));
        Font font1 = new Font("SansSerif", Font.PLAIN, 20);

        //set variables
        gridCells = new JTextField[9][9];
        for (int row=0; row<9; row++) for (int column=0; column<9; column++){
            gridCells[row][column] = new JTextField(10);

            ((AbstractDocument)gridCells[row][column].getDocument()).setDocumentFilter(new DocumentFilter(){
//                public void insertString(FilterBypass fb, int offs, String str, AttributeSet attr) throws BadLocationException {
//                    String text = fb.getDocument().getText(0, fb.getDocument().getLength());
//                    text += str;
//                    if (text.matches("^[1-9]{0,1}$")) {
//                        fb.insertString(offs, str, attr);
//                    }
//                }
//
//                public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet attr) throws BadLocationException {
//                    String text = fb.getDocument().getText(0, fb.getDocument().getLength());
//                    text += str;
//                    if (text.matches("^[1-9]{0,1}$")) {
//                        fb.replace(offs, length, str, attr);
//                    }
//                }
            });

            gridCells[row][column].setHorizontalAlignment(JTextField.CENTER);
            gridCells[row][column].setFont(font1);
        }

        //set layout and borders
        setLayout(new GridLayout(9,9,0,0));

        int topBorder, bottomBorder, leftBorder, rightBorder;
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
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

        //add fields
        for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
            add(gridCells[row][column]);
        }

        this.setScreen(Sudoku.randomPuzzle());
    }

    public Sudoku readScreen(){
        Sudoku sudoku = new Sudoku();
            for (int row=0; row<9; row++) for (int column=0; column<9; column++) {
                try {
                    sudoku.getCell(row, column).setValue( Integer.parseInt(gridCells[row][column].getText()) );
                } catch(Exception e){
                    sudoku.getCell(row, column).clear();
                }
            }
        return sudoku;
    }

    public void setScreen(Sudoku sudoku){
        for (int row=0; row<9; row++) for(int column=0; column<9; column++){
            if (sudoku.getCell(row, column).isEmpty()) {
                gridCells[row][column].setText("");
            } else {
                gridCells[row][column].setText(Integer.toString(sudoku.getCell(row, column).getValue()));
            }
        }
    }

}
