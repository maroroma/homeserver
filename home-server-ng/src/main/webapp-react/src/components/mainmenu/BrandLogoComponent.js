import React from 'react';
import { SearchDisplayButtonComponent } from './SearchBarComponent';

export default function BrandLogoComponent({ selectedModule, onSearchClick }) {

    const dynamicLogo = selectedModule.sideBarEnabled ?
        <SearchDisplayButtonComponent></SearchDisplayButtonComponent>
        : (<i className="material-icons">home</i>);

        return (
        <a className="brand-logo right">
            {dynamicLogo}
        </a>
    );
}