import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import { type Icon } from "react-bootstrap-icons";

export type MenuItemSwitchComponentBasicProps = {
  on:boolean
}
1
export type MenuItemSwitchComponentProps = {
  onElement: React.ReactElement<Icon>,
  offElement: React.ReactElement<Icon>,
}

const MenuItemSwitchComponent: FC<MenuItemComponentProps & MenuItemSwitchComponentProps & MenuItemSwitchComponentBasicProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: props.on ? <props.offElement.type size={40} /> : <props.onElement.type size={40} />
  });
};

export default MenuItemSwitchComponent;
