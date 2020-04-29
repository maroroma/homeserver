import React from 'react';
import dropDownUtils from '../commons/dropDownUtils'

import { useEffect, useState } from 'react';

import './side-bar-menu-item-component.scss';

export default function SideBarMenuItemComponent({ menuDescriptor, onItemClick }) {

  const [hasSubMenuItems, setHasSubMenuItems] = useState(false);
  const [dropDownDescription, setDropDownDescription] = useState({});


  useEffect(() => {
    const hasSubMenu = menuDescriptor.subMenu !== undefined;
    setHasSubMenuItems(hasSubMenu);
    setDropDownDescription(hasSubMenu ? dropDownUtils().dropDownDescription("submenu", menuDescriptor.module) : null)
  }, [menuDescriptor]);


  useEffect(() => {
    if (hasSubMenuItems) {
      window.M.Dropdown.init(document.querySelectorAll('#' + dropDownDescription.triggerId), { coverTrigger: false });
    }
  }, [hasSubMenuItems, dropDownDescription]);

  const menuItem = hasSubMenuItems ? (
    <li className="no-padding">
      <ul className="collapsible collapsible-accordion">
        <li>
          <a data-target={dropDownDescription.targetId} id={dropDownDescription.triggerId} href="#!"><i className="material-icons left">{menuDescriptor.icon}</i>{menuDescriptor.title}</a>
          <div id={dropDownDescription.targetId}>
            {/* <div className="collapsible-body dropdown-content" id={dropDownDescription.targetId}> */}
            <ul className="submenu-list">
              {
                menuDescriptor.subMenu
                  .map((oneSubMenuItem, index) => <li key={index} onClick={() => {
                    onItemClick(oneSubMenuItem)
                  }}>
                    <a href={"#!" + oneSubMenuItem.title} className="sidenav-close">
                      <i className="material-icons left">{oneSubMenuItem.icon}</i>{oneSubMenuItem.title}
                    </a>
                  </li>)}
            </ul>
          </div>
        </li>
      </ul>
    </li>
  ) : (
      <li>
        <a href="#!" className="sidenav-close" onClick={() => onItemClick(menuDescriptor)}>
          <i className="material-icons left">{menuDescriptor.icon}</i>{menuDescriptor.title}
        </a>
      </li >
    );





  return (<>
    {menuItem}
  </>
  );
}