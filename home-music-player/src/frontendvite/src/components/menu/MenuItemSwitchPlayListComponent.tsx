import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import { BookmarkCheck, BookmarkX } from "react-bootstrap-icons";
import MenuItemSwitchComponent, { type MenuItemSwitchComponentBasicProps } from "./MenuItemSwitchComponent";

const MenuItemSwitchPlayListComponent: FC<MenuItemSwitchComponentBasicProps & MenuItemComponentProps> = (props) => {
  return MenuItemSwitchComponent({
    ...props,
    onElement : <BookmarkCheck />,
    offElement : <BookmarkX />
  });
};

export default MenuItemSwitchPlayListComponent;
