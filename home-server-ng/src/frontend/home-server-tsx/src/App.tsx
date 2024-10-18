import React from 'react';
import './App.css';
import './minty.bootstrap.min.css';
import {HomeServerProvider} from './context/HomeServerRootContext';
import {ThemeProvider} from 'react-bootstrap';
import HomeServerLayoutComponent from './components/layouts/HomeServerLayoutComponent';
import {createHashRouter, RouterProvider} from 'react-router-dom';
import AdministractionComponent from './components/administration/AdministractionComponent';
import BuzzerComponent from './components/iot/BuzzerComponent';
import StatusComponent from './components/administration/StatusComponent';
import EventsComponent from './components/administration/EventsComponent';
import TasksComponent from './components/administration/TasksComponent';
import AllBooksComponent from './components/books/AllBooksComponent';
import FullSerieComponent from './components/books/FullSerieComponent';
import AddBookSelectSerieComponent from './components/books/addbooks/AddBookSelectSerieComponent';
import AddBookOnSelectedSerieComponent from './components/books/addbooks/AddBookOnSelectedSerieComponent';
import FileManagerComponent from './components/filemanager/FileManagerComponent';
import EditSpriteComponent from './components/iot/EditSpriteComponent';
import LegoComponent from './components/lego/LegoComponent';
import HomeServerRoutes from './HomeServerRoutes';
import SeedBoxComponent from './components/seedbox/SeedBoxComponent';
import SeedBoxTodoComponent from './components/seedbox/SeedBoxTodoComponent';

function App() {
  const router = createHashRouter([
    {
      path: "/",
      element: <HomeServerLayoutComponent />,
      children: [
        {
          path: HomeServerRoutes.ADMINISTRATION_PROPERTIES,
          element: <AdministractionComponent></AdministractionComponent>
        },
        {
          path: HomeServerRoutes.ADMINISTRATION_STATUS,
          element: <StatusComponent />
        },
        {
          path: HomeServerRoutes.ADMINISTRATION_EVENTS,
          element: <EventsComponent />
        },
        {
          path: HomeServerRoutes.ADMINISTRATION_TASKS,
          element: <TasksComponent />
        },
        {
          path: HomeServerRoutes.IOT_BUZZER,
          element: <BuzzerComponent />
        },
        {
          path: HomeServerRoutes.IOT_BUZZER_SPRITE,
          element: <EditSpriteComponent />
        },
        {
          path: HomeServerRoutes.BOOKS_ALL,
          element: <AllBooksComponent />
        },
        {
          path: HomeServerRoutes.BOOKS_SERIE_DETAIL,
          element: <FullSerieComponent />
        },
        {
          path: HomeServerRoutes.BOOKS_SERIE_SELECTION,
          element: <AddBookSelectSerieComponent />
        },
        {
          path: HomeServerRoutes.BOOKS_ADD_BOOK_TO_SELECTED_SERIE,
          element: <AddBookOnSelectedSerieComponent />
        },
        {
          path: HomeServerRoutes.FILE_MANAGER,
          element: <FileManagerComponent />
        },
        {
          path: HomeServerRoutes.LEGO,
          element: <LegoComponent />
        },
        {
          path: HomeServerRoutes.SEEDBOX_TORRENTS,
          element: <SeedBoxComponent />
        }
        ,
        {
          path: HomeServerRoutes.SEEDBOX_TODO,
          element: <SeedBoxTodoComponent />
        }
      ]
    }
  ])
  return (
    <div className="App">
      <ThemeProvider >
        <HomeServerProvider>
          <RouterProvider router={router}></RouterProvider>
        </HomeServerProvider>
      </ThemeProvider>
    </div>
  );
}

export default App;
