import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import { Bookmarks } from "react-bootstrap-icons";
import { useCustomNavigate } from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";

const MenuItemGoToPlaylistsComponent: FC<MenuItemComponentProps> = (props) => {

  const navigate = useCustomNavigate();


  return MenuItemComponent({
    ...props,
    icon: <Bookmarks size={40} />,
    onClick: () => navigate(Paths.ALL_PLAYLISTS.resolve())
  });
};

export default MenuItemGoToPlaylistsComponent;
