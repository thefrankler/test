import React from 'react';
import {Digit} from "./util/types";

type CellProps = {
    row: Digit,
    column: Digit,
    value?: Digit,
    disabled: boolean,
    handleCellChange: (row: Digit, column: Digit, value: Digit | undefined) => void
};

function Cell(props: CellProps) {
    const {
        row, column, value, disabled, handleCellChange,
    } = props;

    return (
        <input
            type="number"
            min={1}
            max={9}
            className="cell"
            value={value || ''}
            readOnly={disabled}
            onInput={(cell) => handleCellChange(row, column, parseInt(cell.currentTarget.value) as Digit)}
            onKeyDown={(e) => (e.code === 'KeyE' || e.code === 'Period' || e.code === 'Minus') && e.preventDefault()}
        />
        // TODO: can't clear cells once you've typed in them
    );
}

export default Cell;
