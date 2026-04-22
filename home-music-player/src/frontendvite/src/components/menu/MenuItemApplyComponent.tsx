import type { FC } from "react";
import type { MenuItemComponentProps } from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import { CheckCircle } from "react-bootstrap-icons";
import CssTools from "../../tools/CssTools";

type MenuItemApplyComponentProps = {
  valid: boolean
}

const MenuItemApplyComponent: FC<MenuItemComponentProps & MenuItemApplyComponentProps> = (props) => {
  return MenuItemComponent({
    ...props,
    icon: <CheckCircle size={40} className={CssTools.of().ifElse(props.valid, "green", "red").css()} />,
    disabled: !props.valid
  });
};

export default MenuItemApplyComponent;
