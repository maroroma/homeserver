import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {PersonAdd} from "react-bootstrap-icons";

const MenuItemAddArtistComponent: FC<MenuItemComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <PersonAdd size={40} />,
  });
};

export default MenuItemAddArtistComponent;
