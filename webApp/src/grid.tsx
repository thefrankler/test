import React, { FunctionComponent } from 'react';
import Cell from './cell';

class Grid extends React.Component<{
  grid: (number | undefined)[][],
  puzzle: (number | undefined)[][],
  handleCellChange: (row: number, column: number, value: number | undefined) => void
}> {
  renderCell(row: number, column: number, value: number | undefined, disabled: boolean) {
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
    this.props.grid.forEach((cellRow, row) => {
      let cells: JSX.Element[] = [];
      cellRow.forEach((value, column) => {
        if (value === 0) {
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