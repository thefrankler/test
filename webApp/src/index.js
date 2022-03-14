import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Grid from './grid';
import Difficulty from './difficulty';
import getNextPuzzle from './graphQlConnector';

class Game extends React.Component {
  constructor(props) {
    super(props);
    var puzzle = getNextPuzzle();

    this.state = {
      currentPuzzle: puzzle,
      currentGrid: puzzle,
      difficulty: Difficulty.Random
    };
  }
  
  handleCellChange(row, column, value) {
    var grid = copy(this.state.currentGrid);
    grid[row][column] = value;
    console.log(value);

    this.setState({
      currentGrid: grid
    });
  }
  
  reset() {
    this.setState({
      currentGrid: copy(this.state.currentPuzzle)
    });
  }

  solve(puzzle) {
    console.log('Solving...');
  }

  checkSolution(puzzle) {
    console.log('Checking...');
  }

  nextPuzzle() {
    var puzzle = getNextPuzzle(this.state.difficulty);

    this.setState({
      currentPuzzle: puzzle,
      currentGrid: puzzle,
    });
  }

  changeDifficulty(difficulty) {
    this.setState({
      difficulty: difficulty
    })
  }

  render() {
    var difficultyButtons = [];
    Difficulty.all.forEach((difficulty, i) => {
      difficultyButtons.push(
        <button key={i} disabled={this.state.difficulty === difficulty} onClick={() => this.changeDifficulty(difficulty)}>
          {difficulty.name}
        </button>
      );
    });

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
            {difficultyButtons}
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