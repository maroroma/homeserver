import React from 'react';

export default function IconComponent({ icon, classAddons = '', onClick = () => {}}) {
    return (<i className={`material-icons ${classAddons}`} onClick={onClick}>{icon}</i>);
}