import {Bell, Book, Boxes, BoxSeam, FolderSymlink, Tools} from "react-bootstrap-icons"
import HomeServerRoute from "./HomeServerRoute"
import {CustomClassNames} from "./components/bootstrap/CssTools"

export default class HomeServerRoutes {

    static readonly ADMINISTRATION_PROPERTIES: string = "administration/properties"
    static readonly ADMINISTRATION_STATUS: string = "administration/status"
    static readonly ADMINISTRATION_EVENTS: string = "administration/events"
    static readonly ADMINISTRATION_TASKS: string = "administration/tasks"

    static readonly IOT_BUZZER: string = "buzzer"
    static readonly IOT_BUZZER_SPRITE: string = "buzzer/sprite/:spriteId"
    static IOT_BUZZER_SPRITE_ID(id: string): string {
        return `/buzzer/sprite/${id}`;
    }


    static readonly BOOKS_ALL: string = "books/allbooks"
    static readonly BOOKS_SERIE_DETAIL: string = "books/serieDetails/:serieId"
    static BOOKS_SERIE_DETAIL_ID(serieId: string): string {
        return `/books/serieDetails/${serieId}`;
    }
    static readonly BOOKS_SERIE_SELECTION: string = "books/add/serieselection"
    static readonly BOOKS_ADD_BOOK_TO_SELECTED_SERIE: string = "books/add/serie/:serieId"
    static BOOKS_ADD_BOOK_TO_SELECTED_SERIE_ID(serieId: string): string {
        return `/books/add/serie/${serieId}`;
    }


    static readonly LEGO: string = "legos"

    static readonly FILE_MANAGER: string = "files/filemanager"


    static readonly SEEDBOX_TORRENTS: string = "seedbox/torrents"
    static readonly SEEDBOX_TODO: string = "seedbox/todo"



    static readonly LABELED_ROUTES = [
        new HomeServerRoute("Administration", "admin", <Tools className={CustomClassNames.HideOnLargeDevice}/>),
        new HomeServerRoute("Buzzer", "buzzer", <Bell className={CustomClassNames.HideOnLargeDevice}/>),
        new HomeServerRoute("Files", "files", <FolderSymlink className={CustomClassNames.HideOnLargeDevice}/>),
        new HomeServerRoute("Seedbox", "seedbox", <BoxSeam className={CustomClassNames.HideOnLargeDevice}/>),
        new HomeServerRoute("Books", "book", <Book className={CustomClassNames.HideOnLargeDevice}/>),
        new HomeServerRoute("Lego", "lego", <Boxes className={CustomClassNames.HideOnLargeDevice}/>),
    ]

    static getLabeledRoute(aLocation: string): HomeServerRoute {
        const labeledRoute = HomeServerRoutes.LABELED_ROUTES.find(aLabeledRoute => aLabeledRoute.matching(aLocation));

        return labeledRoute ? labeledRoute : new HomeServerRoute("HomeServerTSX", "");
    }



}