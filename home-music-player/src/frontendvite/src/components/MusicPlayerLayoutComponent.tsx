import {type FC} from "react";
import {Toast, ToastContainer} from "react-bootstrap";
import {Outlet} from "react-router";
import {useMusicPlayerContext} from "../state/MusicPlayerContext";

import "./MusicPlayerLayoutComponent.css";
import ScrollHandlerComponent from "./ScrollHandlerComponent";
import PlayerStatusHandlerComponent from "./player/PlayerStatusHandlerComponent";

const MusicPlayerLayoutComponent: FC = () => {
  const { toastState } = useMusicPlayerContext();

  return (
    <>
      <div className="outlet-content">
        <Outlet></Outlet>
      </div>
      <ScrollHandlerComponent />
      <PlayerStatusHandlerComponent />

      <ToastContainer position="middle-end">
        <Toast
          show={toastState.toastMessage !== undefined}
        >
          <Toast.Header closeButton={false}>
            <strong className="me-auto">
              {toastState.toastMessage?.title}{" "}
            </strong>
          </Toast.Header>
          <Toast.Body>{toastState.toastMessage?.message}</Toast.Body>
        </Toast>
        <Toast
          show={toastState.autoHideToastMessage !== undefined}
          delay={5000}
          autohide={true}
        >
          <Toast.Header closeButton={false}>
            <strong className="me-auto">
              {toastState.autoHideToastMessage?.title}{" "}
            </strong>
          </Toast.Header>
          <Toast.Body>{toastState.autoHideToastMessage?.message}</Toast.Body>
        </Toast>
      </ToastContainer>
    </>
  );
};

export default MusicPlayerLayoutComponent;
