import { type FC, useEffect } from "react";
import FadeInPage from "../FadeInPage";
import MenuComponent from "../menu/MenuComponent";
import MenuItemGoToAllArtistComponent from "../menu/MenuItemGoToAllArtistComponent";
import { Button, ButtonGroup, Carousel, Stack } from "react-bootstrap";
import { useMusicPlayerContext } from "../../state/MusicPlayerContext";
import { useCustomNavigate } from "../hooks/CustomHooks";
import Paths from "../../tools/routes/Paths";
import FanArtComponent from "../fanart/FanArtComponent";

import "./PlayerComponent.css";
import LibraryListItemRenderer from "../renderers/LibraryListItemRenderer";
import IconListItemRenderer from "../renderers/IconListItemRenderer";
import { ChevronDoubleLeft, ChevronDoubleRight, List, MusicNoteBeamed, Pause, Play, Shuffle, Stop, VolumeDown, VolumeUp, } from "react-bootstrap-icons";
import MenuItemAddToPlaylistsComponent from "../menu/MenuItemAddToPlaylistsComponent";
import { NameTransformer } from "../../tools/NameTransformer";
import VynilComponent from "../thumb/VynilComponent";
import { StopEmbeddedPlayerAction } from "../../state/actions/StopEmbeddedPlayerAction";
import { VolumeDownAction } from "../../state/actions/VolumeDownAction";
import { VolumeUpAction } from "../../state/actions/VolumeUpAction";
import { PauseEmbeddedPlayerAction } from "../../state/actions/PauseEmbeddedPlayerAction";
import { ResumeEmbeddedPlayerAction } from "../../state/actions/ResumeEmbeddedPlayerAction";
import { NextTrackAction } from "../../state/actions/NextTrackAction";
import { PreviousTrackAction } from "../../state/actions/PreviousTrackAction";
import { ShuffleTrackListAction } from "../../state/actions/ShuffleTrackListAction";
import MenuItemComponent from "../menu/MenuItemComponent";

const PlayerComponent: FC = () => {
  const navigate = useCustomNavigate();
  const { playerStatus, dispatch } = useMusicPlayerContext();

  useEffect(() => {
    if (playerStatus.playerStatus === "STOPPED") {
      navigate(Paths.ALL_ARTISTS.resolve());
    }
  }, [playerStatus]);

  if (playerStatus.album === null || playerStatus.album === undefined) {
    return <></>;
  }

  return (
    <FadeInPage>
      <div className="player-component">
        <Stack gap={5}>
          <div
            className="text-center clickable"
            onClick={() =>
              navigate(
                Paths.ONE_ALBUM.resolve([
                  playerStatus.artist.id,
                  playerStatus.album.id,
                ])
              )
            }
          >
            <VynilComponent
              libraryItemArts={playerStatus.album.libraryItemArts}
              artistArt={playerStatus.artist?.libraryItemArts}
              album={playerStatus.album} />
          </div>
          <Carousel controls={false} indicators={false} className="carousel">
            <Carousel.Item>
              <IconListItemRenderer
                icon={<MusicNoteBeamed />}
                size="small"
                label={NameTransformer.trackName(playerStatus.track)}
                onClick={() =>
                  navigate(
                    Paths.ONE_ALBUM.resolve([
                      playerStatus.artist.id,
                      playerStatus.album.id,
                    ])
                  )
                }
              />
            </Carousel.Item>
            <Carousel.Item>
              <LibraryListItemRenderer
                size="small"
                label={playerStatus.artist.name}
                libraryItemArts={playerStatus.artist.libraryItemArts}
                onClick={() =>
                  navigate(Paths.ONE_ARTIST.resolve(playerStatus.artist.id))
                }
              />
            </Carousel.Item>
          </Carousel>
          <div className="text-center">
            <ButtonGroup size="lg">
              <Button
                variant="light"
                disabled={playerStatus.playerStatus !== "PLAYING"}
                onClick={() => dispatch(new VolumeDownAction())}
              >
                <VolumeDown size={50} />
              </Button>
              <Button
                variant="light"
                disabled={playerStatus.playerStatus !== "PLAYING"}
              >
                <div className="volume">{playerStatus.volume}</div>
              </Button>
              <Button
                variant="light"
                disabled={playerStatus.playerStatus !== "PLAYING"}
                onClick={() => dispatch(new VolumeUpAction())}
              >
                <VolumeUp size={50} />
              </Button>
            </ButtonGroup>
          </div>
          <div className="text-center">
            <ButtonGroup size="lg">
              <Button
                variant="outline-light"
                disabled={playerStatus.playerStatus !== "PLAYING"}
                onClick={() => dispatch(new PreviousTrackAction())}
              >
                <ChevronDoubleLeft size={40} />
              </Button>
              <Button
                variant="outline-light"
                disabled={playerStatus.playerStatus !== "PLAYING"}
                onClick={() => dispatch(new ShuffleTrackListAction())}
              >
                <Shuffle size={40} />
              </Button>
              {
                playerStatus.playerStatus === "PAUSED" ?
                  <Button
                    variant="outline-light"
                    className="blinkable"
                    onClick={() => dispatch(new ResumeEmbeddedPlayerAction())}
                  >
                    <Play size={40} />
                  </Button>
                  : <></>
              }
              {
                playerStatus.playerStatus === "PLAYING" ?
                  <Button
                    variant="outline-light"
                    onClick={() => dispatch(new PauseEmbeddedPlayerAction())}
                  >
                    <Pause size={40} />
                  </Button>
                  : <></>
              }

              <Button
                variant="outline-light"
                disabled={playerStatus.playerStatus !== "PLAYING"}
                onClick={() => dispatch(new StopEmbeddedPlayerAction())}
              >
                <Stop size={40} />
              </Button>
              <Button
                variant="outline-light"
                disabled={playerStatus.playerStatus !== "PLAYING"}
                onClick={() => dispatch(new NextTrackAction())}
              >
                <ChevronDoubleRight size={40} />
              </Button>
            </ButtonGroup>
          </div>
        </Stack>
      </div>
      <FanArtComponent fanart={playerStatus.artist.libraryItemArts} />
      <MenuComponent displayGoToPlayer={false}>
        <MenuItemGoToAllArtistComponent />
        <MenuItemAddToPlaylistsComponent currentTrack={playerStatus.track} />
        <MenuItemComponent icon={<List size={40}/>} onClick={() => navigate(Paths.CURRENT_TRACK_LIST.resolve())}/>
      </MenuComponent>
    </FadeInPage>
  );
};

export default PlayerComponent;
