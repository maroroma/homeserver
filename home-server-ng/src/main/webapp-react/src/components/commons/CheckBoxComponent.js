import React from 'react';
import "./CheckBoxComponent.scss";

export default function CheckBoxComponent({ dataswitch, onChange = () => {}, disabled = false }) {

    const innerOnChangeHandler = () => onChange(!dataswitch);

    return (
        <label>
            <input type="checkbox" disabled={disabled} checked={dataswitch} onChange={innerOnChangeHandler} ></input>
            <span className="checkbox"></span>
        </label>
    );

}