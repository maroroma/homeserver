import "./App.css";
import {createHashRouter, RouterProvider} from "react-router";
import MusicPlayerLayoutComponent from "./components/MusicPlayerLayoutComponent";
import Paths from "./tools/routes/Paths";
import {MusicPlayerProvider} from "./state/MusicPlayerContext";

function App() {
  const router = createHashRouter([
    {
      path: "/",
      element: <MusicPlayerLayoutComponent />,
      children: [
        Paths.ALL_ARTISTS.toRoute(),
        Paths.ONE_ARTIST.toRoute(),
        Paths.ONE_ALBUM.toRoute(),
        Paths.ALL_TRACKS_FOR_ARTIST.toRoute(),
        Paths.ADD_ARTIST.toRoute(),
        Paths.PLAYER.toRoute(),
        Paths.ADD_ALBUM.toRoute()
      ],
    },
  ]);

  return (
    <>
      <MusicPlayerProvider>
        <RouterProvider router={router} />
      </MusicPlayerProvider>
    </>
  );
}

export default App;
