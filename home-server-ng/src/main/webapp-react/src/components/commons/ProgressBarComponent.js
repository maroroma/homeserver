import React from 'react';
import './ProgressBarComponent.scss';

export function range(start, end = 100, colorScheme = "green") {
    return {
        start: start,
        end: end,
        colorScheme: colorScheme
    }
}

export function ProgressBarComponent({ driver }) {
    let colorScheme = driver.colorScheme ? driver.colorScheme : "green" ;
    if (driver.ranges) {
        colorScheme = driver.ranges
            .filter(oneRange => oneRange.start <= driver.currentValue && oneRange.end > driver.currentValue)
            .map(oneRange => oneRange.colorScheme);
    }
    let displayLabel = "";
    if (driver.labels) {
        displayLabel = `${driver.labels.current} / ${driver.labels.max}`
    }


    return <div className={`progress ${colorScheme} lighten-3 progress-bar-component`}>
        <div className={`determinate center-align ${colorScheme} progress-bar-component-text`} style={{ width: driver.currentValue + '%' }}>{displayLabel}</div>
    </div>
}