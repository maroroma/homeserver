import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {PeopleFill} from "react-bootstrap-icons";
import {useCustomNavigate} from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";

const MenuItemGoToAllArtistComponent: FC<MenuItemComponentProps> = (props) => {
  const navigate = useCustomNavigate();
  return MenuItemComponent({
    ...props,
    onClick : () => {navigate(Paths.ALL_ARTISTS.resolve())},
    icon: <PeopleFill size={40} />,
  });
};

export default MenuItemGoToAllArtistComponent;
