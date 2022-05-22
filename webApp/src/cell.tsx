import React from 'react';

class Cell extends React.Component<{
  row: number,
  column: number,
  value: number | undefined,
  disabled: boolean,
  handleCellChange: (row: number, column: number, value: number | undefined) => void
}> {
  render() {
    return (
      <input 
        type="number" 
        min={1}
        max={9}
        className="cell" 
        value={this.props.value || ''}
        readOnly={this.props.disabled}
        onInput={(cell) => this.props.handleCellChange(this.props.row, this.props.column, parseInt(cell.currentTarget.value))}
      />
    );
  }
}

export default Cell;