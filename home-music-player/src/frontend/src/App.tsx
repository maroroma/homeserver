import React from 'react';
import './App.css';
import {MusicPlayerProvider} from './state/MusicPlayerContext';
import AllArtistsComponent from './components/library/allArtists/AllArtistsComponent';
import ToasterComponent from './components/ToasterComponent';
import AddArtistPopupComponent from './components/library/allArtists/AddArtistPopupComponent';
import ArtistView from './components/library/artist/ArtistView';
import ViewPortComponent from './components/common/ViewPortComponent';
import {ViewState} from './state/ViewState';
import DeleteArtistPopupComponent from './components/library/artist/DeleteArtistPopupComponent';
import EditArtistComponent from './components/library/artist/EditArtistComponent';
import AlbumViewComponent from './components/library/tracks/AlbumViewComponent';
import DeleteAlbumComponent from './components/library/tracks/DeleteAlbumComponent';
import AddAlbumPopupComponent from './components/library/artist/AddAlbumPopupComponent';
import AllTracksForArtistComponent from './components/library/tracks/AllTracksForArtistComponent';
import WebSocketHandlerComponent from './components/websocket/WebSocketHandlerComponent';
import SmallPlayerComponent from './components/player/SmallPlayerComponent';
import FullscreenPlayerComponent from './components/player/FullscreenPlayerComponent';

function App() {
  return (
    <div className="app-background">
      <MusicPlayerProvider>

        <ViewPortComponent view={ViewState.AllArtists}>
          <AllArtistsComponent></AllArtistsComponent>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.AddArtist}>
          <AddArtistPopupComponent></AddArtistPopupComponent>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.Artist}>
          <ArtistView></ArtistView>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.DeleteArtist}>
          <DeleteArtistPopupComponent></DeleteArtistPopupComponent>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.EditArtist}>
          <EditArtistComponent></EditArtistComponent>
        </ViewPortComponent>

        <ViewPortComponent view={ViewState.AlbumWithTracks}>
          <AlbumViewComponent></AlbumViewComponent>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.DeleteAlbum}>
          <DeleteAlbumComponent></DeleteAlbumComponent>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.AddAlbum}>
          <AddAlbumPopupComponent></AddAlbumPopupComponent>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.AllTracksForArtist}>
          <AllTracksForArtistComponent></AllTracksForArtistComponent>
        </ViewPortComponent>
        <ViewPortComponent view={ViewState.FullScreenPlayer}>
          <FullscreenPlayerComponent></FullscreenPlayerComponent>
        </ViewPortComponent>

      

        <SmallPlayerComponent></SmallPlayerComponent>

        <WebSocketHandlerComponent></WebSocketHandlerComponent>

        <ToasterComponent></ToasterComponent>
      </MusicPlayerProvider>
    </div>
  );
}

export default App;
