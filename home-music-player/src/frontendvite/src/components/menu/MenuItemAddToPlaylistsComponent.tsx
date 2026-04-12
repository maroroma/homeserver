import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import { BookmarkPlus } from "react-bootstrap-icons";
import { useCustomNavigate } from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";
import type { Track } from "../../api/model/library/Track";

type MenuItemAddToPlaylistsComponentProps = {
  currentTrack: Track;
};

const MenuItemAddToPlaylistsComponent: FC<MenuItemComponentProps & MenuItemAddToPlaylistsComponentProps> = (props) => {

  const navigate = useCustomNavigate();


  return MenuItemComponent({
    ...props,
    icon: <BookmarkPlus size={40}
      onClick={() => navigate(Paths.ADD_TO_PLAYLIST.resolve(props.currentTrack.id))}
    />,
  });
};

export default MenuItemAddToPlaylistsComponent;
