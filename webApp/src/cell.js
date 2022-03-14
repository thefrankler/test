import React from 'react';

class Cell extends React.Component {
  render() {
    if (this.props.disabled) {
      return (
        <input 
          type="text" 
          className="cell" 
          value={this.props.value}
          readOnly
        />
      );
    } else {
      return (
        <input 
          type="text" 
          className="cell" 
          value={this.props.value}
          onInput={(cell) => this.props.handleCellChange(this.props.row, this.props.column, parseInt(cell.target.value))}
        />
      );
    }
  }
}

export default Cell;