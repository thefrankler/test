import React from 'react';
import './index.css';
import Game from './game';
import {createRoot} from "react-dom/client";
import {Provider} from "react-redux";
import {store} from "./store/store";

const container = document.getElementById('root');
const root = createRoot(container!);

root.render(
    <Provider store={store}>
        <Game/>
    </Provider>
);