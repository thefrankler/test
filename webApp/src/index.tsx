import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Grid from './grid';
import {checkSolution, getNextPuzzle, getSolution} from './graphQlConnector';
import {copy} from "./helpers";
import {blankPuzzle, Difficulty, Digit, MultipleSolutionsError, NoSolutionsError} from "./definitions";

class Game extends React.Component<{}, {
  currentGrid: (Digit | undefined)[][],
  currentPuzzle: (Digit | undefined)[][],
  difficulty: Difficulty,
  isLoading: boolean,
  message: {
    error: boolean,
    text: string
  }
}>
 {
  constructor(props: {}) {
    super(props);
    const difficulty: Difficulty = Difficulty.Random;
    const puzzle: (Digit | undefined)[][] = blankPuzzle;

    this.state = {
      currentPuzzle: puzzle,
      currentGrid: puzzle,
      difficulty: difficulty,
      isLoading: true,
      message: {
        error: false,
        text: ''
      }
    };

    this.nextPuzzle();
  }
  
  resetMessage() {
    this.setState({
      message: {
        error: false,
        text: ""
      }
    })
  }
  
  handleCellChange(row: Digit, column: Digit, value: Digit | undefined) {
    this.resetMessage();
    
    let grid: (Digit | undefined)[][] = copy(this.state.currentGrid);
    if (!value) {
        grid[row][column] = undefined;
    } else if (value >=1 && value <= 9) {
        grid[row][column] = value;
    }

    this.setState({
      currentGrid: grid
    });
  }
  
  reset() {
    this.resetMessage();
    
    this.setState({
      currentGrid: copy(this.state.currentPuzzle)
    });
  }

  solve(puzzle: (Digit | undefined)[][]) {
    this.resetMessage();
    this.setState({
      isLoading: true
    })

    getSolution(this.state.currentGrid)
      .then((solution) =>
        this.setState({
          currentGrid: solution,
        })
      )
      
      .catch(error => {
        this.setState({
          message: {
            error: true,
            text: error.message
          }
        })
      })

      .finally(() => {
        this.setState({
          isLoading: false
        })
      });
  }

  checkSolution(puzzle: (Digit | undefined)[][]) {
    this.resetMessage();
    this.setState({
      isLoading: true
    })

    checkSolution(this.state.currentPuzzle, this.state.currentGrid)
      .then(() =>
        this.setState({
          message: {
            error: false,
            text: "Congratulations, you have solved this puzzle!"
          }
        })
      )

      .catch(error => {
        this.setState({
          message: {
            error: true,
            text: error.message
          }
        })
      })

      .finally(() => {
        this.setState({
          isLoading: false
        })
      });
  }

  nextPuzzle() {
    this.resetMessage();
    this.setState({
      isLoading: true
    })
      
    getNextPuzzle(this.state.difficulty)
      .then((puzzle) => 
        
        this.setState({
          currentPuzzle: puzzle,
          currentGrid: puzzle,
        })
        
      )
      .catch(error => console.log(error))
      
      .finally(() => { 
        this.setState({
          isLoading: false
        }) 
      });
  }

  changeDifficulty(difficulty: Difficulty) {
    this.setState({
      difficulty: difficulty
    })
  }

  render() {
    let difficultyButtons: JSX.Element[] = [];
    Object.values(Difficulty).forEach((difficulty, i) => {
      difficultyButtons.push(
        <button key={i} disabled={this.state.difficulty === difficulty} onClick={() => this.changeDifficulty(difficulty)}>
          {difficulty}
        </button>
      );
    });

    return (
      <div className="game">
        {this.state.isLoading && 
            <div className="loading">
                <div className="inner"></div>
                <img src={require('./res/sliding-squares-2.gif')} alt="loading..." />
            </div>
        }

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

          <div className={"message " + (this.state.message.error ? 'error' : '')}>
            {this.state.message.text}
          </div>

        </div>
      </div>
    );
  }
}

// ========================================

ReactDOM.render(
  <Game />,
  document.getElementById('root')
);