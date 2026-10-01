import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {Search} from "react-bootstrap-icons";
import { useCustomNavigate } from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";

const MenuItemSearchComponent: FC<MenuItemComponentProps> = (props) => {
  const navigate = useCustomNavigate();

  return MenuItemComponent({
    ...props,
    icon: <Search size={40} />,
    onClick: () => navigate(Paths.SEARCH.resolve())
  });
};

export default MenuItemSearchComponent;
