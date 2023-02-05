import React from 'react';

import FileManagerComponent from '../filemanager/FileManagerComponent';
import PluginsComponent from '../administration/PluginsComponent';
import PropertiesComponent from '../administration/PropertiesComponent';
import StatusComponent from '../administration/StatusComponent';
import SeedboxDownloadsComponents from '../seedbox/SeedboxDownloadsComponents';
import TodoComponent from '../seedbox/todo/TodoComponent';
import IotBoardComponent from '../iot/board/IotBoardComponent';
import SpriteListComponent from '../iot/sprites/SpriteListComponent';
import ManageIotComponent from '../iot/manage/ManageIotComponent';
import TriggersManagerComponent from '../iot/triggers/TriggersManagerComponent';
import AlarmComponent from '../iot/alarm/AlarmComponent';
import LogEventComponent from '../administration/LogEventsComponent';
import AllBricksComponent from '../lego/AllBricksComponent';
import RunningTasksComponent from '../administration/RunningTasksComponent';
import AddBookComponent from '../book/add/AddBookComponent';
import AllBooksComponent from '../book/AllBooksComponent';
import AllSeriesComponent from '../book/AllSeriesComponent';


export default function modulesAdapter() {

    const menuDescriptors = [{
        module: 'administration',
        icon: 'build',
        title: 'Administration',
        subMenu: [
            {
                path: '/administration/plugins',
                icon: 'settings_input_composite',
                title: 'Plugins',
                component: (<PluginsComponent></PluginsComponent>)
            },
            {
                path: '/administration/properties',
                icon: 'toc',
                title: 'Properties',
                component: (<PropertiesComponent></PropertiesComponent>)
            },
            {
                path: '/administration/status',
                icon: 'info',
                title: 'Status',
                component: (<StatusComponent></StatusComponent>)
            },
            {
                path: '/administration/tasks',
                icon: 'playlist_play',
                title: 'Tasks',
                component: (<RunningTasksComponent></RunningTasksComponent>)
            },
            {
                path: '/administration/logEvents',
                icon: 'notifications',
                title: 'Logs Events',
                component: (<LogEventComponent></LogEventComponent>)
            }
        ]
    }, {
        module: 'filemanager',
        path: '/filemanager',
        icon: 'storage',
        title: 'FileManager',
        component: (<FileManagerComponent></FileManagerComponent>),
        dontUseDefaultPanel: true
    }, {
        module: 'iot',
        path: '/iot',
        icon: 'settings_remote',
        title: 'IOT',
        subMenu: [
            {
                path: '/iot/board',
                icon: 'dashboard',
                title: 'Board',
                component: (<IotBoardComponent></IotBoardComponent>)
            },
            {
                path: '/iot/sprites',
                icon: 'brush',
                title: 'Sprites',
                component: (<SpriteListComponent></SpriteListComponent>)
            },
            {
                path: '/iot/manageiotcomponent',
                icon: 'build',
                title: 'Manage',
                component: (<ManageIotComponent></ManageIotComponent>)
            },
            {
                path: '/iot/alarm',
                icon: 'alarm',
                title: 'Alarm',
                component: (<AlarmComponent></AlarmComponent>)
            }
            // },
            // {
            //     path: '/iot/triggers',
            //     icon: 'call_merge',
            //     title: 'Triggers',
            //     component: (<TriggersManagerComponent></TriggersManagerComponent>)
            // }
        ]
    }, {
        module: 'lego',
        path: '/lego',
        icon: 'face',
        title: 'Lego',
        component: (<AllBricksComponent></AllBricksComponent>),
        dontUseDefaultPanel: true
    },
    {
        module: 'books',
        path: '/books',
        icon: 'local_library',
        title: 'Books',
        subMenu: [
            {
                path: '/books/allbooks',
                icon: 'library_books',
                title: 'Tous les livres',
                component: (<AllBooksComponent></AllBooksComponent>)
            },
            {
                path: '/books/allseries',
                icon: 'view_column',
                title: 'Toutes les séries',
                component: (<AllSeriesComponent></AllSeriesComponent>)
            },
            {
                path: '/books/search',
                icon: 'add',
                title: 'Ajouter un livre',
                component: (<AddBookComponent></AddBookComponent>)
            }
        ]
    },
    {
        module: 'music',
        path: '/music',
        icon: 'music_note',
        title: 'Music',
        component: (<FileManagerComponent></FileManagerComponent>)
    }, {
        module: 'kiosk',
        path: '/kiosk',
        icon: 'fullscreen',
        title: 'Kiosk',
        component: (<FileManagerComponent></FileManagerComponent>)
    }, {
        module: 'seedbox',
        path: '/seedbox',
        icon: 'cloud_download',
        title: 'Seedbox',
        subMenu: [
            {
                path: '/seedbox/downloads',
                icon: 'import_export',
                title: 'Downloads',
                component: (<SeedboxDownloadsComponents></SeedboxDownloadsComponents>)
            },
            {
                path: '/seedbox/todo',
                icon: 'new_releases',
                title: 'Todo',
                component: (<TodoComponent></TodoComponent>),
                dontUseDefaultPanel: true
            }
        ]
    }];

    const getMenuDescriptor = (module) => menuDescriptors.filter(oneDescriptor => module.moduleId === oneDescriptor.module).shift();

    // const getMenuDescriptorForPath = (path) => menuDescriptors
    //     .flatMap(oneDescriptor => oneDescriptor.subMenu ? oneDescriptor.subMenu : [oneDescriptor])
    //     .filter(oneDescriptor => path === oneDescriptor.path).shift();

    const getMenuDescriptorForPath = (path) => menuDescriptors
        .flatMap(oneDescriptor => oneDescriptor.subMenu ? [oneDescriptor].concat(oneDescriptor.subMenu) : [oneDescriptor])
        .find(oneDescriptor => path === oneDescriptor.path);

    const homeMenuDescriptor = () => { return { title: 'HomeServer' } };


    const componentMap = {
        administration: {
            components: [
                { component: PluginsComponent, id: 'plugins', title: 'Plugins', icon: 'settings_input_component' },
                { component: (<PropertiesComponent></PropertiesComponent>), id: 'properties', title: 'Properties', icon: 'toc' }
            ], icon: 'build'
        },
        filemanager: { component: (<FileManagerComponent></FileManagerComponent>), icon: 'storage' },
        iot: { component: (<FileManagerComponent></FileManagerComponent>), icon: 'settings_remote' },
        music: { component: (<FileManagerComponent></FileManagerComponent>), icon: 'music_note' },
        kiosk: { component: (<FileManagerComponent></FileManagerComponent>), icon: 'fullscreen' },
        seedbox: { component: (<FileManagerComponent></FileManagerComponent>), icon: 'cloud_download' },
    };

    const resolveIcon = (componentName) => componentMap[componentName].icon;
    const resolveComponentFromLocation = (location, loadedComponents) =>
        loadedComponents
            .filter(oneComponent => location.pathname.indexOf(oneComponent.moduleId) !== -1)
            .shift();
    const resolveMenuDescriptorForModule = (module) => {
        return componentMap[module.moduleId];
    };

    const buildRoutes = () => {
        let routes = [];
        new Map(Object.entries(componentMap)).forEach((value, key) => {
            if (value.components) {
                routes = routes.concat(value.components.map(oneComponent => {
                    return {
                        path: '/' + key + '/' + oneComponent.id,
                        component: oneComponent.component
                    }
                }));
            } else {
                routes.push({ path: '/' + key, component: value.component });
            }
        });
        return routes;
    }

    const buildRoutesAsMap = () => {
        const routesList = buildRoutes();
        const routesMap = new Map();
        routesList.forEach(oneRoute => routesMap.set(oneRoute.path, oneRoute.component));
        return routesMap;
    }


    return {
        resolveIcon: resolveIcon,
        resolveComponentFromLocation: resolveComponentFromLocation,
        resolveMenuDescriptorForModule: resolveMenuDescriptorForModule,
        buildRoutes: buildRoutes,
        buildRoutesAsMap: buildRoutesAsMap,
        getMenuDescriptor: getMenuDescriptor,
        homeMenuDescriptor: homeMenuDescriptor,
        getMenuDescriptorForPath: getMenuDescriptorForPath
    }

}
