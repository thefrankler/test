import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Grid from './grid';

class Game extends React.Component {
  constructor(props) {
    super(props);

    var puzzle = [
      [1,2,3,4,5,6,7,8,9],
      [1,2,3,4,5,6,7,8,9],
      [0,0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0,0],
      [0,0,0,0,0,0,0,0,0]
    ];

    this.state = {
      currentPuzzle: puzzle,
      currentGrid: puzzle
    };
  }
  
  reset() {
    this.setState({
      currentGrid: copy(this.state.currentPuzzle)
    });
  }
  
  handleCellChange(row, column, value) {
    var grid = copy(this.state.currentGrid);
    grid[row][column] = value;
    console.log(value);

    this.setState({
      currentGrid: grid
    });
  }

  checkSolution(puzzle) {
    console.log('Checking solution...');
  }

  solve(puzzle) {
    console.log('Solving...');
  }

  nextPuzzle() {
    console.log('Getting next puzzle...');
  }

  changeDifficulty(difficulty) {
    console.log('Changing difficulty...');
  }

  render() {
    return (
      <div className="game">

        <div className="grid">
          <Grid 
            puzzle={this.state.currentPuzzle}
            grid={this.state.currentGrid}
            handleCellChange={(row, column, value) => this.handleCellChange(row, column, value)}
          />
        </div>

        <div className="game-info">

          <div className="puzzle-controls">
            <button onClick={() => this.reset()}>
              Reset
            </button>
            <button onClick={() => this.checkSolution(this.state.currentGrid)}>
              Check solution
            </button>
            <button onClick={() => this.solve(this.state.currentGrid)}>
              Solve
            </button>
            <button onClick={() => this.nextPuzzle()}>
              Next puzzle
            </button>
          </div>

          <div className="difficulty-controls">
            <button disabled={true} onClick={() => this.changeDifficulty('Random')}>
              Random
            </button>
            <button onClick={() => this.changeDifficulty('Easy')}>
              Easy
            </button>
            <button onClick={() => this.changeDifficulty('Medium')}>
              Medium
            </button>
          </div>

        </div>
      </div>
    );
  }
}

function copy(array) {
  return JSON.parse(JSON.stringify(array));
}

// ========================================

ReactDOM.render(
  <Game />,
  document.getElementById('root')
);