import AdminComponent from "../../components/admin/AdminComponent";
import AddAlbumComponent from "../../components/album/AddAlbumComponent";
import OneAlbumComponent from "../../components/album/OneAlbumComponent";
import AddArtistComponent from "../../components/artists/AddArtistComponent";
import AllArtistsComponent from "../../components/artists/AllArtistsComponent";
import AllTracksForArtistComponent from "../../components/artists/AllTracksForArtistComponent";
import OneArtistComponent from "../../components/artists/OneArtistComponent";
import PlayerComponent from "../../components/player/PlayerComponent";
import Path from "./Path";

export default class Paths {
    public static readonly ONE_ARTIST = new Path("/artists/:artistId", () => <OneArtistComponent />, ["artistId"]); 
    public static readonly ONE_ALBUM = new Path("/artists/:artistId/albums/:albumId", () => <OneAlbumComponent />, ["artistId", "albumId"]); 
    public static readonly ALL_TRACKS_FOR_ARTIST = new Path("/artists/:artistId/alltracks", () => <AllTracksForArtistComponent />, ["artistId"]); 
    public static readonly ALL_ARTISTS = new Path("/", () =>  <AllArtistsComponent />); 
    public static readonly ADD_ARTIST = new Path("/artists/add", () =>  <AddArtistComponent />); 
    public static readonly ADD_ALBUM = new Path("/artists/:artistId/albums/add", () =>  <AddAlbumComponent />, ["artistId"]); 
    public static readonly PLAYER = new Path("/player", () =>  <PlayerComponent />); 
    public static readonly ADMIN = new Path("/admin", () =>  <AdminComponent />); 

}