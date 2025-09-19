import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {ArrowUpCircle} from "react-bootstrap-icons";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import WindowTool from "../../tools/WindowTool";

import "./MenuComponent.css"

const MenuItemScrollToTopComponent: FC<MenuItemComponentProps> = (props) => {
  const { isScrollOnTop } = useMusicPlayerContext();

  return (
    <>
      {isScrollOnTop ? (
        <></>
      ) : (
        MenuItemComponent({
          ...props,
          icon: <ArrowUpCircle size={40} />,
          onClick : () => WindowTool.scrollToTop()
        })
      )}
    </>
  );
};

export default MenuItemScrollToTopComponent;
