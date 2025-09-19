import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {Plus} from "react-bootstrap-icons";

const MenuItemAddComponent: FC<MenuItemComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <Plus size={40} />,
  });
};

export default MenuItemAddComponent;
