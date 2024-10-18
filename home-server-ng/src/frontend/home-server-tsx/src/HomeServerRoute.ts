import {ReactElement} from "react";

export default class HomeServerRoute {

    static default() : HomeServerRoute {
        return new HomeServerRoute("HomeServerTsx", "");
    }

    constructor(public labelForNavBar: string, public matchingLocation: string, public icon?: ReactElement) { }

    matching(aLocation: string): boolean {
        return aLocation.toLocaleLowerCase().includes(this.matchingLocation.toLocaleLowerCase());
    }
}