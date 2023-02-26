import React, {useEffect, useState} from 'react';
import './index.css';
import {ApolloClient, InMemoryCache} from '@apollo/client';
import {Difficulty, Digit, Sudoku} from './util/types';
import {copy} from './util/helpers';
import GraphQLConnector from './util/graphQlConnector';
import {Grid} from "./grid";
import {ApiSudokuToSudoku} from "./util/ApiMappings";
import {useAppDispatch, useAppSelector} from "./store/hooks";
import {setCurrentGridDispatcher, setCurrentPuzzleDispatcher} from "./store/sudokuSlice";
import {Button, ButtonGroup, ToggleButton, ToggleButtonGroup} from "@mui/material";
import ReplayIcon from '@mui/icons-material/Replay';
import DoneIcon from '@mui/icons-material/Done';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
/* eslint-disable react/react-in-jsx-scope -- Unaware of jsxImportSource */
/** @jsxImportSource @emotion/react */
import {css} from '@emotion/react';

const Game = () => {
    const dispatch = useAppDispatch();
    const {currentPuzzle, currentGrid} = useAppSelector((state) => state.sudoku.value);
    const setCurrentPuzzle = (puzzle: Sudoku) => dispatch(setCurrentPuzzleDispatcher(puzzle));
    const setCurrentGrid = (puzzle: Sudoku) => dispatch(setCurrentGridDispatcher(puzzle));

    const [difficulty, setDifficulty] = useState(Difficulty.Random);
    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState({
        error: false,
        text: '',
    });

    const connector = new GraphQLConnector(
        new ApolloClient({
            uri: 'http://localhost:8081/graphql',
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
            grid[row][column] = 0;
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

        connector.checkSolution(currentGrid)
            .then(({checkSolution}) => {
                if (checkSolution) {
                    setMessage({
                        error: false,
                        text: 'Congratulations, you have solved this puzzle!',
                    })
                } else {
                    setMessage({
                        error: true,
                        text: 'There is a mistake somewhere.',
                    });
                }
            })

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
                const puzzle = ApiSudokuToSudoku(newPuzzle.cells);
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
            <ToggleButton key={i} value={difficultyValue}
                          disabled={difficultyValue === difficulty}
                          css={css`
                            border: 1px solid rgba(46, 125, 50, 0.5);

                            &.Mui-disabled {
                              border: 1px solid rgba(46, 125, 50, 0.5);
                            }
                          `}>
                {difficultyValue}
            </ToggleButton>,
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

                <ButtonGroup className="puzzle-controls" variant="outlined" aria-label="outlined primary button group">
                    <Button onClick={reset} startIcon={<ReplayIcon/>}>Reset</Button>
                    <Button color="success" onClick={checkSolution} startIcon={<DoneIcon/>}>Check solution</Button>
                    <Button onClick={solve}>Solve</Button>
                    <Button onClick={nextPuzzle} startIcon={<ArrowForwardIcon/>}>Next puzzle</Button>
                </ButtonGroup>

                <div className="difficulty-controls">
                    <ToggleButtonGroup value={difficulty}
                                       exclusive
                                       onChange={(
                                           event: React.MouseEvent<HTMLElement>,
                                           newDifficulty: Difficulty) => setDifficulty(newDifficulty)}
                                       aria-label="text difficulty" size="small" color="success">
                        {difficultyButtons}
                    </ToggleButtonGroup>
                </div>

                <div className={`message ${message.error ? 'error' : ''}`}>
                    {message.text}
                </div>

            </div>
        </div>
    );
}

export default Game;
