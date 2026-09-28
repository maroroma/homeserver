import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import { CloudDownload, CloudSlash } from "react-bootstrap-icons";
import MenuItemSwitchComponent, { type MenuItemSwitchComponentBasicProps } from "./MenuItemSwitchComponent";

const MenuItemSwitchDownloadComponent: FC<MenuItemSwitchComponentBasicProps & MenuItemComponentProps> = (props) => {
  return MenuItemSwitchComponent({
    ...props,
    onElement : <CloudDownload />,
    offElement : <CloudSlash />
  });
};

export default MenuItemSwitchDownloadComponent;
