import React from 'react';
import { useEffect, useState } from 'react';
import IconComponent from './IconComponent';

export default function UniqueCollapsibleContainerComponent({ children, title = "default title" }) {

    const [expanded, setExpanded] = useState(false);

    useEffect(() => {
        window.M.Collapsible.init(document.querySelectorAll("#collapsibleContainer"), {});
    }, []);

    const switchExpand = () => setExpanded(!expanded);

    return (
        <ul id="collapsibleContainer" className="collapsible">
            <li>
                <div className="collapsible-header">
                    <button className="btn btn-small waves-effect waves-light" onClick={switchExpand}>
                        {title}
                        <IconComponent icon={expanded ? "arrow_drop_up": "arrow_drop_down"} classAddons="tiny right"></IconComponent>
                    </button>
                </div>
                <div className="collapsible-body">{children}</div>
            </li>
        </ul>
    );

}