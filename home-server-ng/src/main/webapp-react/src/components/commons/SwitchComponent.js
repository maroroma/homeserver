import React from 'react';

export default function SwitchComponent({ dataswitch, onChange, disabled = false }) {

    const innerOnChangeHandler = () => onChange(!dataswitch);

    return (<div className="switch" >
        <label>
            <input type="checkbox" disabled={disabled} checked={dataswitch} onChange={innerOnChangeHandler} ></input>
            <span className="lever"></span>
        </label>
    </div>);

}