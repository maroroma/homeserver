import React from 'react';

export default function RadioButtonComponent({ text, dataswitch, onChange, disabled = false }) {

    return (
        <label>
            <input className="with-gap" type="radio" checked={dataswitch} disabled={disabled} onChange={onChange} />
            <span>{text}</span>
        </label>
    );
}