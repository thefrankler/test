import React from 'react';
import Cell from './cell';
import {Digit} from "./definitions";

class Grid extends React.Component<{
  grid: (Digit | undefined)[][],
  puzzle: (Digit | undefined)[][],
  handleCellChange: (row: Digit, column: Digit, value: Digit | undefined) => void
}> {
  renderCell(row: Digit, column: Digit, value: Digit | undefined, disabled: boolean) {
    return (
      <Cell 
        key={row.toString() + column.toString()} 
        row={row}
        column={column}
        value={value}
        disabled={disabled}
        handleCellChange={(row, column, value) => this.props.handleCellChange(row, column, value)}
      />
    );
  }

  render() {
    let rows: JSX.Element[] = [];
    this.props.grid.forEach((cellRow, i) => {
      let row = i as Digit;
      let cells: JSX.Element[] = [];
      cellRow.forEach((value, j) => {
        let column = j as Digit;
        
        if (!value) {
          cells.push(
            this.renderCell(row, column, undefined, false)
          );
        } else {
          if (value === this.props.puzzle[row][column]) {
            cells.push(
              this.renderCell(row, column, value, true)
            );
          } else {
            cells.push(
              this.renderCell(row, column, value, false)
            );
          }
        }
      });
      
      rows.push(
        <div key={row} className="row">
          {cells}
        </div>
      );
    });

    return (
    <div>
      {rows}
    </div>
    );
  }
}

export default Grid;