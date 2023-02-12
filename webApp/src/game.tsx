import React, {useEffect, useState} from 'react';
import './index.css';
import {ApolloClient, InMemoryCache} from '@apollo/client';
import {Difficulty, Digit, Sudoku} from './util/types';
import {blankPuzzle} from './util/defaults';
import {copy} from './util/helpers';
import GraphQLConnector from './util/graphQlConnector';
import {Grid} from "./grid";
import {ApiSudokuToSudoku} from "./util/ApiMappings";

function Game({}) {
    const [currentGrid, setCurrentGrid] = useState<Sudoku>(blankPuzzle);
    const [currentPuzzle, setCurrentPuzzle] = useState<Sudoku>(blankPuzzle);
    const [difficulty, setDifficulty] = useState(Difficulty.Random);
    const [isLoading, setIsLoading] = useState(true);
    const [message, setMessage] = useState({
        error: false,
        text: '',
    });

    const connector = new GraphQLConnector(
        new ApolloClient({
            uri: 'http://localhost:8080/graphql',
            cache: new InMemoryCache(),
        }),
    );

    useEffect(() => {
        nextPuzzle();
    }, []);

    const resetMessage = () => {
        setMessage({
            error: false,
            text: '',
        });
    };

    const handleCellChange = (row: Digit, column: Digit, value: Digit | undefined) => {
        resetMessage();

        const grid = copy(currentGrid);
        if (value === undefined || isNaN(value)) {
            grid[row][column] = undefined;
        } else if (value >= 1 && value <= 9) {
            grid[row][column] = value;
        }

        setCurrentGrid(grid);
    };

    const reset = () => {
        resetMessage();
        setCurrentGrid(copy(currentPuzzle));
    };

    const solve = () => {
        resetMessage();
        setIsLoading(true);

        connector.getSolution(currentGrid)
            .then(({solution}) => {
                const solvedPuzzle = ApiSudokuToSudoku(solution.cells)
                setCurrentGrid(solvedPuzzle);
            })

            .catch(error => {
                setMessage({
                    error: true,
                    text: error.message,
                })
            })

            .finally(() => {
                setIsLoading(false);
            });
    };

    const checkSolution = () => {
        resetMessage();
        setIsLoading(true);

        connector.checkSolution(currentPuzzle, currentGrid)
            .then(() => setMessage({
                error: false,
                text: 'Congratulations, you have solved this puzzle!',
            }))

            .catch((error) => {
                setMessage({
                    error: true,
                    text: error.message,
                });
            })

            .finally(() => {
                setIsLoading(false);
            });
    };

    const nextPuzzle = () => {
        resetMessage();
        setIsLoading(true);

        connector.getNextPuzzle(difficulty)
            .then(({newPuzzle}) => {
                const puzzle = ApiSudokuToSudoku(newPuzzle.cells)
                setCurrentPuzzle(puzzle);
                setCurrentGrid(puzzle);
            })

            .finally(() => {
                setIsLoading(false);
            });
    };

    const difficultyButtons: JSX.Element[] = [];
    Object.values(Difficulty).forEach((difficultyValue, i) => {
        difficultyButtons.push(
            <button key={i} disabled={difficultyValue === difficulty} onClick={() => setDifficulty(difficultyValue)}>
                {difficultyValue}
            </button>,
        );
    });

    return (
        <div className="game">
            {isLoading
                && (
                    <div className="loading">
                        <div className="inner"/>
                        <img src={require('./res/sliding-squares-2.gif')} alt="loading..."/>
                    </div>
                )}

            <div className="grid">
                <Grid
                    puzzle={currentPuzzle}
                    grid={currentGrid}
                    handleCellChange={(row, column, value) => handleCellChange(row, column, value)}
                />
            </div>

            <div className="game-info">

                <div className="puzzle-controls">
                    <button onClick={reset}>
                        Reset
                    </button>
                    <button onClick={checkSolution}>
                        Check solution
                    </button>
                    <button onClick={solve}>
                        Solve
                    </button>
                    <button onClick={nextPuzzle}>
                        Next puzzle
                    </button>
                </div>

                <div className="difficulty-controls">
                    {difficultyButtons}
                </div>

                <div className={`message ${message.error ? 'error' : ''}`}>
                    {message.text}
                </div>

            </div>
        </div>
    );
}

export default Game;
