import React from 'react';

import FileManagerComponent from '../filemanager/FileManagerComponent';
import PluginsComponent from '../administration/PluginsComponent';
import PropertiesComponent from '../administration/PropertiesComponent';


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
        component: (<FileManagerComponent></FileManagerComponent>)
    }, {
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
                component: (<PluginsComponent></PluginsComponent>)
            },
            {
                path: '/seedbox/todo',
                icon: 'new_releases',
                title: 'Todo',
                component: (<PropertiesComponent></PropertiesComponent>)
            }
        ]
    }];

    const getMenuDescriptor = (module) => menuDescriptors.filter(oneDescriptor => module.moduleId === oneDescriptor.module).shift();
    const getMenuDescriptorForPath = (path) => menuDescriptors
        .flatMap(oneDescriptor => oneDescriptor.subMenu ? oneDescriptor.subMenu : [oneDescriptor])
        .filter(oneDescriptor => path === oneDescriptor.path).shift();
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
