import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import { FolderPlus } from "react-bootstrap-icons";
import { useCustomNavigate } from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";

const MenuItemCreateArtistFolderComponent: FC<MenuItemComponentProps> = (props) => {
  const navigate = useCustomNavigate();
  return MenuItemComponent({
    ...props,
    icon: <FolderPlus size={40} />,
    onClick: () => navigate(Paths.CREATE_ARTIST_FOLDER.resolve())
  });
};

export default MenuItemCreateArtistFolderComponent;
