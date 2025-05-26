import {FC, ReactElement} from "react";
import {Button, Nav, Stack} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {useHomeServerContext} from "../../context/HomeServerRootContext";
import UpdateSearchStringAction from "../../context/actions/UpdateSearchStringAction";
import {Bell, DiscFill, FolderSymlink} from "react-bootstrap-icons";
import HomeServerRoutes from "../../HomeServerRoutes";
import {BootstrapVariants} from "../bootstrap/BootstrapVariants";
import SimpleMarginLayout from "./SimpleMarginLayout";

import "./DefaultStartComponent.css";
import CssTools from "../bootstrap/CssTools";
import {PlayerStatus} from "../../model/musicplayer/PlayerStatus";

export type StartShortCutButtonProps = {
  icon: ReactElement;
  label?: string;
  path: string;
};

const StartShortCutButton: FC<StartShortCutButtonProps> = ({
  icon,
  label = "",
  path,
}) => {
  const navigate = useNavigate();
  const { dispatch } = useHomeServerContext();
  return (
    <Button variant={BootstrapVariants.Dark}>
      <Nav.Link
        onClick={() => {
          navigate(path);
          dispatch(UpdateSearchStringAction.clear());
        }}
      >
        {icon} {label}
      </Nav.Link>
    </Button>
  );
};

const DefaultStartComponent: FC = () => {
  const { musicPlayerSubState } = useHomeServerContext();
  return (
    <SimpleMarginLayout>
      <Stack gap={3} className="hide-on-large-devices">
        <div className="start-button">
          <StartShortCutButton
            icon={<Bell size={100} className="tiltable" />}
            path={HomeServerRoutes.IOT_BUZZER}
          />
        </div>
        <div className="start-button">
          <StartShortCutButton
            icon={<FolderSymlink size={100} />}
            path={HomeServerRoutes.FILE_MANAGER}
          />
        </div>
        <div className="start-button">
          <Button variant={BootstrapVariants.Dark}>
            <Nav.Link
              href={musicPlayerSubState.musicPlayerStatus.musicPlayerUrl}
              target="_blank"
            >
              <DiscFill
                size={100}
                className={CssTools.of()
                  .if(
                    musicPlayerSubState.musicPlayerStatus.playerStatus ===
                      PlayerStatus.PLAYING,
                    "endless-rotation"
                  )
                  .if(
                    musicPlayerSubState.musicPlayerStatus.playerStatus ===
                      PlayerStatus.PAUSED,
                    "blinkable"
                  )
                  .css()}
              />
            </Nav.Link>
          </Button>
        </div>
      </Stack>
    </SimpleMarginLayout>
  );
};

export default DefaultStartComponent;
