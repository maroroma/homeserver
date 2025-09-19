import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {PersonDash} from "react-bootstrap-icons";

const MenuItemRemoveArtistComponent: FC<MenuItemComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <PersonDash size={40} color="red"/>,
  });
};

export default MenuItemRemoveArtistComponent;
