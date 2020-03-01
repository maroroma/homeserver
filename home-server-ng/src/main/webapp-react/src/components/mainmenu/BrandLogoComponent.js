import React from 'react';

export default function BrandLogoComponent({ selectedModule, onSearchClick }) {

    const dynamicLogo = selectedModule.sideBarEnabled ?
        (<i className="material-icons" onClick={onSearchClick}>search</i>)
        : (<i className="material-icons">home</i>);

        return (
        <a className="brand-logo right">
            {dynamicLogo}
        </a>
    );
}