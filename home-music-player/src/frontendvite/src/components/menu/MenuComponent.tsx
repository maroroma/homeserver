/* eslint-disable @typescript-eslint/no-explicit-any */
import type {FC} from "react";

import "./MenuComponent.css";
import {ButtonGroup} from "react-bootstrap";
import MenuItemScrollToTopComponent from "./MenuItemScrollToTopComponent";
import MenuItemGoToPlayerComponent from "./MenuItemGoToPlayerComponent";

type MenuComponentProps = {
  children: any;
  displayScrollToTop?: boolean;
  displayGoToPlayer?: boolean;
};

const MenuComponent: FC<MenuComponentProps> = ({
  children,
  displayScrollToTop = true,
  displayGoToPlayer = true,
}) => {
  return (
    <div className="menu">
      <ButtonGroup className="menu-items">
        {children}
        {displayScrollToTop ? <MenuItemScrollToTopComponent /> : <></>}
        {displayGoToPlayer ? <MenuItemGoToPlayerComponent /> : <></>}
      </ButtonGroup>
    </div>
  );
};

export default MenuComponent;
