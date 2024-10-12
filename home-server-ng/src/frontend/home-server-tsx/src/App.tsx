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

function App() {
  const router = createHashRouter([
    {
      path: "/",
      element: <HomeServerLayoutComponent />,
      children: [
        {
          path: "administration/properties",
          element: <AdministractionComponent></AdministractionComponent>
        },
        {
          path: "administration/status",
          element: <StatusComponent />
        },
        {
          path: "administration/events",
          element: <EventsComponent />
        },
        {
          path: "administration/tasks",
          element: <TasksComponent />
        },
        {
          path: "buzzer",
          element: <BuzzerComponent />
        },
        {
          path: "buzzer/sprite/:spriteId",
          element: <EditSpriteComponent />
        },
        {
          path: "books/allbooks",
          element: <AllBooksComponent />
        },
        {
          path: "books/serieDetails/:serieId",
          element: <FullSerieComponent />
        },
        {
          path: "books/add/serieselection",
          element: <AddBookSelectSerieComponent />
        },
        {
          path: "books/add/serie/:serieId",
          element: <AddBookOnSelectedSerieComponent />
        }
        ,
        {
          path: "files/filemanager",
          element: <FileManagerComponent />
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
