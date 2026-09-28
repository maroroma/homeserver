import type { FC } from "react";
import MenuItemComponent from "./MenuItemComponent";
import { ThreeDotsVertical } from "react-bootstrap-icons";
import { ButtonGroup, Dropdown, DropdownButton } from "react-bootstrap";

import "./MenuItemSubMenu.css"

export type MenuItemSubMenuProps = {
  children: any
  disabled?: boolean;
};

const MenuItemSubMenu: FC<MenuItemSubMenuProps> = ({ children, disabled }) => {

  return <>
    <DropdownButton
      as={ButtonGroup}
      title={<MenuItemComponent icon={<ThreeDotsVertical size={40} />} />}
      variant="light"
      disabled={disabled}
    >
      <Dropdown.Item>
        <ButtonGroup vertical>
          {children}
        </ButtonGroup>
      </Dropdown.Item>
    </DropdownButton>
  </>


};

export default MenuItemSubMenu;
