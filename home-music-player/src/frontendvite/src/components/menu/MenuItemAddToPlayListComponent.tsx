import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {CollectionPlay} from "react-bootstrap-icons";

const MenuItemAddToPlayListComponent: FC<MenuItemComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <CollectionPlay size={40} />,
  });
};

export default MenuItemAddToPlayListComponent;
