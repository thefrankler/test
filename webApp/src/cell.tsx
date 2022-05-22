import React from 'react';
import {Digit} from "./definitions";

class Cell extends React.Component<{
  row: Digit,
  column: Digit,
  value?: Digit,
  disabled: boolean,
  handleCellChange: (row: Digit, column: Digit, value: Digit | undefined) => void
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
        onInput={(cell) => this.props.handleCellChange(this.props.row, this.props.column, parseInt(cell.currentTarget.value) as Digit)}
        onKeyDown={ e => ( e.keyCode === 69 || e.keyCode === 190 || e.keyCode === 189 ) && e.preventDefault() }
      />
    );
  }
}

export default Cell;