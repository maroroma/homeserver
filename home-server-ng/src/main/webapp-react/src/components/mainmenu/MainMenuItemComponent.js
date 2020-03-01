import React from 'react';
import { useEffect } from 'react';
import dropDownUtils from '../commons/dropDownUtils'

export default function MainMenuItemComponent({ menuDescriptor, currentModule, onItemClick }) {

    const hasSubMenuItems = menuDescriptor.subMenu !== undefined;

    const dropDownDescription = hasSubMenuItems ? dropDownUtils().dropDownDescription("mainmenu", menuDescriptor.module) : null;

    useEffect(() => {
        if (hasSubMenuItems) {
            window.M.Dropdown.init(document.querySelectorAll("#" + dropDownDescription.triggerId), { coverTrigger: false });
        }
    }, []);



    const subMenuFragment = hasSubMenuItems ? (
        <ul id={dropDownDescription.targetId} className="dropdown-content">
            {menuDescriptor.subMenu.map((oneSubMenuItem, index) =>
                <li key={index} onClick={() => onItemClick(oneSubMenuItem)}>
                    <a href="#!">
                        <i className="material-icons left">{oneSubMenuItem.icon}</i>{oneSubMenuItem.title}
                    </a>
                </li>)}
        </ul>
    ) : null;

    const menuItem = hasSubMenuItems ?
        <a className="dropdown-trigger" id={dropDownDescription.triggerId} href="#!" data-target={dropDownDescription.targetId}>
            <i className="material-icons left">{menuDescriptor.icon}</i>{menuDescriptor.title}
        </a>
        :
        <a href="#!" onClick={() => onItemClick(menuDescriptor)}><i className="material-icons left">{menuDescriptor.icon}</i>{menuDescriptor.title}</a>
        ;


    return (
        <>
            {subMenuFragment}
            <li className={currentModule?.module === menuDescriptor.module ? 'active' : ''}>
                {menuItem}
            </li>
        </>
    );
}