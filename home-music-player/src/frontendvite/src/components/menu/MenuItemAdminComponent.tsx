import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {Tools} from "react-bootstrap-icons";

const MenuItemAdminComponent: FC<MenuItemComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <Tools size={40} />,
  });
};

export default MenuItemAdminComponent;
