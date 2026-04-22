import type { FC } from "react";
import { Button } from "react-bootstrap";
import { Question } from "react-bootstrap-icons";

export type MenuItemComponentProps = {
  icon?: React.ReactElement;
  onClick?: () => void;
  className?: string;
  disabled?: boolean;
};

const MenuItemComponent: FC<MenuItemComponentProps> = ({
  icon = <Question size={40} />,
  onClick = () => { },
  className = "",
  disabled = false
}) => {
  return (
    <>
      <Button variant="light" className={`menu-item ${className}`} onClick={() => onClick()} disabled={disabled}>
        {icon}
      </Button>
    </>
  );
};

export default MenuItemComponent;
