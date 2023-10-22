import React, {useEffect, useState} from 'react';
import eventReactor from '../../eventReactor/EventReactor';
import {DISPLAYABLE_MODULES_CHANGED} from '../../eventReactor/EventIds';
import modulesAdapter from './ModulesAdapter';
import BrandLogoComponent from './BrandLogoComponent';
import MainMenuItemComponent from './MainMenuItemComponent';
import SideBarMenuItemComponent from './SideBarMenuItemComponent';
import mofRouterEventReactor from './MOFRouterEventReactor';
import {SearchDisplayButtonComponent} from './SearchBarComponent';

export default function MainMenu() {
    const [selectedModule, setSelectedModule] = useState({
        selectedMenuDescriptor: modulesAdapter().homeMenuDescriptor(),
        sideBarEnabled: undefined
    });
    const [menuDescriptors, setMenuDescriptors] = useState([]);

    // init materialize pour le sidenav
    useEffect(() => {
        document.addEventListener('DOMContentLoaded', function () {
            window.M.Sidenav.init(document.querySelectorAll('.sidenav'));
        });

        const unsubscribeDiplayableModulesChange = eventReactor().subscribe(DISPLAYABLE_MODULES_CHANGED, (displayableModules) => {
            setMenuDescriptors(displayableModules.map(oneDisplayableModule => modulesAdapter().getMenuDescriptor(oneDisplayableModule)));
        });

        const unsubscribeSelectedModuleChange = mofRouterEventReactor().onSelectedModuleChange(moduleChangedEvent => updateSelectedModule(moduleChangedEvent.newSelectedModule));

        return () => {
            unsubscribeDiplayableModulesChange();
            unsubscribeSelectedModuleChange();
        }
    }, []);

    const updateDisplayModuleNameStatus = (input) => {
        if (input && selectedModule.sideBarEnabled === undefined) {
            if (getComputedStyle(input, null).display === 'block') {
                setSelectedModule({ ...selectedModule, sideBarEnabled: true });
            } else {
                setSelectedModule({ ...selectedModule, sideBarEnabled: false });
            }
        }
    };


    const updateSelectedModule = (newSelectedModuleDescriptor) => {
        setSelectedModule({ ...selectedModule, selectedMenuDescriptor: newSelectedModuleDescriptor })
    };

    // gestion de l'affichage spécifique pour le mobile
    let specificDisplayForSideBar = { title: null, icon: null };
    if (selectedModule.sideBarEnabled) {
        specificDisplayForSideBar = {
            title: selectedModule.selectedMenuDescriptor.title,
            icon: (<i className="material-icons right">{selectedModule.selectedMenuDescriptor.icon}</i>)
        };
    }

    let mainNavBarContent = null;
        mainNavBarContent = (<>
            {specificDisplayForSideBar.title}
            <BrandLogoComponent selectedModule={selectedModule}></BrandLogoComponent>
            <a href="#!" data-target="mobile-demo" className="sidenav-trigger" ref={updateDisplayModuleNameStatus}>
                <i className="material-icons left">menu</i>
                {specificDisplayForSideBar.icon}
            </a>
            <ul className="hide-on-med-and-down">
                {
                    menuDescriptors.map((oneMenuDescriptor, index) => {
                        return (
                            <MainMenuItemComponent key={"MainMenuItemComponent_" + oneMenuDescriptor.title} menuDescriptor={oneMenuDescriptor} currentModule={selectedModule.selectedMenuDescriptor}></MainMenuItemComponent>
                        );
                    })
                }
                <li><SearchDisplayButtonComponent></SearchDisplayButtonComponent></li>
            </ul>
        </>
        );

    return (<>
        {/* Menu principal (pc) */}
        <div className="navbar-fixed">
            <nav>
                <div className="nav-wrapper">
                    {mainNavBarContent}
                </div>
            </nav>
        </div>

        {/* Menu sidebar (mobile) */}
        <ul className="sidenav" id="mobile-demo">
            {
                menuDescriptors.map((oneMenuDescriptor, index) => {
                    return (
                        <SideBarMenuItemComponent key={"SideBarMenuItemComponent_" + oneMenuDescriptor.title} menuDescriptor={oneMenuDescriptor}></SideBarMenuItemComponent>
                    );
                })
            }
        </ul>
    </>
    )

}