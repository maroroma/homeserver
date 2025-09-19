/* eslint-disable react-refresh/only-export-components */
/* eslint-disable @typescript-eslint/no-explicit-any */
import {createContext, type FC, useContext, useReducer} from "react";
import type {MusicPlayerState} from "./MusicPlayerState";
import type {MusicPlayerContextAction} from "./actions/MusicPlayerContextActions";
import {PlayerStatusEvent} from "../api/model/player/PlayerStatusEvent";

const initialState: MusicPlayerState = {
  dispatch: () => {},
  toastState: {},
  isScrollOnTop: true,
  playerStatus: PlayerStatusEvent.empty(),
};

const reducer = (
  previousState: MusicPlayerState,
  action: MusicPlayerContextAction
) => {
  return action.applyToState(previousState);
};

const MusicPlayerContext = createContext<MusicPlayerState>(initialState);

type MusicPlayerProviderProps = {
  children: any;
};

const MusicPlayerProvider: FC<MusicPlayerProviderProps> = (props) => {
  const [state, dispatch] = useReducer(reducer, initialState);

  return (
    <MusicPlayerContext.Provider value={{ ...state, dispatch }}>
      {props.children}
    </MusicPlayerContext.Provider>
  );
};

const useMusicPlayerContext: () => MusicPlayerState = () =>
  useContext(MusicPlayerContext);

export { MusicPlayerProvider, useMusicPlayerContext };
