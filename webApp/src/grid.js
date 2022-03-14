import React from 'react';
import Cell from './cell';

class Grid extends React.Component {
  renderCell(row, column, value, disabled) {
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
    var rows = [];
    this.props.grid.forEach((cellRow, row) => {
      var cells = [];
      cellRow.forEach((value, column) => {
        if (value === 0) {
          cells.push(
            this.renderCell(row, column, '', false)
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