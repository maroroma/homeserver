import React from 'react';
import { useEffect, useState } from 'react';
import dropDownUtils from '../commons/dropDownUtils';
import eventReactor from '../../eventReactor/EventReactor';
import { OTHER_MAIN_MENU_ITEM_CLICK } from '../../eventReactor/EventIds';
import mofRouterEventReactor from './MOFRouterEventReactor';

export default function MainMenuItemComponent({ menuDescriptor, currentModule }) {

    const [dropDownDescription, setDropDownDescription] = useState({});
    const [subMenuStatus, setSubMenuStatus] = useState({ display: false, style: {} });

    useEffect(() => {
        setDropDownDescription(dropDownUtils().dropDownDescription("mainmenu", menuDescriptor.module));
    }, [menuDescriptor]);

    const displaySubMenu = () => {
        if (subMenuStatus.display) {
            closeSubMenu();
        } else {
            eventReactor().emit(OTHER_MAIN_MENU_ITEM_CLICK);
            setSubMenuStatus({
                ...subMenuStatus,
                display: true,
                style: {
                    display: "block",
                    left: document.querySelector("#" + dropDownDescription.triggerId).getBoundingClientRect().left + "px",
                    top: "64px",
                    opacity: 1
                }
            });

        }
    };

    const innerItemClick = (event, selectedElement) => {
        event.preventDefault();
        eventReactor().emit(OTHER_MAIN_MENU_ITEM_CLICK);
        mofRouterEventReactor().selectedModuleChange(selectedElement);
    }


    const closeSubMenu = () => setSubMenuStatus({
        display: false,
        style: {}
    });

    useEffect(() => eventReactor().subscribe(OTHER_MAIN_MENU_ITEM_CLICK, closeSubMenu), []);

    const menuItem = menuDescriptor.subMenu !== undefined ?
        <a className="dropdown-trigger" id={dropDownDescription.triggerId}
            href="#!"
            onClick={displaySubMenu}
            data-target={dropDownDescription.targetId}>
            <i className="material-icons left">{menuDescriptor.icon}</i>
            {menuDescriptor.title}
        </a>
        :
        <a href="#!" onClick={(event) => innerItemClick(event, menuDescriptor)}><i className="material-icons left">{menuDescriptor.icon}</i>{menuDescriptor.title}</a>
        ;


    return (
        <>
            <ul id={dropDownDescription.targetId} className="dropdown-content" style={subMenuStatus.style}>
                {
                    menuDescriptor?.subMenu?.map((oneSubMenuItem) =>
                        <li key={"main_menu_item_sub_menu_" + oneSubMenuItem.title} onClick={(event) => innerItemClick(event, oneSubMenuItem)}>
                            <a href="#!">
                                <i className="material-icons left">{oneSubMenuItem.icon}</i>{oneSubMenuItem.title}
                            </a>
                        </li>)
                }
            </ul>
            <li className={currentModule?.module === menuDescriptor.module ? 'active' : ''}>
                {menuItem}
            </li>
        </>
    );
}