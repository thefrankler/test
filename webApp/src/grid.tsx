import React from 'react';
import Cell from './cell';
import {Digit, Sudoku} from './util/types';

type GridProps = {
    grid: Sudoku,
    puzzle: Sudoku,
    handleCellChange: (row: Digit, column: Digit, value: Digit | undefined) => void
};

export function Grid(props: GridProps) {
    const {grid, puzzle, handleCellChange} = props;

    const renderCell = (row: Digit, column: Digit, value: Digit, disabled: boolean) => (
        <Cell
            key={row.toString() + column.toString()}
            row={row}
            column={column}
            value={value}
            disabled={disabled}
            handleCellChange={(row, column, value) => handleCellChange(row, column, value)}
        />
    );

    const rows: JSX.Element[] = [];
    grid.forEach((cellRow, i) => {
        const row = i as Digit;
        const cells: JSX.Element[] = [];
        cellRow.forEach((value, j) => {
            const column = j as Digit;

            if (!value) {
                cells.push(
                    renderCell(row, column, 0, false),
                );
            } else if (value === puzzle[row][column]) {
                cells.push(
                    renderCell(row, column, value, true),
                );
            } else {
                cells.push(
                    renderCell(row, column, value, false),
                );
            }
        });

        rows.push(
            <div key={row} className="row">
                {cells}
            </div>,
        );
    });

    return (
        <div>
            {rows}
        </div>
    );
}