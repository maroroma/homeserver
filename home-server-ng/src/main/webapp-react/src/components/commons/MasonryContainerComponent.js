import React from 'react';

import './MasonryContainerComponent.scss';

export default function MasonryContainerComponent({ children, column = 4 }) {

    const columnCountStyle = { "columnCount": column };
    const columnCountStyleOnSmallDevice = { "columnCount": 1 };

    return (
        <>
            <div className="masonry-container hide-on-large-only" style={columnCountStyleOnSmallDevice}>
                {children}
            </div>
            <div className="masonry-container hide-on-small-only" style={columnCountStyle}>
                {children}
            </div>
        </>
    );

}