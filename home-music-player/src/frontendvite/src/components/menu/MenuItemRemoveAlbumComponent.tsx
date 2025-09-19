import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {Trash} from "react-bootstrap-icons";

const MenuItemRemoveAlbumComponent: FC<MenuItemComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <Trash size={40} color="red"/>,
  });
};

export default MenuItemRemoveAlbumComponent;
