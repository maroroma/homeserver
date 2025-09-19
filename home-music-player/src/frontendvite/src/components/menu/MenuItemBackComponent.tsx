import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {ChevronLeft} from "react-bootstrap-icons";

const MenuItemBackComponent: FC<MenuItemComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <ChevronLeft size={40} />,
  });
};

export default MenuItemBackComponent;
