import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import { ConeStriped } from "react-bootstrap-icons";
import { useArtist, useCustomNavigate } from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";

const MenuItemCreateAlbumComponent: FC<MenuItemComponentProps> = (props) => {
  const navigate = useCustomNavigate();
  const { artist } = useArtist();

  return MenuItemComponent({
    ...props,
    icon: <ConeStriped size={40} />,
    onClick: () => navigate(Paths.CREATE_ALBUM_PROJECT.resolve(artist.id))
  });
};

export default MenuItemCreateAlbumComponent;
