import type {FC} from "react";
import type {MenuItemComponentProps} from "./MenuItemComponent";
import MenuItemComponent from "./MenuItemComponent";
import {DiscFill} from "react-bootstrap-icons";
import {useMusicPlayerContext} from "../../state/MusicPlayerContext";
import CssTools from "../../tools/CssTools";
import {useCustomNavigate} from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";

const MenuItemGoToPlayerComponent: FC<MenuItemComponentProps> = (props) => {
  const { playerStatus } = useMusicPlayerContext();
  const navigate = useCustomNavigate();

  return (
    <>
      {playerStatus.playerStatus !== "STOPPED" ? (
        MenuItemComponent({
          ...props,
          onClick: () => navigate(Paths.PLAYER.resolve()),
          icon: (
            <DiscFill
              size={40}
              className={CssTools.of()
                .if(playerStatus.playerStatus === "PLAYING", "endless-rotation")
                .if(playerStatus.playerStatus === "PAUSED", "blinkable")
                .if(playerStatus.playerStatus === "LOADING", "blinkable")
                .css()}
            />
          ),
        })
      ) : (
        <></>
      )}
    </>
  );
};

export default MenuItemGoToPlayerComponent;
